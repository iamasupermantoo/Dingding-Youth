package ${basePackage}.dao;

import java.util.List;
import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.model.EntityStatus;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
import ${basePackage}.constants.${enumStatus};
import ${basePackage}.model.${model};

/**
 * 
 * 
 * 
 * @author codegen
 */
@Repository
public class ${dao} extends AbstractDAO<Integer, ${model}>{
	private static final String TABLE_NAME = "${table}";
	
	private static final RowMapper<${model}> MAPPER = (rs, num)->
		new ${model}(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"),rs.getInt("status"),
			<#list dbFields as dbField>
			rs.get${dbField.type?cap_first}("${dbField.name}"),
			</#list>
			);
	
    public ${dao}() {
        super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
    }
    
    
    public int insert(
    	<#list dbFields as dbField>
		${dbField.type} ${dbField.paramName},
		</#list>
    	String data, ${enumStatus} status, long createTime) {
    	String sql = "insert into " + TABLE_NAME + "(<#list dbFields as dbField>${dbField.name},</#list>data, status, create_time) "
    			+ "values(<#list dbFields as dbField>:${dbField.placeholder},</#list> :data, :status, :createTime)";
    	
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource()
				<#list dbFields as dbField>
				.addValue("${dbField.placeholder}", ${dbField.paramName})
				</#list>
				.addValue("data", data)
				.addValue("status", status.getValue())
				.addValue("createTime", createTime)
				;
		getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
    	
    	return keyHolder.getKey().intValue();
    }
    
    public enum ${enumStatus} implements EntityStatus {
		Normal(0, "正常"),
		;
		
		private final int value;
		private final String name;
		${enumStatus}(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }
	
	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }
	
	    private static final IntObjectMap<${enumStatus}> map = new IntObjectOpenHashMap<>();
	    static {
	        for (${enumStatus} e : ${enumStatus}.values()) {
	            map.put(e.getValue(), e);
	        }
	    }
	
	    public static final ${enumStatus} fromValue(Integer value) {
	        return map.get(value);
	    }
	}
	    
}
