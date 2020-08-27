package com.kgc.kmall.managerservice.service;

import com.kgc.kmall.bean.*;
import com.kgc.kmall.managerservice.mapper.PmsBaseCatalog1Mapper;
import com.kgc.kmall.managerservice.mapper.PmsBaseCatalog2Mapper;
import com.kgc.kmall.managerservice.mapper.PmsBaseCatalog3Mapper;
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
    @Resource
    PmsBaseCatalog2Mapper pmsBaseCatalog2Mapper;
    @Resource
    PmsBaseCatalog3Mapper pmsBaseCatalog3Mapper;

    @Override
    public List<PmsBaseCatalog1> getCatalog1() {
        return pmsBaseCatalog1Mapper.selectByExample(null);
    }

    @Override
    public List<PmsBaseCatalog2> getCatalog2(Integer catalog1Id) {
        PmsBaseCatalog2Example example=new PmsBaseCatalog2Example();
        PmsBaseCatalog2Example.Criteria criteria = example.createCriteria();
        criteria.andCatalog1IdEqualTo(catalog1Id);
        List<PmsBaseCatalog2> pmsBaseCatalog2s = pmsBaseCatalog2Mapper.selectByExample(example);
        return pmsBaseCatalog2s;
    }

    @Override
    public List<PmsBaseCatalog3> getCatalog3(Long  catalog2Id) {
        PmsBaseCatalog3Example example=new PmsBaseCatalog3Example();
        PmsBaseCatalog3Example.Criteria criteria = example.createCriteria();
        criteria.andCatalog2IdEqualTo(catalog2Id);
        List<PmsBaseCatalog3> pmsBaseCatalog3s = pmsBaseCatalog3Mapper.selectByExample(example);
        return pmsBaseCatalog3s;
    }
}
