package com.appinfo.controller.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.appinfo.controller.BaseController;
import com.appinfo.pojo.AppCategory;
import com.appinfo.pojo.AppInfo;
import com.appinfo.pojo.AppVersion;
import com.appinfo.pojo.DataDictionary;
import com.appinfo.service.appcategory.AppCategoryService;
import com.appinfo.service.appinfo.AppInfoService;
import com.appinfo.service.appversion.AppVersionService;
import com.appinfo.service.datadictionary.DataDictionaryService;
import com.appinfo.tools.Constants;
import com.appinfo.tools.PageSupport;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/28 9:23
 */
@Controller
@RequestMapping(value="/manager/backend/app")
public class AppCheckController extends BaseController {
    private Logger logger = Logger.getLogger(AppCheckController.class);

    @Resource
    private AppInfoService appService;
    @Resource
    private AppVersionService appVersionService;
    @Resource
    private DataDictionaryService dataDictionaryService;
    @Resource
    private AppCategoryService appCategoryService;

    @RequestMapping(value="/list")
    public String getAppInfoList(Model model, HttpSession session,
                                 @RequestParam(value="querySoftwareName",required=false) String querySoftwareName,
                                 @RequestParam(value="queryCategoryLevel1",required=false) String _queryCategoryLevel1,
                                 @RequestParam(value="queryCategoryLevel2",required=false) String _queryCategoryLevel2,
                                 @RequestParam(value="queryCategoryLevel3",required=false) String _queryCategoryLevel3,
                                 @RequestParam(value="queryFlatformId",required=false) String _queryFlatformId,
                                 @RequestParam(value="pageIndex",required=false) String pageIndex){

        logger.info("getAppInfoList -- > querySoftwareName: " + querySoftwareName);
        logger.info("getAppInfoList -- > queryCategoryLevel1: " + _queryCategoryLevel1);
        logger.info("getAppInfoList -- > queryCategoryLevel2: " + _queryCategoryLevel2);
        logger.info("getAppInfoList -- > queryCategoryLevel3: " + _queryCategoryLevel3);
        logger.info("getAppInfoList -- > queryFlatformId: " + _queryFlatformId);
        logger.info("getAppInfoList -- > pageIndex: " + pageIndex);

        List<AppInfo> appInfoList = null;
        List<DataDictionary> flatFormList = null;
        List<AppCategory> categoryLevel1List = null;//列出一级分类列表，注：二级和三级分类列表通过异步ajax获取
        List<AppCategory> categoryLevel2List = null;
        List<AppCategory> categoryLevel3List = null;
        //页面容量
        int pageSize = Constants.pageSize;
        //当前页码
        Integer currentPageNo = 1;

        if(pageIndex != null){
            try{
                currentPageNo = Integer.valueOf(pageIndex);
            }catch (NumberFormatException e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        Integer queryCategoryLevel1 = null;
        if(_queryCategoryLevel1 != null && !_queryCategoryLevel1.equals("")){
            queryCategoryLevel1 = Integer.parseInt(_queryCategoryLevel1);
        }
        Integer queryCategoryLevel2 = null;
        if(_queryCategoryLevel2 != null && !_queryCategoryLevel2.equals("")){
            queryCategoryLevel2 = Integer.parseInt(_queryCategoryLevel2);
        }
        Integer queryCategoryLevel3 = null;
        if(_queryCategoryLevel3 != null && !_queryCategoryLevel3.equals("")){
            queryCategoryLevel3 = Integer.parseInt(_queryCategoryLevel3);
        }
        Integer queryFlatformId = null;
        if(_queryFlatformId != null && !_queryFlatformId.equals("")){
            queryFlatformId = Integer.parseInt(_queryFlatformId);
        }

        //总数量（表）
        int totalCount = 0;
        try {
            totalCount = appService.getAppInfoCount(querySoftwareName,1, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId,null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //总页数
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        int totalPageCount = pages.getTotalPageCount();
        //控制首页和尾页
        if(currentPageNo < 1){
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }
        try {
            appInfoList = appService.getAppInfoList(querySoftwareName,1, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId,null, currentPageNo, pageSize);
            flatFormList = this.getDataDictionaryList("APP_FLATFORM");
            categoryLevel1List = appCategoryService.getAppCategoryListByParentId(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("appInfoList", appInfoList);
        model.addAttribute("flatFormList", flatFormList);
        model.addAttribute("categoryLevel1List", categoryLevel1List);
        model.addAttribute("pages", pages);
        model.addAttribute("querySoftwareName", querySoftwareName);
        model.addAttribute("queryCategoryLevel1", queryCategoryLevel1);
        model.addAttribute("queryCategoryLevel2", queryCategoryLevel2);
        model.addAttribute("queryCategoryLevel3", queryCategoryLevel3);
        model.addAttribute("queryFlatformId", queryFlatformId);

        //二级分类列表和三级分类列表---回显
        if(queryCategoryLevel2 != null && !queryCategoryLevel2.equals("")){
            categoryLevel2List = getCategoryList(queryCategoryLevel1.toString());
            model.addAttribute("categoryLevel2List", categoryLevel2List);
        }
        if(queryCategoryLevel3 != null && !queryCategoryLevel3.equals("")){
            categoryLevel3List = getCategoryList(queryCategoryLevel2.toString());
            model.addAttribute("categoryLevel3List", categoryLevel3List);
        }
        return "backend/applist";
    }

    public List<DataDictionary> getDataDictionaryList(String typeCode){
        List<DataDictionary> dataDictionaryList = null;
        try {
            dataDictionaryList = dataDictionaryService.getDataDictionaryList(typeCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataDictionaryList;
    }

    public List<AppCategory> getCategoryList (String pid){
        List<AppCategory> categoryLevelList = null;
        try {
            categoryLevelList = appCategoryService.getAppCategoryListByParentId(pid==null?null:Integer.parseInt(pid));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryLevelList;
    }

    /**
     * 根据parentId查询出相应的分类级别列表
     * @param pid
     * @return
     */
    @RequestMapping(value="/categorylevellist.json",method=RequestMethod.GET)
    @ResponseBody
    public String getAppCategoryList (@RequestParam String pid){
        logger.debug("getAppCategoryList pid ============ " + pid);
        if(pid.equals("")) pid = null;
        return JSONArray.toJSONString(getCategoryList(pid));
    }


    /**
     * 跳转到APP信息审核页面
     * @param appId
     * @param versionId
     * @param model
     * @return
     */
    @RequestMapping(value="/check",method=RequestMethod.GET)
    public String check(@RequestParam(value="aid",required=false) String appId,
                        @RequestParam(value="vid",required=false) String versionId,
                        Model model){
        AppInfo appInfo = null;
        AppVersion appVersion = null;
        try {
            appInfo = appService.getAppInfo(Integer.parseInt(appId),null);
            appVersion = appVersionService.getAppVersionById(Integer.parseInt(versionId));
        }catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(appVersion);
        model.addAttribute(appInfo);
        return "backend/appcheck";
    }
    @RequestMapping(value="/checksave",method= RequestMethod.POST)
    public String checkSave(AppInfo appInfo){
        logger.debug("appInfo =========== > " + appInfo.getStatus());
        try {
            if(appService.updateSatus(appInfo.getStatus(),appInfo.getId())){
                return "redirect:/manager/backend/app/list";
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "backend/appcheck";
    }
}
