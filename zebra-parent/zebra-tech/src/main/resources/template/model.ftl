package ${basePackage}.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;

/**
 * 
 * @author codegen
 */
public class ${model} extends AbstractModel<Integer>{
	public enum ${keysEnum} {
		// db 字段
		<#list dbFields as dbField>
		${dbField.name},
		</#list>
	
		// data字段
		<#list dataFields as dataField>
		${dataField.name},
		</#list>
	}
	
	<#list dbFields as dbField>
	private ${dbField.type} ${dbField.paramName};
	</#list>
	
	public ${model}(
			int id, String data, long createTime, int status,
			<#list dbFields as dbField>
			${dbField.type} ${dbField.paramName}<#if dbField.isLast == false>,</#if>
			</#list>
			) {
		super(id, data, createTime, status);
		<#list dbFields as dbField>
		this.${dbField.paramName} = ${dbField.paramName};
		</#list>
	}

	<#list dbFields as dbField>
	public ${dbField.type} get${dbField.paramName?cap_first}() {
		return ${dbField.paramName};
	}
	</#list>
	
	<#list dataFields as dataField>
	public ${dataField.type} get${dataField.paramName?cap_first}() {
		return ModelUtils.get${dataField.type?cap_first}(this, ${keysEnum}.${dataField.name});
	}
	</#list>
}
