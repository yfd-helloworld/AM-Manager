package com.appinfo.service.appinfo;

import com.appinfo.dao.appinfo.AppInfoMapper;
import com.appinfo.dao.appversion.AppVersionMapper;
import com.appinfo.pojo.AppInfo;
import com.appinfo.pojo.AppVersion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
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

    @Resource
    private AppVersionMapper appVersionMapper;

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
        if (appInfoMapper.deleteAppInfoById(id) == 1)
            return true;
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteAppLogo(Integer id) throws Exception {
        if (appInfoMapper.deleteAppLogo(id) == 1)
            return true;
        return false;
    }

    @Override
    public boolean appsysUpdateSaleStatusByAppId(AppInfo appInfoObj) throws Exception {
		/*
		 * 上架：
			1 更改status由【2 or 5】 to 4 ， 上架时间
			2 根据versionid 更新 publishStauts 为 2

			下架：
			更改status 由4给为5
		 */

        Integer operator = appInfoObj.getModifyBy();
        if(operator < 0 || appInfoObj.getId() < 0 ){
            throw new Exception();
        }

        //get appinfo by appid
        AppInfo appInfo = appInfoMapper.getAppInfo(appInfoObj.getId(), null);
        if(null == appInfo){
            return false;
        }else{
            switch (appInfo.getStatus()) {
                case 2: //当状态为审核通过时，可以进行上架操作
                    onSale(appInfo,operator,4,2);
                    break;
                case 5://当状态为下架时，可以进行上架操作
                    onSale(appInfo,operator,4,2);
                    break;
                case 4://当状态为上架时，可以进行下架操作
                    offSale(appInfo,operator,5);
                    break;

                default:
                    return false;
            }
        }
        return true;
    }


    private void onSale(AppInfo appInfo,Integer operator,Integer appInfStatus,Integer versionStatus) throws Exception{
        offSale(appInfo,operator,appInfStatus);
        setSaleSwitchToAppVersion(appInfo,operator,versionStatus);
    }


    /**
     * offSale
     * @param appInfo
     * @param operator
     * @param appInfStatus
     * @return
     * @throws Exception
     */
    private boolean offSale(AppInfo appInfo,Integer operator,Integer appInfStatus) throws Exception{
        AppInfo _appInfo = new AppInfo();
        _appInfo.setId(appInfo.getId());
        _appInfo.setStatus(appInfStatus);
        _appInfo.setModifyBy(operator);
        _appInfo.setOffSaleDate(new Date(System.currentTimeMillis()));
        appInfoMapper.modify(_appInfo);
        return true;
    }

    @Override
    public boolean updateSatus(Integer status, Integer id) throws Exception {
        if (appInfoMapper.updateSatus(status, id)==1)
            return true;
        return false;
    }

    /**
     * set sale method to on or off
     * @param appInfo
     * @param operator
     * @return
     * @throws Exception
     */
    private boolean setSaleSwitchToAppVersion(AppInfo appInfo,Integer operator,Integer saleStatus) throws Exception{
        AppVersion appVersion = new AppVersion();
        appVersion.setId(appInfo.getVersionId());
        appVersion.setPublishStatus(saleStatus);
        appVersion.setModifyBy(operator);
        appVersion.setModifyDate(new Date(System.currentTimeMillis()));
        appVersionMapper.modify(appVersion);
        return false;
    }
}
