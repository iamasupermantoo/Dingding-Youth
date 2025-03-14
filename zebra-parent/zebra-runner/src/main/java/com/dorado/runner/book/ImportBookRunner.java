package com.dorado.runner.book;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * 
 * @author wangsch
 * @date 2017年8月21日
 */
public class ImportBookRunner {
	private static HelpFormatter formatter = new HelpFormatter();
	private static Options options = new Options();
	private static CommandLineParser parser = new PosixParser();
	
	static {
		initCli();
	}
	
	public static void main(String[] args) {
		UploadBookArgs parsedArgs = parseCli(args);
		
//		DoradoBeanFactory.init();
		
		String uploadDir = "";
		File file = new File(uploadDir);
		String[] files = file.list();
		for (String fileName : files) {
			File child = new File(fileName);
//			if(child.get) {
//				
//			}
			
		}
		
		
		
		
		System.out.println("-------------------------------- FINISHED -------------------------------");
		System.exit(0);
	}
	
	@SuppressWarnings("static-access")
	private static void initCli() {
		options.addOption(OptionBuilder
				.isRequired()
				.hasArg()
				.withArgName("zip-file")
				.withDescription("zip文件，绝对路径")
				.create("zip"));
		options.addOption(OptionBuilder
				.isRequired()
				.hasArg()
				.withArgName("excel-file")
				.withDescription("excel文件，绝对路径")
				.create("excel"));
		options.addOption(OptionBuilder
				.isRequired()
				.withArgName("book-name").hasArg()
				.withDescription("教材名")
				.create("name"));
		options.addOption(OptionBuilder
				.withLongOpt("help")
				.withDescription("打印帮助信息")
				.create("h"));
	}

	public static UploadBookArgs parseCli(String[] args) {
		CommandLine cli = null;
		try {
			cli = parser.parse(options, args);
		} catch (ParseException e) {
			printHelp();
			System.exit(0);
		}
        if(cli.hasOption("h")) {
        	printHelp();
        	System.exit(0);
        }
        
        
        String name = cli.getOptionValue("name");
        String zipFilePath = cli.getOptionValue("zip");
        String excelFilePath = cli.getOptionValue("excel");
        
        UploadBookArgs result = new UploadBookArgs();
        result.name = name;
        result.zipFilePath = zipFilePath;
        result.excelFilePath = excelFilePath;
        
        return result;
	}
	
	public static void printHelp() {
		formatter.printHelp("book-import", options);
	}
	
	public static class UploadBookArgs {
		String name;
		String zipFilePath;
		String excelFilePath;
	}
}
