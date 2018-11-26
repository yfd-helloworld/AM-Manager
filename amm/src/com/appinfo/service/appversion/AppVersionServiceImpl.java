package com.appinfo.service.appversion;

import com.appinfo.dao.appinfo.AppInfoMapper;
import com.appinfo.dao.appversion.AppVersionMapper;
import com.appinfo.pojo.AppVersion;
import com.appinfo.service.appinfo.AppInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/26 9:43
 */
@Service
public class AppVersionServiceImpl implements AppVersionService {

    @Resource
    private AppVersionMapper appVersionMapper;

    @Resource
    private AppInfoMapper appInfoMapper;

    @Override
    public List<AppVersion> getAppVersionList(Integer appId) throws Exception {
        return appVersionMapper.getAppVersionList(appId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean add(AppVersion appVersion) throws Exception {
        boolean flag = false;
        Integer versionId = null;
        if(appVersionMapper.add(appVersion) > 0){
            versionId = appVersion.getId();
            flag = true;
        }
        if(appInfoMapper.updateVersionId(versionId, appVersion.getAppId()) > 0 && flag){
            flag = true;
        }
        return flag;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean modify(AppVersion appVersion) throws Exception {
        if (appVersionMapper.modify(appVersion) == 1)
            return true;
        return false;
    }

    @Override
    public AppVersion getAppVersionById(Integer id) throws Exception {
        return appVersionMapper.getAppVersionById(id);
    }
}
