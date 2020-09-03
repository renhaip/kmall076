package com.kgc.kmall.managerservice.service;

import com.kgc.kmall.bean.*;
import com.kgc.kmall.managerservice.mapper.*;
import com.kgc.kmall.service.SpuService;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shkstart
 * @create 2020-08-28 14:10
 */
@Service
public class SpuServiceImpl implements SpuService {

    @Resource
    PmsProductInfoMapper pmsProductInfoMapper;

    @Resource
    PmsBaseSaleAttrMapper baseSaleAttrMapper;

    @Resource
    PmsProductImageMapper pmsProductImageMapper;

    @Resource
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Resource
    pmsProductSaleAttrValueMapper  productSaleAttrValueMapper;

    @Override
    public List<PmsProductInfo> spuList(Integer catalog3Id) {
        PmsProductInfoExample example=new PmsProductInfoExample();
        PmsProductInfoExample.Criteria criteria = example.createCriteria();
        criteria.andCatalog3IdEqualTo(catalog3Id.longValue());
        List<PmsProductInfo> pmsProductInfos = pmsProductInfoMapper.selectByExample(example);
        return pmsProductInfos;
    }

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        return baseSaleAttrMapper.selectByExample(null);
    }

    @Override
    public Integer saveSpuInfo(PmsProductInfo pmsProductInfo) {
        //保存spu信息,得到spu的id
        int insert = pmsProductInfoMapper.insert(pmsProductInfo);
        Long ProductInfoId = pmsProductInfo.getId();

        //保存图片
        List<PmsProductImage> spuImageList = pmsProductInfo.getSpuImageList();
        if(spuImageList!=null){
            for (PmsProductImage pmsProductImage : spuImageList) {
                pmsProductImage.setProductId(ProductInfoId);
                pmsProductImageMapper.insert(pmsProductImage);
            }
        }

        //保存销售属性 ,保存销售属性值
        List<PmsProductSaleAttr> spuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        if(spuSaleAttrList!=null){
            for (PmsProductSaleAttr pmsProductSaleAttr : spuSaleAttrList) {
                pmsProductSaleAttr.setProductId(ProductInfoId);
                pmsProductSaleAttrMapper.insert(pmsProductSaleAttr);

                for (pmsProductSaleAttrValue pmsProductSaleAttrValue : pmsProductSaleAttr.getSpuSaleAttrValueList()) {
                    pmsProductSaleAttrValue.setProductId(ProductInfoId);
                    productSaleAttrValueMapper.insert(pmsProductSaleAttrValue);
                }
            }
        }


        return insert;
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(Integer spuId) {
        //查询销售属性值颜色  (颜色  黑色 白色 红色)  版本(64  128  256)
        PmsProductSaleAttrExample example=new PmsProductSaleAttrExample();
        PmsProductSaleAttrExample.Criteria criteria = example.createCriteria();
        criteria.andProductIdEqualTo(spuId.longValue());
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.selectByExample(example);
        for (PmsProductSaleAttr pmsProductSaleAttr : pmsProductSaleAttrs) {
            //根据spuid和销售属性id查询销售属性值
            pmsProductSaleAttrValueExample example1=new pmsProductSaleAttrValueExample();
            PmsProductSaleAttrExample.Criteria criteria1 = example.createCriteria();
            criteria1.andProductIdEqualTo(spuId.longValue());
            criteria1.andSaleAttrIdEqualTo(pmsProductSaleAttr.getSaleAttrId());
            List<pmsProductSaleAttrValue> pmsProductSaleAttrValues = productSaleAttrValueMapper.selectByExample(example1);

            pmsProductSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValues);
        }

        return pmsProductSaleAttrs;
    }
}
