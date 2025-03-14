package com.dorado.event.server.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Sets;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public enum SingleProcessCheck {
    instance;

    public enum ProcessType {
        DoradoEventConsumer(401), //
        

        ;

        /*
         * 注明：
         * 1、端口范围为：0-65535, 其中0-1023为常用端口不可以去占用它
         * 2、centos /proc/sys/net/ipv4/ip_local_port_range 默认配置为：5000 65000，作为客户端可打开的端口的地址范围为5000->65000
         * 3、我们可以利用65000->65535之间的五百个端口，或者1024->4999之间的端口，这样我们可以尽可能减少端口冲突
         * 
         */
        private static final int BASE = 65000;

        private final int port;

        /**
         * @return the port
         */
        public int getPort() {
            return port;
        }

        /**
         * @param port
         */
        ProcessType(int port) {
            this.port = BASE + port;
        }

        static {
            Set<Integer> blogIds = Sets.newHashSet();
            for (ProcessType type : values()) {

                if (type.getPort() <= BASE) {
                    throw new RuntimeException("端口不可以小于等于0");
                }

                if (type.getPort() > 65535) {
                    throw new RuntimeException("端口不能超出65535");
                }

                if (!blogIds.add(type.getPort())) {
                    throw new RuntimeException("找到重复的port.哥们.");
                }
            }
        }

    }

    private final Map<ProcessType, ServerSocket> socketMap = new ConcurrentHashMap<SingleProcessCheck.ProcessType, ServerSocket>();

    /**
     * 检查这个进程是否启动过
     * 
     * @param processType
     * @return
     */
    public boolean isStarted(ProcessType processType) {
        if (socketMap.containsKey(processType)) {
            return false;
        }
        try {
            socketMap.put(processType, new ServerSocket(processType.getPort()));
            return false;
        } catch (IOException e) {
            //System.out.println("process:" + processType.getName() + " is started.");
            return true;
        }
    }

    public ProcessType getStartedProcess() {
        for (ProcessType processType : socketMap.keySet()) {
            return processType;
        }
        return null;
    }

    @Override
    public String toString() {
        return name();
    }

}
