package com.appinfo.service.backenduser;

import com.appinfo.pojo.BackendUser;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/22 13:59
 */
public interface BackendUserService {
    public BackendUser login(String userCode, String password) throws Exception;
}
