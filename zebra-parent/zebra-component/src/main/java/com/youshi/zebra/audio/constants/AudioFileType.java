package com.youshi.zebra.audio.constants;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年2月10日
 */
public enum AudioFileType {
    AAC("aac"),
    AMR("amr"),
    SPX("spx"),
    MP3("mp3"),
    // QUES raw?
    RAW_MP3("mp3"),
//    SPX_XUNFEI("spx_xunfei")
    ;

    private final String fileExt;

    private AudioFileType(String fileExt) {
        this.fileExt = fileExt;
    }

    public String getFileExt() {
        return fileExt;
    }

    public static AudioFileType findByName(String name) {
        for (AudioFileType type : AudioFileType.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return AAC;
    }

    public static AudioFileType findByFileExt(String ext) {
        for (AudioFileType type : AudioFileType.values()) {
            if (type.getFileExt().equalsIgnoreCase(ext)) {
                return type;
            }
        }
        return AAC;
    }
}
