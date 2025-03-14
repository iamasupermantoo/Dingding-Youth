package com.youshi.zebra;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.dorado.framework.utils.DoradoBeanFactory;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.dorado.framework.utils.crypt.DES;
import com.youshi.zebra.core.ZebraSystemInitBean;
import com.youshi.zebra.course.dao.CourseMetaDAO;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.order.model.OrderModel;

/**
 * 
 * @author wangsch
 * @date 2017年5月10日
 */
public class ZebraUUIDTest {
	@Before
	public void init() {
		new ZebraSystemInitBean().init();
	}
	
	@Test
	public void Id() {
		System.out.println(UuidUtils.getId(CourseModel.class, "xD9ZA4RkQ24rWFBJGxv2Vw"));
	}
	
	
	@Test
	public void imageuuid() {
		
		DES des = new DES(new byte[] { 113, 39, 43, 38, 33, 55, -10, 72 });
		for(int i=948; i<=958; i++) {
			if(i == 950) {continue;}
			
			String uuid = des.encodeBase64URLSafeStringLong(i);
			System.out.println(uuid);
			
//			System.out.println("insert into z_image.image values("+i+", '{\"size\":19263,\"author\":1,\"width\":230,\"format\":\"jpg\",\"height\":296,\"quality\":85}', 0, 1497436494874);");
		}
		
		
	}
	
	@Test
	public void updatecourseMeta() {
		CourseMetaDAO dao = DoradoBeanFactory.getBean(CourseMetaDAO.class);
		List<CourseMetaModel> list = dao.getByCursor(null, 20);
		int i=300;
		for (CourseMetaModel courseMetaModel : list) {
			Map<String, Object> dataMap = courseMetaModel.resolvedData();
			dataMap.put("image_id", i++);
			dao.updateData(courseMetaModel.getId(), 
					DoradoMapperUtils.toJSON(dataMap), courseMetaModel.getData());
			
			System.out.println("id: " + courseMetaModel.getId() + " OK");
		}
		System.exit(0);
	}
	
	
	@Test
	public void lessonid() {
		System.out.println(UuidUtils.getId(LessonModel.class, "1HWTmf_fCm89RzEFBVF2BQ"));
	}
	
}
