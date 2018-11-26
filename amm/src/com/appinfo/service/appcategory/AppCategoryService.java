package com.appinfo.service.appcategory;

import com.appinfo.pojo.AppCategory;

import java.util.List;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/24 13:51
 */
public interface AppCategoryService {
    public List<AppCategory> getAppCategoryListByParentId(Integer parentId) throws Exception;
}
