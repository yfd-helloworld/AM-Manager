package com.appinfo.dao.appversion;

import com.appinfo.pojo.AppVersion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/26 9:23
 */
public interface AppVersionMapper {
    public List<AppVersion> getAppVersionList(@Param("appId")Integer appId) throws Exception;

    public int add(AppVersion appVersion)throws Exception;

    public int getVersionCountByAppId(@Param("appId")Integer appId)throws Exception;

    public int deleteVersionByAppId(@Param("appId")Integer appId)throws Exception;

    public AppVersion getAppVersionById(@Param("id")Integer id)throws Exception;

    public int modify(AppVersion appVersion)throws Exception;

    public int deleteApkFile(@Param("id")Integer id)throws Exception;
}
