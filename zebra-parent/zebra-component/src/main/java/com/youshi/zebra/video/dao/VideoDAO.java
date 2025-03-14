package com.youshi.zebra.video.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.video.model.VideoModel;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
@Repository
public class VideoDAO extends AbstractDAO<Integer, VideoModel>{
	private static final String TABLE_NAME = "video";
	
	private static final RowMapper<VideoModel> MAPPER = (rs, num)->{
		return new VideoModel(rs.getInt("id"), rs.getString("data"), 
				rs.getLong("create_time"), rs.getInt("status"));
	};
	
	public VideoDAO() {
		super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
	}
	
//	public static void main(String[] args) throws Exception {
//		new ZebraSystemInitBean().init();
//		
//		Collection<File> files = FileUtils.listFiles(new File("/Users/wangsch/Work/zebra/30 Images for DDlad"), new String[]{"JPG"}, false);
//		int i = 900;
//		for (File file : files) {
//			String filename = UuidUtils.getUuid(ImageModel.class, i) + ".jpg";
//			FileUtils.copyFile(file, new File(file.getParent() + "/000/", filename));
//			System.out.println("insert into z_image.image values("+i+", '{\"size\":856485,\"author\":110092,\"width\":1400,\"format\":\"jpg\",\"height\":1000}', 0, 1487732664847);");
//			i++;
//		}
//		
//		System.exit(0);
//	}
	
//	public static void main(String[] args) {
//		int i=0;
//		int id = 200;
//		int img = 900;
//		while(i++<30) {
//			System.out.println("insert into media values("+id++ +", 0, '{\"name\":\"图片\",\"image_id\":"+img++ +"}', 0, 14000000000);");
//		}
//		
//	}
}
