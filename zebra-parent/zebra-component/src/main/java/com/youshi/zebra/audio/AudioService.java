package com.youshi.zebra.audio;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.util.UuidUtils;
import com.dorado.framework.crud.service.AbstractService;
import com.dorado.framework.event.utils.PerfUtils;
import com.dorado.framework.tuple.TwoTuple;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.ecyrd.speed4j.StopWatch;
import com.youshi.zebra.aliyun.constants.AliyunConstants;
import com.youshi.zebra.audio.constants.AudioCompressParam;
import com.youshi.zebra.audio.constants.AudioFileType;
import com.youshi.zebra.audio.dao.AudioDAO;
import com.youshi.zebra.audio.model.AudioModel;
import com.youshi.zebra.audio.model.AudioModel.AudioKeys;
import com.youshi.zebra.audio.utils.AudioConvertUtils;
import com.youshi.zebra.image.constants.AliyunOssConstants;

/**
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
@Service
public class AudioService extends AbstractService<Integer, AudioModel>{
	private static final Logger logger = LoggerFactory.getLogger(AudioService.class);
	
	private OSSClient ossClient = new OSSClient(AliyunOssConstants.OSS_ENDPOINT, 
    		AliyunConstants.ACCESS_KEY_ID,AliyunConstants.ACCESS_KEY_SECRET);
	
	
	@Autowired
	private AudioDAO audioDAO;
	
	/**
	 * 将原始文件{@code fileData}字节流，转换为mp3格式（使用默认的压缩参数）后，上传到阿里云。
	 * 
	 * @param userId			上传者用户id
	 * @param fileData			原始文件字节数据
	 * @param fileExt			原始文件扩展名
	 * @return					音频id
	 */
	public Integer uploadMp3Audio(Integer userId, byte[] fileData, String fileExt) {
		Objects.requireNonNull(fileExt);
		
        StopWatch watcher = PerfUtils.getWatcher("AudioService.uploadMp3Audio");
        InputStream is = null;
        try {
        	// 源文件格式，转换为mp3格式
        	AudioFileType sourceType = AudioFileType.findByFileExt(fileExt);
        	TwoTuple<byte[], Long> afterCompress = AudioConvertUtils.convert(fileData, sourceType, 
        			AudioFileType.MP3, AudioCompressParam.Mp3Abr72);
        	
        	byte[] mp3FileData = afterCompress.first;
        	long mp3Seconds = afterCompress.second;
        	if(mp3FileData == null || mp3FileData.length == 0) {
        		return null;
        	}
        	
        	// 写数据库
        	Map<AudioKeys, Object> dataMap = new HashMap<>();
        	dataMap.put(AudioKeys.author, userId);
        	dataMap.put(AudioKeys.format, "mp3");
        	dataMap.put(AudioKeys.length, mp3Seconds);
        	Number nid = audioDAO.insert(DoradoMapperUtils.toJSON(dataMap), System.currentTimeMillis());
        	
        	// 上传到阿里云
        	Integer id = nid.intValue();
			String key = UuidUtils.getUuid(AudioModel.class, id) + ".mp3";
	        ObjectMetadata fileMeta = new ObjectMetadata();
	        fileMeta.setContentLength(mp3FileData.length);
	        fileMeta.setContentType("audio/mp3");
        
        	is = new ByteArrayInputStream(mp3FileData);
        	PutObjectResult result = ossClient.putObject(
        			AliyunOssConstants.AUDIO_BUCKET_NAME, key, is, fileMeta);
        	logger.info("Audio upload succ, aliyun result: {}, key: {}", result, key);
        	watcher.stop();
        	
        	return id;
        } finally {
        	IOUtils.closeQuietly(is);
        }
	}
	
	@Override
	protected AbstractDAO<Integer, AudioModel> dao() {
		return audioDAO;
	}
}
