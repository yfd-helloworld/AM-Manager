package com.appinfo.controller;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/15 15:59
 */

/**
 * 类型转换基类
 */
public class BaseController {

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        System.out.println("initBinder=================");
        dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }
}
