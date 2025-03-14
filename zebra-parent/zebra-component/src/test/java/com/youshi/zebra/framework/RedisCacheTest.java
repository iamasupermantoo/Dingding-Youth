package com.youshi.zebra.framework;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.dorado.framework.cache.access.JsonRedisCacheAccess;
import com.dorado.framework.cache.codec.JsonRedisCacheCodec;
import com.dorado.framework.cache.codec.SerializableRedisCacheCodec;
import com.dorado.framework.crud.model.util.UuidUtils;
import com.dorado.framework.jedis.JedisByZooKeeper;
import com.dorado.framework.utils.DoradoBeanFactory;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.google.common.collect.ImmutableMap;
import com.youshi.zebra.core.constants.jedis.CachedRedisKey;
import com.youshi.zebra.image.constants.ImageConstants;
import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.image.model.ImageModel.ImageKeys;
import com.youshi.zebra.image.service.ImageService;

/**
 * @author wangsch
 * @date 2017年1月5日
 */
public class RedisCacheTest {
	
	@Test
	public void imageUUID() {
		
		String uuid = UuidUtils.getUuid(ImageConstants.IMAGE_DES, 1);
		System.out.println(uuid);
	}
	
	@Test
	public void cache() {
		ImageService bean = DoradoBeanFactory.getBean(ImageService.class);
		ImageModel image = bean.getById(1);
		System.out.println(image);
		
		image = bean.getById(1);
		System.out.println(image);
		
	}
	
	Map<Integer, ImageModel> dataMap = new HashMap<>();
	@Before
	public void init() {
		ImmutableMap<ImageKeys, Object> map = ImmutableMap.<ImageKeys, Object>builder()
				.put(ImageKeys.author, 1)
				.put(ImageKeys.width, 200)
				.put(ImageKeys.height, 200)
				.build();
	    String data = DoradoMapperUtils.toJSON(map);
	    long time = System.currentTimeMillis();
	    
	    for(int i=0; i<10000; i++) {
			dataMap.put(i, new ImageModel(i, data, time, 1));
	    }
	}
	
	@Test
	public void save1wImageJson() {
		JsonRedisCacheAccess<Integer, ImageModel> redisCache = new JsonRedisCacheAccess<>(
	            JedisByZooKeeper.of("cache"), (id) -> CachedRedisKey.Image.of(id), ImageModel::new);
	    
	    redisCache.set(dataMap);
	    
	    System.out.println("---------------------------------------------- JSON OK --------------------------------------------");
	    System.exit(0);
	}
	
	@Test
	public void serializableCodec() {
		SerializableRedisCacheCodec<ImageModel> codec = new SerializableRedisCacheCodec<>();
		ImmutableMap<ImageKeys, Object> map = ImmutableMap.<ImageKeys, Object>builder()
			.put(ImageKeys.author, 1)
			.put(ImageKeys.width, 200)
			.put(ImageKeys.height, 200)
			.build();
		ImageModel image = new ImageModel(1, DoradoMapperUtils.toJSON(map), System.currentTimeMillis(), 2);
		byte[] bytes = codec.encode(image);
		System.out.println(bytes);
		
		image = codec.decode(bytes);
		System.out.println(image);
	}

	@Test
	public void jsonCodec() {
		JsonRedisCacheCodec<ImageModel> codec = new JsonRedisCacheCodec<>(ImageModel.class);
		ImmutableMap<ImageKeys, Object> map = ImmutableMap.<ImageKeys, Object>builder()
			.put(ImageKeys.author, 1)
			.put(ImageKeys.width, 200)
			.put(ImageKeys.height, 200)
			.build();
		ImageModel image = new ImageModel(1, DoradoMapperUtils.toJSON(map), System.currentTimeMillis(), 2);
		String json = codec.encode(image);
		System.out.println(json);
		
		image = codec.decode(json);
		System.out.println(image);
	}
}
