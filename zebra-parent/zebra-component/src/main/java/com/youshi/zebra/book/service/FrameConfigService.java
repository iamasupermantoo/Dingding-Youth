package com.youshi.zebra.book.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.HasData;
import com.dorado.framework.crud.service.AbstractService;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.youshi.zebra.book.constants.FrameConfigStatus;
import com.youshi.zebra.book.dao.FrameConfigDAO;
import com.youshi.zebra.book.model.FrameConfigModel;
import com.youshi.zebra.book.model.FrameItem;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.exception.common.EntityNotNormalException;
import com.youshi.zebra.media.constants.MediaStatus;
import com.youshi.zebra.media.model.MediaModel;
import com.youshi.zebra.media.service.MediaService;

/**
 * 
 * 
 * 这个方法的每个写操作，在前端展示页面，需要加强烈的提示信息：将同步更新所有已排课程，是否确认修改？
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
@Service
public class FrameConfigService extends AbstractService<Integer, FrameConfigModel>{
	/**
	 * 
	 */
	private static final int MAX_FRAMES_SIZE = 10;

	private static final Logger logger = LoggerFactory.getLogger(FrameConfigService.class);
	
	@Autowired
	private FrameConfigDAO frameConfigDAO;
	
	@Autowired
	private MediaService mediaService;
	
	public List<FrameItem> getFrames(Integer chapterId, boolean flat) {
		FrameConfigModel config = getById(chapterId);
		if(config == null) {
			return Collections.emptyList();
		}
		
		List<FrameItem> frames = parseFrameContent(config.getFrameContent());
		if(flat) {
			frames = flatFrame(frames);
		}
		
		
		int parentIdx = 0;
		int childIdx = 0;
		for (FrameItem frame : frames) {
			boolean isChild = frame.getIsChild();
			if(isChild) {
				frame.setIdx(childIdx++);
			} else {
				childIdx = 0;
				frame.setIdx(parentIdx++);
			}
		}
		
		
		return frames;
	}
	
	public List<FrameItem> getFrames(Integer chapterId) {
		return getFrames(chapterId, true);
	}
	
	

	public void saveIdx(Integer chapterId, List<String> rawIdxes) {
		FrameConfigModel config = getById(chapterId);
		if(config == null) {
			return;
		}
		List<FrameItem> frames = parseFrameContent(config.getFrameContent());
		if(CollectionUtils.isEmpty(frames)) {
			return ;
		}
		
		List<FrameItem> newFrames = new ArrayList<>(frames);
		for (String rawIdx : rawIdxes) {
			String[] parts = rawIdx.split("-");
			int oldIdx = Integer.parseInt(parts[0]);
			int newIdx = Integer.parseInt(parts[1]);
			
			newFrames.set(newIdx, frames.get(oldIdx));
		}
		
		String frameContent = DoradoMapperUtils.toJSON(newFrames);
		int c = frameConfigDAO.insertOrUpdate(chapterId, frameContent, 
				HasData.EMPTY_DATA, FrameConfigStatus.Normal, System.currentTimeMillis());
		DAOUtils.checkAffectRows(c, DAOUtils.INSERT_DUPLICATE_ROWS);
		
		logger.info("Save idx succ. cid: {}, mediaId: {}", chapterId);
	}

	public void addFrame(Integer chapterId, Integer mediaId, Integer parentIdx) {
		FrameConfigModel config = getById(chapterId);
		String oldFrameContent = null;
		if(config != null) {
			oldFrameContent = config.getFrameContent();
		}
		MediaModel media = mediaService.getById(mediaId);
		if(media.getStatus() != MediaStatus.Normal.getValue()) {
			throw new EntityNotNormalException();
		}
		
		String frameContent = doAdd(mediaId, parentIdx,
				oldFrameContent, media.getType());
		
		int c = frameConfigDAO.insertOrUpdate(chapterId, frameContent, 
				HasData.EMPTY_DATA, FrameConfigStatus.Normal, System.currentTimeMillis());
		DAOUtils.checkAffectRows(c, DAOUtils.INSERT_DUPLICATE_ROWS);
		
		logger.info("Add frame succ. cid: {}, mediaId: {}",
				chapterId, mediaId);
	}
	
	public void updateFrame(Integer chapterId, Integer mediaId, Integer parentIdx, Integer childIdx) {
		FrameConfigModel config = getById(chapterId);
		if(config == null) {
			throw new EntityNotNormalException();
		}
		
		MediaModel media = mediaService.getById(mediaId);
		if(media.getStatus() != MediaStatus.Normal.getValue()) {
			throw new EntityNotNormalException();
		}
		
		String frameContent = doUpdate(mediaId, parentIdx, childIdx, config.getFrameContent(), media.getType());
		int c = frameConfigDAO.insertOrUpdate(chapterId, frameContent, 
				HasData.EMPTY_DATA, FrameConfigStatus.Normal, System.currentTimeMillis());
		DAOUtils.checkAffectRows(c, DAOUtils.INSERT_DUPLICATE_ROWS);
		
		logger.info("Update frame succ. cid: {}, mediaId: {}, parentIdx: {}, childIdx: {}",
				chapterId, mediaId, parentIdx, childIdx);
		
	}

	public void removeFrame(Integer chapterId, Integer parentIdx, Integer childIdx) {
		FrameConfigModel config = getById(chapterId);
		if(config == null) {
			throw new EntityNotNormalException();
		}
		
		String frameContent = doRemove(parentIdx, childIdx, config.getFrameContent());
		int c = frameConfigDAO.insertOrUpdate(chapterId, frameContent, 
				HasData.EMPTY_DATA, FrameConfigStatus.Normal, System.currentTimeMillis());
		DAOUtils.checkAffectRows(c, DAOUtils.INSERT_DUPLICATE_ROWS);
		
		logger.info("Remove frame succ. cid: {}, parentIdx: {}, childIdx: {}",
				chapterId, parentIdx, childIdx);
	}
 
	@Override
	protected AbstractDAO<Integer, FrameConfigModel> dao() {
		return frameConfigDAO;
	}
	
	public static List<FrameItem> parseFrameContent(String content) {
		List<FrameItem> frames = DoradoMapperUtils.fromJSON(content, ArrayList.class, FrameItem.class);
		
		List<FrameItem> result = new ArrayList<>(MAX_FRAMES_SIZE);
		result.addAll(frames);
		
		return frames;
	}

	// ---------------------------------------- private method ---------------------------------------
	
	/**
	 * @param frames
	 * @return
	 */
	private List<FrameItem> flatFrame(List<FrameItem> frames) {
		List<FrameItem> result = new ArrayList<>(frames.size() * 2);
		for (FrameItem frame : frames) {
			result.add(frame);
			if(CollectionUtils.isNotEmpty(frame.getChildren())) {
				List<FrameItem> children = frame.getChildren();
				for(FrameItem childFrame : children) {
					childFrame.setIsChild(true);
					result.add(childFrame);
				}
			}
		}
		
		return result;
	}
	
	@Deprecated
	private String doAdd(Integer mediaId, Integer parentIdx, Integer childIdx, String frameContent, int type) {
		List<FrameItem> frames = parseFrameContent(frameContent);
		if(childIdx != null) {
			FrameItem parentFrame = frames.get(parentIdx);
			List<FrameItem> children = parentFrame.getChildren();
			if(children == null) {
				children = new ArrayList<>(1);
			}
			children.add(childIdx, new FrameItem(type, mediaId));
			parentFrame.setChildren(children);
		} else {
			frames.add(parentIdx, new FrameItem(type, mediaId));
		}
		
		return DoradoMapperUtils.toJSON(frames);
	}
	
	private String doAdd(Integer mediaId, Integer parentIdx, String frameContent, int type) {
		List<FrameItem> frames = parseFrameContent(frameContent);
		if(parentIdx != null) {
			FrameItem parentFrame = frames.get(parentIdx);
			List<FrameItem> children = parentFrame.getChildren();
			if(children == null) {
				children = new ArrayList<>(1);
			}
			children.add(new FrameItem(type, mediaId));
			parentFrame.setChildren(children);
		} else {
			parentIdx = frames.size();
			frames.add(new FrameItem(type, mediaId));
		}
		
		return DoradoMapperUtils.toJSON(frames);
	}
	
	private String doUpdate(Integer mediaId, Integer parentIdx, Integer childIdx, String frameContent, int type) {
		List<FrameItem> frames = parseFrameContent(frameContent);
		if(childIdx != null) {
			FrameItem parentFrame = frames.get(parentIdx);
			List<FrameItem> children = parentFrame.getChildren();
			if(children != null) {
				children.set(childIdx, new FrameItem(type, mediaId));
			}
		} else {
			FrameItem parentFrame = frames.get(parentIdx);
			parentFrame.setType(type);
			parentFrame.setMediaId(mediaId);
		}
		
		return DoradoMapperUtils.toJSON(frames);
	}
	
	private String doRemove(Integer parentIdx, Integer childIdx, String frameContent) {
		List<FrameItem> frames = parseFrameContent(frameContent);
		if(childIdx != null) {
			FrameItem parentFrame = frames.get(parentIdx);
			List<FrameItem> children = parentFrame.getChildren();
			if(children != null) {
				children.remove(childIdx.intValue());
			}
		} else {
			frames.remove(parentIdx.intValue());
		}
		return DoradoMapperUtils.toJSON(frames);
	}
	
}
