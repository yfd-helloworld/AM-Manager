package com.appinfo.controller.dev;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.appinfo.pojo.*;
import com.appinfo.service.appcategory.AppCategoryService;
import com.appinfo.service.appinfo.AppInfoService;
import com.appinfo.service.appversion.AppVersionService;
import com.appinfo.service.datadictionary.DataDictionaryService;
import com.appinfo.tools.Constants;
import com.appinfo.tools.PageSupport;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/23 9:38
 */
@Controller
@RequestMapping("/dev/app")
public class AppInfoController {
    private Logger logger = Logger.getLogger(AppInfoController.class);

    @Resource
    private AppInfoService appInfoService;

    @Resource
    private AppCategoryService appCategoryService;

    @Resource
    private DataDictionaryService dataDictionaryService;

    @Resource
    private AppVersionService appVersionService;

    @RequestMapping(value="/list.html")
    public String getAppInfoList(Model model, HttpSession session,
                                 @RequestParam(value="querySoftwareName",required=false) String querySoftwareName,
                                 @RequestParam(value="queryStatus",required=false) String _queryStatus,
                                 @RequestParam(value="queryCategoryLevel1",required=false) String _queryCategoryLevel1,
                                 @RequestParam(value="queryCategoryLevel2",required=false) String _queryCategoryLevel2,
                                 @RequestParam(value="queryCategoryLevel3",required=false) String _queryCategoryLevel3,
                                 @RequestParam(value="queryFlatformId",required=false) String _queryFlatformId,
                                 @RequestParam(value="pageIndex",required=false) String pageIndex){

        logger.info("getAppInfoList -- > querySoftwareName: " + querySoftwareName);
        logger.info("getAppInfoList -- > queryStatus: " + _queryStatus);
        logger.info("getAppInfoList -- > queryCategoryLevel1: " + _queryCategoryLevel1);
        logger.info("getAppInfoList -- > queryCategoryLevel2: " + _queryCategoryLevel2);
        logger.info("getAppInfoList -- > queryCategoryLevel3: " + _queryCategoryLevel3);
        logger.info("getAppInfoList -- > queryFlatformId: " + _queryFlatformId);
        logger.info("getAppInfoList -- > pageIndex: " + pageIndex);

        Integer devId = ((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId();
        List<AppInfo> appInfoList = null;
        List<DataDictionary> statusList = null;
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
                e.printStackTrace();
            }
        }
        Integer queryStatus = null;
        if(_queryStatus != null && !_queryStatus.equals("")){
            queryStatus = Integer.parseInt(_queryStatus);
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
            totalCount = appInfoService.getAppInfoCount(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId, devId);
        } catch (Exception e) {
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
            appInfoList = appInfoService.getAppInfoList(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId, devId, currentPageNo, pageSize);
            statusList = this.getDataDictionaryList("APP_STATUS");
            flatFormList = this.getDataDictionaryList("APP_FLATFORM");
            categoryLevel1List = appCategoryService.getAppCategoryListByParentId(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("appInfoList", appInfoList);
        model.addAttribute("statusList", statusList);
        model.addAttribute("flatFormList", flatFormList);
        model.addAttribute("categoryLevel1List", categoryLevel1List);
        model.addAttribute("pages", pages);
        model.addAttribute("queryStatus", queryStatus);
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
        return "developer/appinfolist";
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

    /**
     * 根据parentId查询出相应的分类级别列表
     * @param pid
     * @return
     */
    @RequestMapping(value="/categorylevellist.json",method= RequestMethod.GET)
    @ResponseBody
    public String getAppCategoryList (@RequestParam String pid){
        logger.debug("getAppCategoryList pid ============ " + pid);
        if(pid.equals("")) pid = null;
        return JSON.toJSONString(getCategoryList(pid));     //使用Ajax时不能直接返回Java类型
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

    @RequestMapping(value = "/appinfoadd.html",method = RequestMethod.GET)
    public String appinfoadd(){
        System.out.println("进入增加页面");
        return "developer/appinfoadd";
    }


    @RequestMapping(value = "datadictionarylist.json",method = RequestMethod.GET)
    @ResponseBody
    public String getDataDicList(String typeCode){
        return JSON.toJSONString(this.getDataDictionaryList(typeCode));
    }

    @RequestMapping(value = "apkexist.json",method = RequestMethod.GET)
    @ResponseBody
    public String verifyAPKNameSole(String APKName){
        String result = "{\"APKName\":\"empty\"}";
        if (APKName != null && !APKName.equals("")){
            try {
                AppInfo appInfo = appInfoService.getAppInfo(null,APKName);
                if (appInfo == null)
                    result = "{\"APKName\":\"noexist\"}";
                else
                    result = "{\"APKName\":\"exist\"}";
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }


    @RequestMapping(value="/appinfoaddsave",method=RequestMethod.POST)
    public String addSave(AppInfo appInfo, HttpSession session, HttpServletRequest request,
                          @RequestParam(value="a_logoPicPath",required= false) MultipartFile attach){

        String logoPicPath =  null;
        String logoLocPath =  null;
        System.out.println("开始保存----------------->");
        if(!attach.isEmpty()){
            System.out.println("文件不为空-------------->");
            System.out.println("文件大小："+attach.getSize());
            String path = request.getSession().getServletContext().getRealPath("statics"+java.io.File.separator+"uploadfiles");
            logger.info("uploadFile path: " + path);
            String oldFileName = attach.getOriginalFilename();//原文件名
            String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
            int filesize = 500000;
            if(attach.getSize() > filesize){//上传大小不得超过 50k
                request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_4);
                return "developer/appinfoadd";
            }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                    ||prefix.equalsIgnoreCase("jepg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式
                String fileName = appInfo.getAPKName() + ".jpg";//上传LOGO图片命名:apk名称.apk
                File targetFile = new File(path,fileName);
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }
                try {
                    attach.transferTo(targetFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_2);
                    return "developer/appinfoadd";
                }
                logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
                logoLocPath = path+File.separator+fileName;
            }else{
                request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_3);
                return "developer/appinfoadd";
            }
        }
        appInfo.setCreatedBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
        appInfo.setCreationDate(new Date());
        appInfo.setLogoPicPath(logoPicPath);
        appInfo.setLogoLocPath(logoLocPath);
        appInfo.setDevId(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
        appInfo.setStatus(1);
        try {
            if(appInfoService.add(appInfo)){
                return "redirect:/dev/app/list.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "developer/appinfoadd";
    }

    @RequestMapping(value = "/appinfomodify",method = RequestMethod.GET)
    public String modifyAppInfo(String id, Model model) {
        AppInfo appInfo = null;
        try {
            appInfo = appInfoService.getAppInfo(Integer.parseInt(id), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("appInfo", appInfo);
        return "developer/appinfomodify";
    }

    @RequestMapping(value="/appinfomodifysave",method=RequestMethod.POST)
    public String modifySave(AppInfo appInfo,HttpSession session,HttpServletRequest request,
                             @RequestParam(value="attach",required= false) MultipartFile attach){
        String logoPicPath =  null;
        String logoLocPath =  null;
        String APKName = appInfo.getAPKName();
        if(!attach.isEmpty()){
            String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
            logger.info("uploadFile path: " + path);
            String oldFileName = attach.getOriginalFilename();//原文件名
            String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
            int filesize = 500000;
            if(attach.getSize() > filesize){//上传大小不得超过 50k
                return "redirect:/dev/app/appinfomodify?id="+appInfo.getId()
                        +"&error=error4";
            }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                    ||prefix.equalsIgnoreCase("jepg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式
                String fileName = APKName + ".jpg";//上传LOGO图片命名:apk名称.apk
                File targetFile = new File(path,fileName);
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }
                try {
                    attach.transferTo(targetFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    return "redirect:/dev/app/appinfomodify?id="+appInfo.getId()
                            +"&error=error2";
                }
                logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
                logoLocPath = path+File.separator+fileName;
            }else{
                return "redirect:/dev/app/appinfomodify?id="+appInfo.getId()
                        +"&error=error3";
            }
        }
        appInfo.setModifyBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
        appInfo.setModifyDate(new Date());
        appInfo.setLogoLocPath(logoLocPath);
        appInfo.setLogoPicPath(logoPicPath);
        try {
            if(appInfoService.modify(appInfo)){
                return "redirect:/dev/app/list.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "developer/appinfomodify";
    }


    @RequestMapping(value = "/appview/{id}", method = RequestMethod.GET)
    public String messageAll(@PathVariable String id, Model model) {
        AppInfo appInfo = null;
        List<AppVersion> versionList = null;
        Integer appinfoId = Integer.parseInt(id);
        try {
            appInfo = appInfoService.getAppInfo(appinfoId, null);
            versionList = appVersionService.getAppVersionList(appinfoId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("appInfo", appInfo);
        model.addAttribute("appVersionList", versionList);
        return "developer/appinfoview";
    }

    @RequestMapping(value = "/delfile",method=RequestMethod.GET)
    @ResponseBody
    public Object delFile(@RequestParam(value="flag",required=false) String flag,
                          @RequestParam(value="id",required=false) String id){
        HashMap<String, String> resultMap = new HashMap<String, String>();
        String fileLocPath = null;
        if(flag == null || flag.equals("") ||
                id == null || id.equals("")){
            resultMap.put("result", "failed");
        }else if(flag.equals("logo")){//删除logo图片（操作app_info）
            try {
                fileLocPath = (appInfoService.getAppInfo(Integer.parseInt(id), null)).getLogoLocPath();
                File file = new File(fileLocPath);
                if(file.exists())
                    if(file.delete()){//删除服务器存储的物理文件
                        if(appInfoService.deleteAppLogo(Integer.parseInt(id))){//更新表
                            resultMap.put("result", "success");
                        }
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(flag.equals("apk")){//删除apk文件（操作app_version）
            try {
                fileLocPath = (appVersionService.getAppVersionById(Integer.parseInt(id))).getApkLocPath();
                File file = new File(fileLocPath);
                if(file.exists())
                    if(file.delete()){//删除服务器存储的物理文件
                        if(appVersionService.deleteApkFile(Integer.parseInt(id))){//更新表
                            resultMap.put("result", "success");
                        }
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    @RequestMapping(value="/delapp.json")
    @ResponseBody
    public Object delApp(@RequestParam String id){
        logger.debug("delApp appId===================== "+id);
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(StringUtils.isNullOrEmpty(id)){
            resultMap.put("delResult", "notexist");
        }else{
            try {
                if(appInfoService.delete(Integer.parseInt(id)))
                    resultMap.put("delResult", "true");
                else
                    resultMap.put("delResult", "false");
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    @RequestMapping(value="/{appid}/sale",method=RequestMethod.GET)
    @ResponseBody
    public String sale(@PathVariable String appid,HttpSession session){
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        Integer appIdInteger = 0;
        try{
            appIdInteger = Integer.parseInt(appid);
        }catch(Exception e){
            appIdInteger = 0;
        }
        resultMap.put("errorCode", "0");
        resultMap.put("appId", appid);
        if(appIdInteger>0){
            try {
                DevUser devUser = (DevUser)session.getAttribute(Constants.DEV_USER_SESSION);
                AppInfo appInfo = new AppInfo();
                appInfo.setId(appIdInteger);
                appInfo.setModifyBy(devUser.getId());
                if(appInfoService.appsysUpdateSaleStatusByAppId(appInfo)){
                    resultMap.put("resultMsg", "success");
                }else{
                    resultMap.put("resultMsg", "success");
                }
            } catch (Exception e) {
                resultMap.put("errorCode", "exception000001");
            }
        }else{
            //errorCode:0为正常
            resultMap.put("errorCode", "param000001");
        }

        /*
         * resultMsg:success/failed
         * errorCode:exception000001
         * appId:appId
         * errorCode:param000001
         * 处理Ajax请求服务器端以Map对象存储结果时，直接返回Java的Map对象会造成客户端406
         * 应该将Java对象转换为JSON字符串返回出方法
         */
        return JSONArray.toJSONString(resultMap);
    }
}
