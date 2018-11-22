package com.appinfo.dao.devuser;

import com.appinfo.pojo.DevUser;
import org.apache.ibatis.annotations.Param;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/22 19:45
 */
public interface DevUserMapper {
    public DevUser login(@Param("devCode") String devCode,@Param("devPassword") String devPassword) throws Exception;
}
