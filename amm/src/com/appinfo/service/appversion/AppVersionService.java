package com.appinfo.service.appversion;

import com.appinfo.pojo.AppVersion;

import java.util.List;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/26 9:41
 */
public interface AppVersionService {

    public List<AppVersion> getAppVersionList(Integer appId) throws Exception;

    public boolean add(AppVersion appVersion) throws Exception;

    public boolean modify(AppVersion appVersion) throws Exception;

    public AppVersion getAppVersionById(Integer id) throws Exception;
}
