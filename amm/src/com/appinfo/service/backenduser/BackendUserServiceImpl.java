package com.appinfo.service.backenduser;

import com.appinfo.dao.backenduser.BackendUserMapper;
import com.appinfo.pojo.BackendUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/22 14:00
 */
@Service("backendUserService")
public class BackendUserServiceImpl implements BackendUserService {

    @Resource
    private BackendUserMapper backendUserMapper;

    @Override
    public BackendUser login(String userCode, String password) throws Exception {
        return backendUserMapper.login(userCode, password);
    }
}
