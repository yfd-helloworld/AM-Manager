package com.appinfo.service.devuser;

import com.appinfo.dao.devuser.DevUserMapper;
import com.appinfo.pojo.DevUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/22 19:55
 */
@Service("devService")
public class DevUserServiceImpl implements DevUserService {
    @Resource
    private DevUserMapper devUserMapper;

    @Override
    public DevUser login(String devCode, String devPassword) throws Exception {
        return devUserMapper.login(devCode, devPassword);
    }
}
