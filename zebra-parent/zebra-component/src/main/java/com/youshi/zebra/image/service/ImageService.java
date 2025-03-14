package com.youshi.zebra.image.service;

import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.dorado.framework.cache.RequestContextCache;
import com.dorado.framework.cache.access.JsonRedisCacheAccess;
import com.dorado.framework.constants.InProduction;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.util.UuidUtils;
import com.dorado.framework.crud.service.AbstractService;
import com.dorado.framework.event.utils.PerfUtils;
import com.dorado.framework.jedis.JedisByZooKeeper;
import com.dorado.framework.utils.MonitorUtils;
import com.dorado.framework.utils.RetrieveIdUtils;
import com.dorado.framework.utils.RetrieveIdUtils.IMultiDataAccess;
import com.ecyrd.speed4j.StopWatch;
import com.github.phantomthief.util.ObjectMapperUtils;
import com.youshi.zebra.aliyun.constants.AliyunConstants;
import com.youshi.zebra.core.constants.CommonKey;
import com.youshi.zebra.core.constants.jedis.CachedRedisKey;
import com.youshi.zebra.image.constants.AliyunOssConstants;
import com.youshi.zebra.image.dao.ImageDAO;
import com.youshi.zebra.image.exception.ImageUploadException;
import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.image.model.ImageModel.ImageKeys;
import com.youshi.zebra.image.utils.ImageUtils;

import magick.CompressionType;
import magick.ImageInfo;
import magick.MagickImage;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
@Service
public class ImageService extends AbstractService<Integer, ImageModel> {

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    private final AtomicInteger uploadImageFailureCount = new AtomicInteger();

    private OSSClient ossClient;
    
    @Autowired
    private ImageDAO imageDAO;
    
    private RequestContextCache<Integer, ImageModel> cache = new RequestContextCache<>();
    
    private JsonRedisCacheAccess<Integer, ImageModel> redisCache = new JsonRedisCacheAccess<>(
            JedisByZooKeeper.of("cache"), (id) -> CachedRedisKey.Image.of(id), ImageModel::new);
    
//    private SerializableRedisCacheAccess<Integer, ImageModel> redisCache = new SerializableRedisCacheAccess<>(
//		  CachedRedisKey.Image::ofBytes);
    
    private List<IMultiDataAccess<Integer, ImageModel>> multiDAList;

    @PostConstruct
    protected void init() {
        ossClient = new OSSClient(AliyunOssConstants.OSS_ENDPOINT, 
        		AliyunConstants.ACCESS_KEY_ID,AliyunConstants.ACCESS_KEY_SECRET);
        multiDAList = Arrays.asList(cache, redisCache, imageDAO);
        
        // TODO monitor utils测试
        MonitorUtils.watch("aliyunOss", "imageUploadFailure", uploadImageFailureCount);
    }
    
    @Override
    public Map<Integer, ImageModel> getByIds(Collection<Integer> ids) {
        return RetrieveIdUtils.get(ids, multiDAList());
    }
    
    public Integer createImage(Integer userId, byte[] imageData) throws ImageUploadException {
    	StopWatch watcher = PerfUtils.getWatcher("createImage");
        // 根据imageData先得适配出类型
        if (imageData == null) {
            throw new ImageUploadException();
        }
        MagickImage magickImage = null;
        try (InputStream inputStream = new ByteArrayInputStream(imageData)) {
            magickImage = new MagickImage(new ImageInfo(), imageData);

            Dimension dimension = magickImage.getDimension();
            // 备注，不处理旋转，上传上来的图都没有旋转，保持原样
            final String format = magickImage.getImageFormat().toLowerCase();

            Map<String, Object> data = new HashMap<>();
            data.put(ImageKeys.width.name(), (int) dimension.getWidth());
            data.put(ImageKeys.height.name(), (int) dimension.getHeight());
            data.put(ImageKeys.size.name(), imageData.length);
            if (magickImage.getCompression() == CompressionType.JPEGCompression) {
                int quality = magickImage.getQuality();
                if (quality > 0) {
                    data.put(ImageKeys.quality.name(), quality);
                }
            }
            data.put(ImageKeys.format.name(), format);
            if (userId != null) {
                data.put(ImageKeys.author.name(), userId);
            }

            CommonKey.inject(data);

            Integer imageId = imageDAO.insert(ObjectMapperUtils.toJSON(data),
                    System.currentTimeMillis()).intValue();
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(imageData.length);
            meta.setContentType(ImageUtils.getMimeType(format));

            String path = UuidUtils.getUuid(ImageModel.class, imageId) + "." + format;
            PutObjectResult putObject = ossClient.putObject(AliyunOssConstants.BUCKET_NAME, path,
                    inputStream, meta);

            logger.info("oss path={} result:{},{}", path, putObject,
                    ToStringBuilder.reflectionToString(putObject));
            watcher.stop();
            return imageId;
        } catch (Throwable e) {
            if (InProduction.get()) {
                logger.error("fail to upload image.{},{}", userId, imageData.length, e);
            } else {
                logger.error("fail to upload image.{},{},{}", userId, imageData.length, e);
            }
            uploadImageFailureCount.incrementAndGet();
            throw new ImageUploadException(e);
        } finally {
            if (magickImage != null) {
                magickImage.destroyImages();
            }
        }
    }
    
    public Integer createHeadImage(Integer userId, File imgFile) throws Exception {
    	StopWatch watcher = PerfUtils.getWatcher("createImage");
        
    	byte[] imageData = FileUtils.readFileToByteArray(imgFile);
    	String format = FilenameUtils.getExtension(imgFile.getAbsolutePath());
        try (InputStream inputStream = new ByteArrayInputStream(imageData)) {
            // insert DB
        	Map<String, Object> data = new HashMap<>();
            data.put(ImageKeys.width.name(), 200);	// 200
            data.put(ImageKeys.height.name(), 200);	// 200
            data.put(ImageKeys.size.name(), imageData.length);
            data.put(ImageKeys.format.name(), format);
            if (userId != null) {
                data.put(ImageKeys.author.name(), userId);
            }

            Integer imageId = imageDAO.insert(ObjectMapperUtils.toJSON(data),
                    System.currentTimeMillis()).intValue();
            
            // upload
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(imageData.length);
            meta.setContentType(ImageUtils.getMimeType(format));
            String path = UuidUtils.getUuid(ImageModel.class, imageId) + "." + format;
            PutObjectResult putObject = ossClient.putObject(AliyunOssConstants.BUCKET_NAME, path,
                    inputStream, meta);

            logger.info("oss path={} result:{},{}", path, putObject, ToStringBuilder.reflectionToString(putObject));
            watcher.stop();
            return imageId;
        }
    }

    public OSSClient getOSSClient() {
    	return ossClient;
    }
    
	@Override
	protected AbstractDAO<Integer, ImageModel> dao() {
		return imageDAO;
	}

	@Override
	public List<IMultiDataAccess<Integer, ImageModel>> multiDAList() {
		return multiDAList;
	}
}
