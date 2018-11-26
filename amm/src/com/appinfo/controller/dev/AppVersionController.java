package com.appinfo.controller.dev;

import com.appinfo.pojo.AppVersion;
import com.appinfo.pojo.DevUser;
import com.appinfo.service.appinfo.AppInfoService;
import com.appinfo.service.appversion.AppVersionService;
import com.appinfo.tools.Constants;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/26 9:50
 */
@Controller
@RequestMapping("/dev/ver")
public class AppVersionController {
    private Logger logger = Logger.getLogger(AppVersionController.class);

    @Resource
    private AppVersionService appVersionService;

    @Resource
    private AppInfoService appInfoService;

    @RequestMapping(value = "appversionadd.html",method = RequestMethod.GET)
    public String addVersion(@RequestParam(value="id")String appId,
                             @RequestParam(value="error",required= false)String fileUploadError,
                             AppVersion appVersion,Model model){
        logger.debug("fileUploadError============> " + fileUploadError);
        if(null != fileUploadError && fileUploadError.equals("error1")){
            fileUploadError = Constants.FILEUPLOAD_ERROR_1;
        }else if(null != fileUploadError && fileUploadError.equals("error2")){
            fileUploadError	= Constants.FILEUPLOAD_ERROR_2;
        }else if(null != fileUploadError && fileUploadError.equals("error3")){
            fileUploadError = Constants.FILEUPLOAD_ERROR_3;
        }
        appVersion.setAppId(Integer.parseInt(appId));
        List<AppVersion> appVersionList = null;
        try {
            appVersionList = appVersionService.getAppVersionList(Integer.parseInt(appId));
            appVersion.setAppName((appInfoService.getAppInfo(Integer.parseInt(appId),null)).getSoftwareName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("appVersionList", appVersionList);
        model.addAttribute(appVersion);
        model.addAttribute("fileUploadError",fileUploadError);
        return "developer/appversionadd";
    }

    @RequestMapping(value = "/addversionsave",method = RequestMethod.POST)
    public String addVersionSave(AppVersion appVersion, HttpSession session, HttpServletRequest request,
                                 @RequestParam(value="a_downloadLink",required= false) MultipartFile attach ){
        String downloadLink =  null;
        String apkLocPath = null;
        String apkFileName = null;
        if(!attach.isEmpty()){
            String path = request.getSession().getServletContext().getRealPath("statics"+ File.separator+"uploadfiles");
            logger.info("uploadFile path: " + path);
            String oldFileName = attach.getOriginalFilename();//原文件名
            String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
            if(prefix.equalsIgnoreCase("apk")){//apk文件命名：apk名称+版本号+.apk
                String apkName = null;
                try {
                    apkName = appInfoService.getAppInfo(appVersion.getAppId(),null).getAPKName();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                if(apkName == null || "".equals(apkName)){
                    return "redirect:/dev/ver/appversionadd?id="+appVersion.getAppId()
                            +"&error=error1";
                }
                apkFileName = apkName + "-" +appVersion.getVersionNo() + ".apk";
                File targetFile = new File(path,apkFileName);
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }
                try {
                    attach.transferTo(targetFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    return "redirect:/dev/ver/appversionadd?id="+appVersion.getAppId()
                            +"&error=error2";
                }
                downloadLink = request.getContextPath()+"/statics/uploadfiles/"+apkFileName;
                apkLocPath = path+File.separator+apkFileName;
            }else{
                return "redirect:/dev/ver/appversionadd?id="+appVersion.getAppId()
                        +"&error=error3";
            }
        }
        appVersion.setCreatedBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
        appVersion.setCreationDate(new Date());
        appVersion.setDownloadLink(downloadLink);
        appVersion.setApkLocPath(apkLocPath);
        appVersion.setApkFileName(apkFileName);
        try {
            if(appVersionService.add(appVersion)){
                return "redirect:/dev/app/list.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/dev/ver/appversionadd?id="+appVersion.getAppId();
    }

    @RequestMapping(value="/appversionmodify",method=RequestMethod.GET)
    public String modifyAppVersion(@RequestParam("vid") String versionId,
                                   @RequestParam("aid") String appId,
                                   @RequestParam(value="error",required= false)String fileUploadError,
                                   Model model){
        AppVersion appVersion = null;
        List<AppVersion> appVersionList = null;
        if(null != fileUploadError && fileUploadError.equals("error1")){
            fileUploadError = Constants.FILEUPLOAD_ERROR_1;
        }else if(null != fileUploadError && fileUploadError.equals("error2")){
            fileUploadError	= Constants.FILEUPLOAD_ERROR_2;
        }else if(null != fileUploadError && fileUploadError.equals("error3")){
            fileUploadError = Constants.FILEUPLOAD_ERROR_3;
        }
        try {
            appVersion = appVersionService.getAppVersionById(Integer.parseInt(versionId));
            appVersionList = appVersionService.getAppVersionList(Integer.parseInt(appId));
        }catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(appVersion);
        model.addAttribute("appVersionList",appVersionList);
        model.addAttribute("fileUploadError",fileUploadError);
        return "developer/appversionmodify";
    }

    /**
     * 保存修改后的appVersion
     * @param appVersion
     * @param session
     * @return
     */
    @RequestMapping(value="/appversionmodifysave",method=RequestMethod.POST)
    public String modifyAppVersionSave(AppVersion appVersion,HttpSession session,HttpServletRequest request,
                                       @RequestParam(value="attach",required= false) MultipartFile attach){

        String downloadLink =  null;
        String apkLocPath = null;
        String apkFileName = null;
        if(!attach.isEmpty()){
            String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
            logger.info("uploadFile path: " + path);
            String oldFileName = attach.getOriginalFilename();//原文件名
            String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
            if(prefix.equalsIgnoreCase("apk")){//apk文件命名：apk名称+版本号+.apk
                String apkName = null;
                try {
                    apkName = appInfoService.getAppInfo(appVersion.getAppId(),null).getAPKName();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                if(apkName == null || "".equals(apkName)){
                    return "redirect:/dev/ver/appversionmodify?vid="+appVersion.getId()
                            +"&aid="+appVersion.getAppId()
                            +"&error=error1";
                }
                apkFileName = apkName + "-" +appVersion.getVersionNo() + ".apk";
                File targetFile = new File(path,apkFileName);
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }
                try {
                    attach.transferTo(targetFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    return "redirect:/dev/ver/appversionmodify?vid="+appVersion.getId()
                            +"&aid="+appVersion.getAppId()
                            +"&error=error2";
                }
                downloadLink = request.getContextPath()+"/statics/uploadfiles/"+apkFileName;
                apkLocPath = path+File.separator+apkFileName;
            }else{
                return "redirect:/dev/ver/appversionmodify?vid="+appVersion.getId()
                        +"&aid="+appVersion.getAppId()
                        +"&error=error3";
            }
        }
        appVersion.setModifyBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
        appVersion.setModifyDate(new Date());
        appVersion.setDownloadLink(downloadLink);
        appVersion.setApkLocPath(apkLocPath);
        appVersion.setApkFileName(apkFileName);
        try {
            if(appVersionService.modify(appVersion)){
                return "redirect:/dev/app/list.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "developer/appversionmodify";
    }

}
