package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.youshi.zebra.tech.TemplateUtils;
import com.youshi.zebra.tech.component.codegen.DBField;
import com.youshi.zebra.tech.component.codegen.DataField;
import com.youshi.zebra.tech.component.codegen.FTLUtils;
import com.youshi.zebra.tech.component.codegen.JavaType;
import com.youshi.zebra.tech.component.codegen.ftl.DAOFtl;
import com.youshi.zebra.tech.component.codegen.ftl.ModelFtl;
import com.youshi.zebra.tech.component.codegen.ftl.ServiceFtl;

/**
 * 
 * @author wangsch
 * @date 2017年4月7日
 */
public class FtlTest {
	
	@Test
	public void test() {
		String code = TemplateUtils.getCodeFormTemplate("test.ftl", ImmutableMap.<String, Object>builder()
				.put("username", "wangsch")
				.build());
		System.out.println(code);
		
		code = TemplateUtils.getCodeFormTemplate("test.ftl", new User());
		System.out.println(code);
		
	}
	
	public class User {
		public String getUsername() {
			return "wangsancheng";
		}
	}
	
	public static void main(String[] args) {
		// 输入参数
		String basePackage = "com.dorado.pack";
		String baseDir = "/Users/wangsch/workspace/pansy-parent/pansy-component/src/main/java/com/dorado/pack/";
		String table = "pack_music";
		
		List<DBField> dbFields = new ArrayList<>(Arrays.asList(
				
				));

		List<DataField> dataFields = Arrays.asList(
            new DataField(JavaType.STRING, "audio_id"),
				new DataField(JavaType.STRING, "content", true)
		        );
		// END 输入参数
		
		// 
		String caped = FTLUtils.toCamelStyle(table, true);
		String model = caped + "Model";
		String keyEnum = caped + "Keys";
		String enumStatus = caped + "Status";
		String dao = caped + "DAO";
		String service = caped + "Service";
		
		System.out.println("-------------------------------- Model 代码 --------------------------------");
		ModelFtl dataModel = new ModelFtl()
				.setBasePackage(basePackage)
				.setModel(model)
				.setKeysEnum(keyEnum)
				.addDBFields(dbFields)
				.addDataFields(dataFields);
		
		String modelCode = TemplateUtils.getCodeFormTemplate("model.ftl", dataModel);
		System.out.println(modelCode);
		
		System.out.println("-------------------------------- DAO 代码 --------------------------------");
		DAOFtl daoModel = new DAOFtl()
				.setBasePackage(basePackage)
				.setEnumStatus(enumStatus)
				.setModel(model)
				.setDao(dao)
				.setTable(table)
				.addDBFields(dbFields)
				;
		
		String daoCode = TemplateUtils.getCodeFormTemplate("dao.ftl", daoModel);
		System.out.println(daoCode);
		
		System.out.println("-------------------------------- Service 代码 --------------------------------");

		ServiceFtl serviceModel = new ServiceFtl()
				.setBasePackage(basePackage)
				.setModel(model)
				.setDao(dao)
				.setService(service)
				;
		
		String serviceCode = TemplateUtils.getCodeFormTemplate("service.ftl", serviceModel);
		System.out.println(serviceCode);
		
		
		System.out.println("-------------------------------- 写文件 --------------------------------");
		try {
			File file = new File(baseDir + "/model", model + ".java");
			if(file.exists()) {
				throw new RuntimeException(file.getAbsolutePath());
			}
			FileUtils.writeStringToFile(file, modelCode, "UTF-8");
			
			file = new File(baseDir + "/dao", dao + ".java");
			if(file.exists()) {
				throw new RuntimeException(file.getAbsolutePath());
			}
			FileUtils.writeStringToFile(file, daoCode, "UTF-8");
			
			file = new File(baseDir + "/service", service + ".java");
			if(file.exists()) {
				throw new RuntimeException(file.getAbsolutePath());
			}
			FileUtils.writeStringToFile(file, serviceCode, "UTF-8");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
