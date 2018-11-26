package com.appinfo.service.appcategory;

import com.appinfo.dao.appcategory.AppCategoryMapper;
import com.appinfo.pojo.AppCategory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/24 13:53
 */
@Service
public class AppCategoryServiceImpl implements AppCategoryService {

    @Resource
    private AppCategoryMapper appCategoryMapper;

    @Override
    public List<AppCategory> getAppCategoryListByParentId(Integer parentId) throws Exception {
        return appCategoryMapper.getAppCategoryListByParentId(parentId);
    }
}
