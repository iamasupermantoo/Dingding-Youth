package com.dorado.runner.sys;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.curator.framework.CuratorFramework;

import com.dorado.framework.constants.InProduction;
import com.dorado.framework.zookeeper.ZkClientHolder;

/**
 * 
 * dump zookeeper全部数据，打印到标准输出。定时执行，用于备份zk数据。
 * 
 * @author wangsch
 * @date 2016年12月10日
 */
public class ZKDumpRunner {
	
	private CuratorFramework zkClient = ZkClientHolder.get();
	
	public static void main(String[] args) {
		try {
			ZKDumpRunner dumper = new ZKDumpRunner();
			
			String time = DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm");
			String env = InProduction.get()? "production" : "development";
			
			System.out.println("----------------------------------------------------------ZK dump(" + time + ", " + env + ")--------------------------------------------------------");
			dumper.printZNodes("/", 0);
			dumper.close();
			System.out.println("---------------------------------------------------------------------------END----------------------------------------------------------------------------------");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void printZNodes(String path, int depth) throws Exception {
		List<String> children = zkClient.getChildren().forPath(path);
		for (String child : children) {
			String childPath = childPath(path, child);
			String childValue = new String(zkClient.getData().forPath(childPath));
			System.out.println(getKeyValue(depth, child, childValue));
			printZNodes(childPath, depth + 1);
		}
	}
	
	public void close() {
		zkClient.close();
	}

	private String getKeyValue(int depth, String child, String childValue) {
		if(StringUtils.isEmpty(childValue) || "''".equals(childValue)) {
			return padding(depth) + child;
		}
		return padding(depth) + child + "-->" + childValue;
	}

	private String childPath(String path, String child) {
		String childPath = (path.equals("/") ? "" : path) + "/" + child;
		return childPath;
	}
	
	private String padding(int depth) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<depth; i++) {
			sb.append("\t");
		}
		return sb.toString();
	}
}
