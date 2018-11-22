package com.appinfo.dao.backenduser;

import com.appinfo.pojo.BackendUser;
import org.apache.ibatis.annotations.Param;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/21 21:08
 */
public interface BackendUserMapper {
    public BackendUser login(@Param("userCode") String userName, @Param("password") String password) throws Exception;
}
