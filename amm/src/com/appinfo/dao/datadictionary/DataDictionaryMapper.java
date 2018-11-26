package com.appinfo.dao.datadictionary;

import com.appinfo.pojo.DataDictionary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/24 14:00
 */
public interface DataDictionaryMapper {
    public List<DataDictionary> getDataDictionaryList(@Param("typeCode") String typeCode) throws Exception;
}
