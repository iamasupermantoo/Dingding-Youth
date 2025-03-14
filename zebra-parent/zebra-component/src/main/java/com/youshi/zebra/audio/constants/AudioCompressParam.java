package com.youshi.zebra.audio.constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
public enum AudioCompressParam {
//    SpeexL16r16K(Arrays.asList("-w", "--quality", "8", "--bitrate", "16000")),
    Mp3Abr72(Arrays.asList("--abr", "72k")),
    Mp3Abr96(Arrays.asList("--abr", "96k")),
    ;

    private static Map<String, AudioCompressParam> namesMap = new HashMap<String, AudioCompressParam>();
    static {
        for (AudioCompressParam p : values()) {
            namesMap.put(p.name(), p);
        }
    }

    private final List<String> params;

    private AudioCompressParam(List<String> params) {
        this.params = params;
    }

    public List<String> getParams() {
        return params;
    }

    public static AudioCompressParam fromName(String name) {
        return namesMap.get(name);
    }
}
