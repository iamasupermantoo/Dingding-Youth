package com.youshi.zebra.admin.adminuser.model;

import com.dorado.framework.crud.model.HasId;
import com.dorado.framework.crud.model.HasStatus;

/**
 * 
 * 运营后台管理员passport，管理员使用用户名和密码作为通行证，登录.
 * 
 * 术语：
 * AdminUser：代表后台管理员，可能用“administrator”更为准确。以AdminUser打头的类，都和后台管理员相关
 * 
 * @author wangsch
 * @date 2016-09-12
 */
public interface AdminUserPassport extends HasStatus, HasId<Integer> {

    String getUsername();
    
    String getPassword();

    String getSignature();
}
