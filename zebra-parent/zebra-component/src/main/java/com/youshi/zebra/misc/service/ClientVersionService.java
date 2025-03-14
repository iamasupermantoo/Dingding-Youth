package com.youshi.zebra.misc.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.internal.util.StringUtils;
import com.dorado.framework.constants.InProduction;
import com.dorado.framework.event.utils.PerfUtils;
import com.dorado.framework.localcache.AbstractCacheConfig;
import com.dorado.framework.localcache.LocalCache;
import com.dorado.framework.localcache.LocalCacheFactory;
import com.dorado.framework.localcache.NotifyAble;
import com.dorado.mvc.reqcontext.AppVer;
import com.dorado.mvc.reqcontext.Platform;
import com.ecyrd.speed4j.StopWatch;
import com.github.phantomthief.util.ObjectMapperUtils;
import com.youshi.zebra.core.constants.localcache.LocalCacheNotifyKey;
import com.youshi.zebra.misc.dao.ClientVersionDAO;
import com.youshi.zebra.misc.model.ClientVersion;

@Service
public class ClientVersionService {
	private static final Logger logger = LoggerFactory.getLogger(ClientVersionService.class);
	
	public class LatestVersion {
		private String url;
		
		private boolean force;
		
		private String title;
		
		private String desc;
		
