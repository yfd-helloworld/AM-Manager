package com.appinfo.dao.appinfo;

import com.appinfo.pojo.AppInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/23 8:31
 */
public interface AppInfoMapper {
    public int add(AppInfo appInfo) throws Exception;
    public int modify(AppInfo appInfo) throws Exception;
    public int deleteAppInfoById(@Param("id") Integer id) throws Exception;

    public List<AppInfo> getAppInfoList(@Param(value="softwareName")String querySoftwareName,
                               @Param(value="status")Integer queryStatus,
                               @Param(value="categoryLevel1")Integer queryCategoryLevel1,
                               @Param(value="categoryLevel2")Integer queryCategoryLevel2,
                               @Param(value="categoryLevel3")Integer queryCategoryLevel3,
                               @Param(value="flatformId")Integer queryFlatformId,
                               @Param(value="devId")Integer devId,
                               @Param(value="from")Integer currentPageNo,
                               @Param(value="pageSize")Integer pageSize)throws Exception;

    public int getAppInfoCount(@Param(value="softwareName")String querySoftwareName,
                               @Param(value="status")Integer queryStatus,
                               @Param(value="categoryLevel1")Integer queryCategoryLevel1,
                               @Param(value="categoryLevel2")Integer queryCategoryLevel2,
                               @Param(value="categoryLevel3")Integer queryCategoryLevel3,
                               @Param(value="flatformId")Integer queryFlatformId,
                               @Param(value="devId")Integer devId)throws Exception;

    public AppInfo getAppInfo(@Param("id")Integer id,@Param("APKName")String apkName) throws Exception;


    public int deleteAppLogo(@Param(value="id")Integer id)throws Exception;

    /**
     * 根据appId，更新最新versionId
     * @param versionId
     * @param appId
     * @return
     * @throws Exception
     */
    public int updateVersionId(@Param(value="versionId")Integer versionId,@Param(value="id")Integer appId)throws Exception;

    /**
     * updateSaleStatusByAppId
     * @param appId
     * @return
     * @throws Exception
     */
    public int updateSaleStatusByAppId(@Param(value="id")Integer appId) throws Exception;

    /*
     * 更新app状态
     * @param status
     * @param id
     * @return
     * @throws Exception
     */
    public int updateSatus(@Param(value="status")Integer status,@Param(value="id")Integer id)throws Exception;
}
