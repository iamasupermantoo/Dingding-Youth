package com.youshi.zebra.tech;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Properties;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 模版操作业务对象
 * @author wangsch
 *
 */
public class TemplateUtils {
	
	
	/**模板目录*/
	private static String templateDir = null;
	
	/** freemarker配置对象 */
	private static Configuration freemarkerCfg = null;
	
	/** service接口模板 */
	public static final String SERVICE_FTL = "service.ftl";
	
	/** dao模板文件 */
	public static final String DAO_FTL = "dao.ftl";
	
	/** model模板文件 */
	public static final String MODEL_FTL = "model.ftl";
	
	static {
		URL templateUrl = TemplateUtils.class.getClassLoader().getResource("template/");
		if(templateUrl==null){
			throw new RuntimeException("未能成功初始化模板业务类，未找到模板路径");	
		}
		
		
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name");
		if(os.startsWith("win") || os.startsWith("Win")) {
			templateDir = templateUrl.getFile().substring(1,templateUrl.getFile().length());
		} else {
			templateDir = templateUrl.getFile().substring(0, templateUrl.getFile().length());
		}
		
		try {
			freemarkerCfg = new Configuration(Configuration.VERSION_2_3_0);
			freemarkerCfg.setDirectoryForTemplateLoading(new File(templateDir));
			freemarkerCfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_0));
		} catch (IOException e) {
			throw new RuntimeException("未能成功初始化freemarker配置",e);
		}
		
	}
	
	public static Configuration getFreemarkerCfg(){
		return freemarkerCfg;
	}
			

	/**
	 * 根据模版获取代码片段
	 * @param ftlName 	模版文件名
	 * @param dataModel 数据模型
	 * @return 代码片段
	 */
	public static String getCodeFormTemplate(String ftlName, Object dataModel){
		StringWriter writer = new StringWriter();
		try {
			Template template = TemplateUtils.getFreemarkerCfg().getTemplate(ftlName, "UTF-8");
			template.process(dataModel, writer);
		} catch (IOException e) {
			throw new RuntimeException("未能成功获取模板",e);
		} catch (TemplateException e) {
			throw new RuntimeException("未能成功合并模板和数据模型",e);
		}
		
		return writer.toString();
	}
	
	/**
	 * 根据文件名，获取模版对象
	 * @param ftlName 文件名
	 * @param codeDescription 模版描述
	 * @return 模版对象
	 */
	public static Template getTemplate(String ftlName, String codeDescription) {
		try {
			return TemplateUtils.getFreemarkerCfg().getTemplate(ftlName);
		} catch (IOException e) {
			throw new RuntimeException(codeDescription+"：未能成功获取模板",e);
		}
	}
}
