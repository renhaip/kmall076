package com.kgc.kmall.managerservice.service;

import com.kgc.kmall.bean.PmsBaseCatalog1;
import com.kgc.kmall.managerservice.mapper.PmsBaseCatalog1Mapper;
import com.kgc.kmall.service.CatalogService;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shkstart
 * @create 2020-08-25 18:36
 */
@Service
public class CatalogServiceImpl implements CatalogService {


    @Resource
    PmsBaseCatalog1Mapper pmsBaseCatalog1Mapper;
    @Override
    public List<PmsBaseCatalog1> getCatalog1() {
        return pmsBaseCatalog1Mapper.selectByExample(null);
    }
}
