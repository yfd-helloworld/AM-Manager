package com.appinfo.service.appinfo;

import com.appinfo.pojo.AppInfo;

import java.util.List;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/23 8:38
 */
public interface AppInfoService {
    public List<AppInfo> getAppInfoList(String querySoftwareName, Integer queryStatus, Integer queryCategoryLevel1,
                                        Integer queryCategoryLevel2, Integer queryCategoryLevel3, Integer queryFlatformId,
                                        Integer devId, Integer currentPageNo, Integer pageSize) throws Exception;

    public int getAppInfoCount(String querySoftwareName, Integer queryStatus, Integer queryCategoryLevel1,
                               Integer queryCategoryLevel2, Integer queryCategoryLevel3, Integer queryFlatformId,
                               Integer devId) throws Exception;

    public AppInfo getAppInfo(Integer id,String APKName) throws Exception;

    public boolean add(AppInfo appInfo) throws Exception;

    public boolean modify(AppInfo appInfo) throws Exception;

    public boolean delete(Integer id) throws Exception;

    public boolean deleteAppLogo(Integer id) throws Exception;

    public boolean appsysUpdateSaleStatusByAppId(AppInfo appInfoObj) throws Exception;
}
