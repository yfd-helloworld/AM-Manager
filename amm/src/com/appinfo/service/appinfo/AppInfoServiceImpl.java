package com.appinfo.service.appinfo;

import com.appinfo.dao.appinfo.AppInfoMapper;
import com.appinfo.pojo.AppInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/23 9:34
 */
@Service
public class AppInfoServiceImpl implements AppInfoService {

    @Resource
    private AppInfoMapper appInfoMapper;

    @Override
    public List<AppInfo> getAppInfoList(String querySoftwareName, Integer queryStatus, Integer queryCategoryLevel1, Integer queryCategoryLevel2, Integer queryCategoryLevel3, Integer queryFlatformId, Integer devId, Integer currentPageNo, Integer pageSize) throws Exception {
        return appInfoMapper.getAppInfoList(querySoftwareName,queryStatus,queryCategoryLevel1,queryCategoryLevel2,queryCategoryLevel3,queryFlatformId,devId,(currentPageNo-1)*pageSize,pageSize);
    }

    @Override
    public int getAppInfoCount(String querySoftwareName, Integer queryStatus, Integer queryCategoryLevel1, Integer queryCategoryLevel2, Integer queryCategoryLevel3, Integer queryFlatformId, Integer devId) throws Exception {
        return appInfoMapper.getAppInfoCount(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId, devId);
    }

    @Override
    public AppInfo getAppInfo(Integer id, String APKName) throws Exception {
        return appInfoMapper.getAppInfo(id,APKName);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean add(AppInfo appInfo) throws Exception {
        if (appInfoMapper.add(appInfo) == 1)
            return true;
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean modify(AppInfo appInfo) throws Exception {
        if (appInfoMapper.modify(appInfo) == 1)
            return true;
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean delete(Integer id) throws Exception {
        return false;
    }
}
