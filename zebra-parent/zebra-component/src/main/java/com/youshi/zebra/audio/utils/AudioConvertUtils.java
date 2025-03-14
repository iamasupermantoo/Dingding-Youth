package com.youshi.zebra.audio.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dorado.framework.event.utils.PerfUtils;
import com.dorado.framework.tuple.TwoTuple;
import com.ecyrd.speed4j.StopWatch;
import com.google.common.base.Joiner;
import com.youshi.zebra.audio.constants.AudioCompressParam;
import com.youshi.zebra.audio.constants.AudioConstants;
import com.youshi.zebra.audio.constants.AudioFileType;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年2月10日
 */
public class AudioConvertUtils {

    private static Logger logger = LoggerFactory.getLogger(AudioConvertUtils.class);
    
    private static File tempDir;
    
    static {
    	// Linux /dev/shm
        String path = "/dev/shm/";
        tempDir = new File(path);
        
        // OSX上测试用
        if (!tempDir.isDirectory()) {
            tempDir = new File("/tmp/shm");
            if(!tempDir.exists()) {
            	tempDir.mkdirs();
            }
        }
    }

    public static TwoTuple<byte[], Long> convert(byte[] data, AudioFileType sourceType,
            AudioFileType destType, AudioCompressParam param) {
        StopWatch stopWatch = PerfUtils.getWatcher("AudioConvertUtils.convert");

        File tmpSourceFile = null;
        File tmpWavFile = null;
        File tmpDestFile = null;
        FileOutputStream tmpSourceFos = null;
        FileInputStream resultFis = null;
        try {
            tmpSourceFile = makeTempFile("ac_", "." + sourceType.getFileExt());
            tmpWavFile = makeTempFile("ac_", ".wav");
            tmpDestFile = makeTempFile("ac_", "." + destType.getFileExt());

            // 拷贝源文件到临时目录
            tmpSourceFos = new FileOutputStream(tmpSourceFile.getAbsolutePath());
            IOUtils.copy(new ByteArrayInputStream(data), tmpSourceFos);
            tmpSourceFos.flush();

            // 源文件 -> wav
            ProcessBuilder pb = new ProcessBuilder(AudioConstants.FFMPEG_BIN_PATH,
                    "-y", "-i", tmpSourceFile.getAbsolutePath(),
                    tmpWavFile.getAbsolutePath());
            pb.environment().put("LD_LIBRARY_PATH", "/usr/local/lib/");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            String result = IOUtils.toString(p.getInputStream());
            p.waitFor();
            logger.debug("Audio compress(1/2): command: {} result: {}", pb.command(), result);
            
            // wav -> 目标文件
            List<String> cmd = new ArrayList<>();
            if (destType == AudioFileType.SPX) {
                cmd.add(AudioConstants.SPEEXENC_BIN_PATH);
            } else {
                cmd.add(AudioConstants.LAME_BIN_PATH);
            }
            cmd.addAll(param.getParams());
            cmd.add(tmpWavFile.getAbsolutePath());
            cmd.add(tmpDestFile.getAbsolutePath());
            pb = new ProcessBuilder(cmd);
            pb.environment().put("LD_LIBRARY_PATH", "/usr/local/lib/");
            pb.redirectErrorStream(true);
            p = pb.start();
            result = IOUtils.toString(p.getInputStream());
            p.waitFor();
            logger.debug("Audio compress(2/2): command: {} result: {}", pb.command(), result);
            
            logger.debug("Source len: " + AudioConvertUtils.getAudioLengthV2(tmpSourceFile));
            logger.debug("Dest len: " + AudioConvertUtils.getAudioLengthV2(tmpDestFile));
            
            // 返回结果
            long length = AudioConvertUtils.getAudioLengthV2(tmpDestFile);
            resultFis = new FileInputStream(tmpDestFile);
            return new TwoTuple<byte[], Long>(IOUtils.toByteArray(resultFis), length);
        } catch (Throwable e) {
            logger.warn("压缩音频失败", e);
        } finally {
            IOUtils.closeQuietly(tmpSourceFos);
            IOUtils.closeQuietly(resultFis);
            FileUtils.deleteQuietly(tmpSourceFile);
            FileUtils.deleteQuietly(tmpWavFile);
            FileUtils.deleteQuietly(tmpDestFile);

            stopWatch.stop();
        }
        return null;
    }
    
    /**
     * 
     * @param audioFile
     * @return
     * @throws UnsupportedAudioFileException
     * @throws IOException
     * 
     * @deprecated 总是报错
     */
    @Deprecated
    public static long getAudioLength(File audioFile) throws UnsupportedAudioFileException, IOException {
        AudioFileFormat baseFileFormat = AudioSystem.getAudioFileFormat(audioFile);
        Map<String,Object> properties = baseFileFormat.properties();
        Number rawLength = (Number) properties.get("duration");
        if (rawLength == null) {
            return 0L;
        }
        return rawLength.longValue() / 1000L;
    }
    
    /**
     * @param mp3File
     * @return
     * @throws Exception
     * 
     * @deprecated 不准确
     * 
     */
    @Deprecated
    public static long getMp3AudioLength(File mp3File) throws Exception {
        MP3File f = (MP3File) AudioFileIO.read(mp3File);
        MP3AudioHeader audioHeader = (MP3AudioHeader)f.getAudioHeader();
        return audioHeader.getTrackLength();
    }
    
    
    /**
     * @param audioFile
     * @return
     * @throws Exception
     */
    // /opt/ffmpeg/bin/ffmpeg -i 9HfXLPgSa0A9RzEFBVF2BQ.mp3 2>&1 | grep 'Duration' | cut -d ' ' -f 4 | sed s/,//
    public static long getAudioLengthV2(File audioFile) throws Exception {
    	List<String> list = Arrays.asList(AudioConstants.FFMPEG_BIN_PATH, "-i", audioFile.getAbsolutePath(), 
        		"2>&1", "|", "grep", "Duration", "|", "cut", "-d", "' '", "-f", "4", "|", "sed s/,//");
    	List<String> cmds = new ArrayList<String>();
    	cmds.add("sh");
    	cmds.add("-c");
    	cmds.add(Joiner.on(" ").join(list));
    	
    	ProcessBuilder pb = new ProcessBuilder(cmds);
        pb.environment().put("LD_LIBRARY_PATH", "/usr/local/lib/");
        pb.redirectErrorStream(true);
        Process p = pb.start();
        String result = IOUtils.toString(p.getInputStream());
        p.waitFor();
        long seconds = durationToSeconds(result);
    	logger.debug("Get audio length, cmd: {}, result: {}, seconds: {}", pb.command(), result, seconds);
    	
		return seconds;
    }
    
    
    private static File makeTempFile(String prefix, String ext) throws IOException {
        return File.createTempFile(prefix, ext, tempDir);
    }
    
    public static long durationToSeconds(String duration) {
    	int idx = duration.indexOf(".");
    	String[] parts = duration.substring(0, idx).split(":");
    	
    	long seconds = TimeUnit.HOURS.toSeconds(Long.parseLong(parts[0]));
    	seconds += TimeUnit.MINUTES.toSeconds(Long.parseLong(parts[1]));
    	seconds += Long.parseLong(parts[2]);
    	
    	return seconds;
    }
}
