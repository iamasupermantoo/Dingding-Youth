package com.dorado.runner.exam;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.dorado.framework.utils.DoradoBeanFactory;
import com.youshi.zebra.exam.service.ExamImportService;
import com.youshi.zebra.exam.service.ExamImportService.ExamImportResult;
import com.youshi.zebra.exam.service.ExamService.ExamLevel;

/**
 * 
 * @author wangsch
 * @date 2017年8月31日
 */
public class ImportExamRunner {
	private static HelpFormatter formatter = new HelpFormatter();
	private static Options options = new Options();
	private static CommandLineParser parser = new PosixParser();
	
	static {
		initCli();
	}
	
	
	public static void main(String[] args) {
		ImportExamArgs parsedArgs = parseCli(args);
		
		DoradoBeanFactory.init();
		ExamImportService bean = DoradoBeanFactory.getBean(ExamImportService.class);
		
		System.out.println("-------------------------------- STARTED -------------------------------");
		ExamImportResult result = bean.importExam(parsedArgs.name, parsedArgs.level,
				new File(parsedArgs.zipFilePath), new File(parsedArgs.excelFilePath));
		
		System.out.println(String.format("INFO: examId: %s, question count: %s", 
				result.getExamId(), result.getQuestionCnt()));
		
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
				.withArgName("exam-name").hasArg()
				.withDescription("试卷名")
				.create("name"));
		options.addOption(OptionBuilder
				.isRequired()
				.withArgName("exam-level").hasArg()
				.withDescription("试卷级别")
				.create("level"));
		options.addOption(OptionBuilder
				.withLongOpt("help")
				.withDescription("打印帮助信息")
				.create("h"));
	}

	public static ImportExamArgs parseCli(String[] args) {
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
        String level = cli.getOptionValue("level");
        String zipFilePath = cli.getOptionValue("zip");
        String excelFilePath = cli.getOptionValue("excel");
        
        ImportExamArgs result = new ImportExamArgs();
        result.name = name;
        result.level = ExamLevel.valueOf(level);
        result.zipFilePath = zipFilePath;
        result.excelFilePath = excelFilePath;
        
        return result;
	}
	
	public static void printHelp() {
		formatter.printHelp("book-import", options);
	}
	
	public static class ImportExamArgs {
		String name;
		ExamLevel level;
		String zipFilePath;
		String excelFilePath;
	}
}
