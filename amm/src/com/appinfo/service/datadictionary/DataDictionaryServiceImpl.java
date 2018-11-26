package com.appinfo.service.datadictionary;

import com.appinfo.dao.datadictionary.DataDictionaryMapper;
import com.appinfo.pojo.DataDictionary;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/24 14:06
 */
@Service
public class DataDictionaryServiceImpl implements DataDictionaryService {

    @Resource
    private DataDictionaryMapper dataDictionaryMapper;

    @Override
    public List<DataDictionary> getDataDictionaryList(String typeCode) throws Exception {
        return dataDictionaryMapper.getDataDictionaryList(typeCode);
    }
}
