package com.youshi.zebra.core.constants;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.dorado.framework.crud.model.HasData;
import com.dorado.framework.utils.EntryUtils;
import com.dorado.mvc.reqcontext.AppVer;
import com.dorado.mvc.reqcontext.Platform;
import com.dorado.mvc.reqcontext.WebRequestContext;

/**
 * 一些公共的key，这些key是从{@link WebRequestContext}中获取的。<br />
 * 必要的话（重要的写操作接口），可以存到DB中便于追踪，如：创建用户时注入这些key，
 * 可以知道用户注册时的手机类型、IP地址等。
 * 
 * @author wangsch
 * @date 2016-09-12
 */
public enum CommonKey {
	/** {@link Platform} */
    platform,
    
    /** {@link AppVer} */
    appVer,
    
    /** IP地址 */
    ip,
    
    /** User-Agent头 */
    ua,
    
    /** 位置，经纬度 */
    location,
    
    // --------设备标识
    /** 设备id */
    deviceId,
    
    /** 设备Mac地址 */
    deviceMac,
    
    /** IOS IDFA标识 */
    idfa,
    // END
    ;
    
    /**
     * 往Map里边注入{@link CommonKey}中的的值
     * 
     * @param data		Map对象
     */
    public static final void inject(Map<String, Object> data) {
    	Platform platform = WebRequestContext.getAppPlatform();
    	if (platform != null) {
    		data.put(CommonKey.platform.name(), platform);
    	}
        AppVer appVer = WebRequestContext.getAppVer();
        if (appVer != null) {
        	data.put(CommonKey.appVer.name(), appVer.toString());
        }
        long ip = WebRequestContext.getCurrentIpInLong();
        if (ip > 0) {
            data.put(CommonKey.ip.name(), ip);
        }
        String ua = WebRequestContext.getUserAgent();
        if (StringUtils.isNotEmpty(ua)) {
            data.put(CommonKey.ua.name(), ua);
        }
        Entry<Double, Double> location = WebRequestContext.getAccessLocation();
        if (location != null) {
            data.put(CommonKey.location.name(), location.getKey() + "," + location.getValue());
        }
        String deviceMac = WebRequestContext.getAndroidDeviceMac();
        if (StringUtils.isNotEmpty(deviceMac)) {
            data.put(CommonKey.deviceMac.name(), deviceMac);
        }
        String deviceId = WebRequestContext.getAndroidDeviceId();
        if (StringUtils.isNotEmpty(deviceId)) {
            data.put(CommonKey.deviceId.name(), deviceId);
        }
        String idfa = WebRequestContext.getIdfa();
        if (StringUtils.isNotEmpty(idfa)) {
            data.put(CommonKey.idfa.name(), idfa);
        }
    }
    
    /**
     * 从{@link HasData}对象中，解析出{@link CommonKey}的每个值
     * 
     * @param object	{@link HasData}对象
     * @return	{@link RequestInfo}
     */
    public static final RequestInfo getRequestInfo(HasData hasData) {
        RequestInfo requestInfo = new RequestInfo();
        if (hasData != null) {
            Map<String, Object> resolvedData = hasData.resolvedData();
            AppVer appVer = AppVer.of((String) resolvedData.get(CommonKey.appVer.name()));
            if (appVer != null) {
                requestInfo.setAppVer(appVer);
            }
            Number p = (Number) resolvedData.get(CommonKey.platform.name());
            if (p != null) {
                Platform platform = Platform.fromValue(p.intValue());
                if (platform != null) {
                    requestInfo.setPlatform(platform);
                }
            }
            Number ip = (Number) resolvedData.get(CommonKey.ip.name());
            if (ip != null) {
                requestInfo.setIp(ip.longValue());
            }
            String ua = (String) resolvedData.get(CommonKey.ua.name());
            if (ua != null) {
                requestInfo.setUa(ua);
            }
            String rawLocation = (String) resolvedData.get(CommonKey.location.name());
            if (rawLocation != null) {
                String[] split = rawLocation.split(",");
                if (split.length == 2) {
                    try {
                        Double latitude = Double.parseDouble(split[0]);
                        Double longitude = Double.parseDouble(split[1]);
                        requestInfo.setLocation(EntryUtils.newEntry(latitude, longitude));
                    } catch (Throwable e) {
                        // ignore
                    }
                }
            }

            String deviceMac = (String) resolvedData.get(CommonKey.deviceMac.name());
            if (deviceMac != null) {
                requestInfo.setDeviceMac(deviceMac);
            }
            String deviceId = (String) resolvedData.get(CommonKey.deviceId.name());
            if (deviceId != null) {
                requestInfo.setDeviceId(deviceId);
            }
            String idfa = (String) resolvedData.get(CommonKey.idfa.name());
            if (idfa != null) {
                requestInfo.setIdfa(idfa);
            }
        }
        return requestInfo;
    }
    
    /**
     * 从{@link HasData}对象中，解析出经纬度
     * 
     * @param object	{@link HasData}对象
     * @return	{@link Entry}，key：经度，value：纬度
     */
    public static final <T extends HasData> Entry<Double, Double> getLocation(T object) {
        if (object == null) {
            throw new NullPointerException();
        }
        String rawLocationString = (String) object.resolvedData().get(CommonKey.location.name());
        if (rawLocationString == null) {
            return null;
        }
        String[] split = StringUtils.split(rawLocationString, ',');
        return EntryUtils.newEntry(NumberUtils.toDouble(split[0]), NumberUtils.toDouble(split[1]));
    }
    
    /**
     * 一次http请求，包含的信息
     * 
     * @author wangsch
     * @date 2016年11月04日
     */
    public static final class RequestInfo {

        private Platform platform;

        private AppVer appVer;

        private Long ip;

        private String ua;
        
        private Entry<Double, Double> location;

        private String deviceId;	// 设备标识

        private String deviceMac;	// 设备MAC地址

        private String idfa;			// IOS设备标识

        public String getIdfa() {
            return idfa;
        }

        public void setIdfa(String idfa) {
            this.idfa = idfa;
        }

        public String getUa() {
            return ua;
        }

        public void setUa(String ua) {
            this.ua = ua;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getDeviceMac() {
            return deviceMac;
        }

        public void setDeviceMac(String deviceMac) {
            this.deviceMac = deviceMac;
        }

        public Entry<Double, Double> getLocation() {
            return location;
        }

        public void setLocation(Entry<Double, Double> location) {
            this.location = location;
        }

        public Platform getPlatform() {
            return platform;
        }

        public void setPlatform(Platform platform) {
            this.platform = platform;
        }

        public AppVer getAppVer() {
            return appVer;
        }

        public void setAppVer(AppVer appVer) {
            this.appVer = appVer;
        }

        public Long getIp() {
            return ip;
        }

        public void setIp(Long ip) {
            this.ip = ip;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (platform != null) {
                sb.append('[').append(platform).append(']');
            }
            if (appVer != null) {
                sb.append('[').append(appVer).append(']');
            }
            if (ip != null) {
                sb.append('[').append(WebRequestContext.longToIpv4(ip)).append(']');
            }
            if (ua != null) {
                sb.append('[').append(ua).append(']');
            }
            if (location != null) {
                sb.append('[').append(location.toString()).append(']');
            }
            if (deviceMac != null) {
                sb.append('[').append(deviceMac).append(']');
            }
            if (deviceId != null) {
                sb.append('[').append(deviceId).append(']');
            }
            if (idfa != null) {
                sb.append('[').append(idfa).append(']');
            }
            return sb.toString();
        }

    }
}
