package com.appinfo.dao.appcategory;

import com.appinfo.pojo.AppCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/24 13:40
 */
public interface AppCategoryMapper {
    public List<AppCategory> getAppCategoryListByParentId(@Param("parentId") Integer parentId) throws Exception;
}
