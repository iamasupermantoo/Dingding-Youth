package com.youshi.zebra.media.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.crud.service.AbstractService;
import com.dorado.framework.model.PageView;
import com.dorado.framework.utils.DoradoBeanFactory;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.youshi.zebra.audio.AudioService;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.exception.base.DoradoRuntimeException;
import com.youshi.zebra.image.service.ImageService;
import com.youshi.zebra.media.constants.MediaStatus;
import com.youshi.zebra.media.constants.MediaType;
import com.youshi.zebra.media.dao.MediaDAO;
import com.youshi.zebra.media.model.MediaModel;
import com.youshi.zebra.media.model.MediaModel.MediaKeys;
import com.youshi.zebra.video.service.VideoService;

/**
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
@Service
public class MediaService extends AbstractService<Integer, MediaModel>{
	private static final Logger logger = LoggerFactory.getLogger(MediaService.class);
	
	@Autowired
	private MediaDAO mediaDAO;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private AudioService audioService;
	
	@Autowired
	private VideoService videoService;
	
	@Override
	protected AbstractDAO<Integer, MediaModel> dao() {
		return mediaDAO;
	}

	public PageView<MediaModel, HasUuid<Integer>> getMedias(MediaType type, 
			Integer cursor, Integer limit) {
		WhereClause params = WhereClause.create();
		if(type != null) {
			params.and().eq(MediaKeys.type, type.getValue());
		}
		
		PageView<MediaModel, HasUuid<Integer>> page = getByCursor(cursor, limit, params);
		return page;
	}

	public Integer createMedia(Integer loginAdminId, String name, String desc, MultipartFile mediaFile) {
		MediaType type = decideMediaType(mediaFile.getOriginalFilename());
		
		Integer uploadedId = doMediaUpload(loginAdminId, type, mediaFile);
		Map<String, Object> dataMap = mediaDataMap(type, name, desc, uploadedId);
		
		String data = DoradoMapperUtils.toJSON(dataMap);
		int id = mediaDAO.insert(type, data, System.currentTimeMillis(), MediaStatus.Normal);
		logger.info("Create media succ. mediaId: {}, name: {}, desc:{} mediaType: {}", 
				id, type, name, desc);
		
		return id;
	}
	
	public Integer createMedia(Integer loginAdminId, String name, String desc, File mediaFile) {
		MediaType type = decideMediaType(mediaFile.getName());
		
		Integer uploadedId = doMediaUpload(loginAdminId, type, mediaFile);
		Map<String, Object> dataMap = mediaDataMap(type, name, desc, uploadedId);
		
		String data = DoradoMapperUtils.toJSON(dataMap);
		int id = mediaDAO.insert(type, data, System.currentTimeMillis(), MediaStatus.Normal);
		logger.info("Create media succ. mediaId: {}, name: {}, desc:{} mediaType: {}", 
				id, type, name, desc);
		
		return id;
	}
	
	public void updateMedia(Integer loginAdminId, Integer mediaId, String name, String desc, 
			MediaType type, MultipartFile mediaFile) {
		Integer uploadedId = doMediaUpload(loginAdminId, type, mediaFile);
		
		upload(mediaId, name, desc, type, uploadedId);
	}
	
	public void updateMedia(Integer loginAdminId, Integer mediaId, String name, String desc, 
			MediaType type, File mediaFile) {
		Integer uploadedId = doMediaUpload(loginAdminId, type, mediaFile);
		
		upload(mediaId, name, desc, type, uploadedId);
	}

	public void deleteMedia(Integer mediaId) {
		checkAndGet(mediaId, MediaStatus.Normal);
		
		int c = mediaDAO.delete(mediaId);
		DAOUtils.checkAffectRows(c);
		
	}
	
	// ---------------------------------- private methods ----------------------------------
	private Map<String, Object> mediaDataMap(MediaType type, String name, String desc, Integer uploadedId) {
		Objects.requireNonNull(uploadedId);
		
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(MediaKeys.name.name(), name);
		dataMap.put(MediaKeys.desc.name(), desc);
		
		switch (type) {
		case Image:
			dataMap.put(MediaKeys.image_id.name(), uploadedId);
			break;
		case Audio:
			dataMap.put(MediaKeys.audio_id.name(), uploadedId);
			
			break;
		case Video:
			dataMap.put(MediaKeys.video_id.name(), uploadedId);
			
			break;

		default:
			throw new IllegalArgumentException("Unknow media type: " + type);
		}
		return dataMap;
	}
	
	private void upload(Integer mediaId, String name, String desc, MediaType type, Integer uploadedId) {
		MediaModel media = getById(mediaId, MediaStatus.Normal);
		
		Map<String, Object> dataMap = mediaDataMap(type, name, desc, uploadedId);
		
		Map<String, Object> resolvedData = media.resolvedData();
		resolvedData.putAll(dataMap);
		
		String newData = DoradoMapperUtils.toJSON(resolvedData);
		int id = mediaDAO.updateData(mediaId, newData, media.getData());
		
		logger.info("Create UPDATE succ. mediaId: {}, name: {}, desc:{} mediaType: {}", 
				id, type, name, desc);
	}

	private MediaType decideMediaType(String filename) {
		MediaType type = null;
		String ext = FilenameUtils.getExtension(filename).toLowerCase();
		switch (ext) {
			case "jpg":
			case "jpeg":
			case "png":
				type = MediaType.Image;
				
				break;
			case "mp3":
				type = MediaType.Audio;
				break;
	
			case "mp4":
				type = MediaType.Video;
				break;
				
			default:
				throw new IllegalArgumentException();
		}
		
		return type;
	}

	public Integer  doMediaUpload(Integer loginAdminId, MediaType type, MultipartFile mediaFile) {
		String extension = FilenameUtils.getExtension(mediaFile.getOriginalFilename());
		try {
			Integer uploadId = doMediaUpload(loginAdminId, type, extension, mediaFile.getBytes());
			return uploadId;
		} catch(Exception e) {
			logger.error("Media UPLOAD FAIL. loginAdminId: {}, mediaType: {}", loginAdminId, type);
			throw new DoradoRuntimeException(e);
		}
	}
	
	public Integer  doMediaUpload(Integer loginAdminId, MediaType type, File mediaFile) {
		String extension = FilenameUtils.getExtension(mediaFile.getName());
		try {
			Integer uploadId = doMediaUpload(loginAdminId, type, extension, FileUtils.readFileToByteArray(mediaFile));
			return uploadId;
		} catch(Exception e) {
			logger.error("Media UPLOAD FAIL. loginAdminId: {}, mediaType: {}", loginAdminId, type);
			throw new DoradoRuntimeException(e);
		}
	}
	
	public Integer  doMediaUpload(Integer loginAdminId, MediaType type, String extension, byte[] data) {
		Integer uploadId = null;
		
		switch (type) {
		case Image:
			uploadId = imageService.createImage(loginAdminId, data);
			
//			File file = new File("/tmp/" + UUID.randomUUID().toString() + ".jpg");
//			try {
//				FileUtils.writeByteArrayToFile(file, data);
//				uploadId = imageService.createHeadImage(loginAdminId, file);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			
			
			break;
		case Audio:
			uploadId = audioService.uploadMp3Audio(loginAdminId, data, extension);
			
			break;
		case Video:
			uploadId = videoService.uploadMp4Video(loginAdminId, data, extension);
			break;

		default:
			throw new UnsupportedOperationException("Unsupported media type: " + type);
		}
		
		return uploadId;
	}
	
	public static void main(String[] args) {
		MediaDAO bean = DoradoBeanFactory.getBean(MediaDAO.class);
		List<MediaModel> list = bean.getByCursor(null, 20);
		for (MediaModel media : list) {
//			bean.updateMedia(1, media.getId(), "名字-"+media.getId(), "描述-" + media.getId(), media.gettype, mediaFile);
			
			Map<String, Object> dataMap = media.resolvedData();
			dataMap.put("name", "名字-"+media.getId());
			int c = bean.updateData(media.getId(), DoradoMapperUtils.toJSON(dataMap), media.getData());
			DAOUtils.checkAffectRows(c);
		}
	}
}