		public LatestVersion(String url, boolean force, String title, String desc) {
			this.url = url;
			this.force = force;
			this.title = title;
			this.desc = desc;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public boolean isForce() {
			return force;
		}

		public void setForce(boolean force) {
			this.force = force;
		}
		
		public String getTitle() {
			return title;
		}
		
		public void setTitle(String title) {
			this.title = title;
		}
		
		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
	
	public LatestVersion checkVersion(Platform platform, AppVer appVer) {
//		ios: 1.1.5
//		android: 1.1.3
		
		if(platform == null || appVer == null) {
			return null;
		}
		if(platform != Platform.Android && platform != Platform.IOS) {
			return null;
		}
		if(StringUtils.isEmpty(appVer.toString())) {
			return null;
		}
		
		String url = platform == Platform.Android ? "http://a.app.qq.com/o/simple.jsp?pkgname=com.udan" 
				: "https://itunes.apple.com/us/app/you-dan-da-xue-xiao-you-dai/id1163974372?l=zh&ls=1&mt=8";
		String title = "建议您更新至最新版本";
		String desc = "1：解决语音播放被中断；2：评论增加到500字";
		String version = appVer.toString();
		LatestVersion v = null;
		if(platform == Platform.Android) {
			if("1.1.2".equals(version)) {
				v = new LatestVersion(url, false, title, desc);
			}
		} else if(platform == Platform.IOS) {
			if("1.1.4".equals(version)) {
				v = new LatestVersion(url, false, title, desc);
			}
		} else {
			// ignore
		}
		logger.info("Client version check. platform: {}, appVer: {}, update: {}", 
				platform, appVer, ObjectMapperUtils.toJSON(v));
		
		return v;
	}
	
	public LatestVersion checkVersionTest(Platform platform, AppVer appVer) {
		if(InProduction.get()) {
			return null;
		}
		
		int c = RandomUtils.nextInt(0, 10) % 3;
		if(c == 0) {
			return null;
		}
		
		String url = platform == Platform.Android ? "http://a.app.qq.com/o/simple.jsp?pkgname=com.udan" 
				: "https://itunes.apple.com/us/app/you-dan-da-xue-xiao-you-dai/id1163974372?l=zh&ls=1&mt=8";
		String desc = "1. 优化\n 2. 修复bug";
		String title = null;
		boolean force = false;
		if(c==1) {
			force = true;
			title = "请更新至最新版本";
		} else if(c==2) {
			force = false;
			title = "建议您更新至最新版本";
		}
		return new LatestVersion(url, force, title, desc);
	}
	
	// ------------------------------------------------------- 废弃的 -----------------------------------------------------------------
	/**
	 * 缓存一下子
	 */
	private static class ClientVersionCache {
		public static final ClientVersionCache EMPTY = new ClientVersionCache(Collections.emptyMap());
		
		Map<Platform, ClientVersion> latestVersions = 
				new ConcurrentHashMap<Platform, ClientVersion>();
		
		ClientVersionCache(Map<Platform, ClientVersion> latestVersions) {
			this.latestVersions = latestVersions;
		}
	}
	
	
	
	private LocalCache<ClientVersionCache> clientVersionCache = LocalCacheFactory.createCopyOnWriteStore(
			new AbstractCacheConfig<ClientVersionCache>() {
				@Override
				public NotifyAble getNotifyKey() {
					return LocalCacheNotifyKey.ClientVersion;
				}

				@Override
				public long getReloadPeriod() {
					return TimeUnit.MINUTES.toMillis(1);
				}

				@Override
				public ClientVersionCache buildCache() throws Exception {
					Map<Platform, ClientVersion> latestVersions = getLatestVersionFromDB();
					return new ClientVersionCache(latestVersions);
				}

				@Override
				public ClientVersionCache defaultCache() {
					return ClientVersionCache.EMPTY;
				}
			});
	
	
	@Autowired
	private ClientVersionDAO clientVersionDAO;
	
	/**
	 * 
	 * @param platform		{@link Platform}
	 * @param appVer		{@link AppVer}
	 * @return				{@link ClientVersion}
	 */
	public ClientVersion getUpdateVersion(Platform platform, AppVer appVer) {
		return clientVersionCache.getCacheContent().latestVersions.get(platform);
	}
	
	
	/**
	 * 
	 * 从db查最新版本
	 * 
	 * @return	Map，{@link ConcurrentHashMap}
	 */
	public Map<Platform, ClientVersion> getLatestVersionFromDB() {
		StopWatch watcher = PerfUtils.getWatcher("ClientVersionService.getNewVersion");
		
		Map<Platform, ClientVersion> map = new ConcurrentHashMap<Platform, ClientVersion>();
		ClientVersion latestAndroid = clientVersionDAO.getLatest(Platform.Android);
		if(latestAndroid != null) {
			map.put(Platform.Android, latestAndroid);
		}
		
		ClientVersion latestIOS = clientVersionDAO.getLatest(Platform.IOS);
		if(latestIOS != null) {
			map.put(Platform.IOS, latestIOS);
		}
		
		watcher.stop();
		return map;
	}
	
	public class VersionExp {
		private boolean not;
		private String op;
		private AppVer[] appVers;
		
		
		public boolean hit(AppVer appVer) {
			boolean hitExp = hitExp(appVer);
			return not ? !hitExp : hitExp;
		}
		
		private boolean hitExp(AppVer appVer) {
			switch (op) {
			case "gt":
				AppVer ver = appVers[0];
				int res = AppVer.comparator().compare(appVer, ver);
				if(res > 0) {
					return true;
				}
				
				break;
				
			case "in":
				for (int i=0; i<appVers.length; i++) {
					ver = appVers[i];
					res = AppVer.comparator().compare(appVer, ver);
					if(res == 0) {
						return true;
					}
				}
				
				break;
			case "range":
				AppVer from = appVers[0];
				AppVer to = appVers[1];
				
				int fromRes = AppVer.comparator().compare(appVer, from);
				int toRes = AppVer.comparator().compare(appVer, to);
				if(fromRes >= 0 && toRes <= 0) {
					return true;
				}
				
				break;

			default:
				break;
			}
			
			return false;
		}
	}
	
	public boolean needUpdate(Platform platform, AppVer appVer) {
		// 根据platform, 拿到安卓/IOS的更新配置
		List<String> iosExps = Arrays.asList(
				"in:1.0.3,1.0.4", 		// in
				"gt:1.3.2", 			// 大于
				"ge:1.3.2",				// 大于等于
				"range:1.3.2,1.4.0",	// 范围内, 闭区间
				"all",					// 所有
				"!in:1.3.4"				// 前面加!, 表示取反
				);
		
		String reqVer = appVer.toString();
		
		for (String iosExp : iosExps) {
			String[] parts = iosExp.split(":");
			String op = parts[0];
			String[] vers = parts[1].split(",");
			
			switch (op) {
			case "gt":
				AppVer ver = AppVer.of(vers[0]);
				int res = AppVer.comparator().compare(appVer, ver);
				if(res > 0) {
					return true;
				}
				
				break;
				
			case "in":
				for (String verStr : vers) {
					ver = AppVer.of(verStr);
					res = AppVer.comparator().compare(appVer, ver);
					if(res == 0) {
						return true;
					}
				}
				
				break;

			default:
				break;
			}
		}
		
		return false;
	}
}
