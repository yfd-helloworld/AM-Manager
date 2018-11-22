package com.appinfo.service.devuser;

import com.appinfo.pojo.DevUser;
import org.springframework.stereotype.Service;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/22 19:54
 */
public interface DevUserService {
    public DevUser login(String devCode,String devPassword) throws Exception;
}
