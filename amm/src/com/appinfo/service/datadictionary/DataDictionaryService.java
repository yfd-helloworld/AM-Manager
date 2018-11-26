package com.appinfo.service.datadictionary;

import com.appinfo.pojo.DataDictionary;

import java.util.List;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/24 14:05
 */
public interface DataDictionaryService {
    public List<DataDictionary> getDataDictionaryList(String typeCode) throws Exception;
}
