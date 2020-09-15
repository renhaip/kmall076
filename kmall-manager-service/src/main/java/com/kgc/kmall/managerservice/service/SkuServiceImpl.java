package com.kgc.kmall.managerservice.service;

import com.kgc.kmall.bean.PmsSkuAttrValue;
import com.kgc.kmall.bean.PmsSkuImage;
import com.kgc.kmall.bean.PmsSkuInfo;
import com.kgc.kmall.bean.PmsSkuSaleAttrValue;
import com.kgc.kmall.managerservice.mapper.PmsSkuAttrValueMapper;
import com.kgc.kmall.managerservice.mapper.PmsSkuImageMapper;
import com.kgc.kmall.managerservice.mapper.PmsSkuInfoMapper;
import com.kgc.kmall.managerservice.mapper.PmsSkuSaleAttrValueMapper;
import com.kgc.kmall.service.SkuService;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shkstart
 * @create 2020-09-04 15:42
 */
@Service
public class SkuServiceImpl implements SkuService {


    @Resource
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Resource
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Resource
    PmsSkuImageMapper pmsSkuImageMapper;

    @Resource
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Override
    public Integer saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        //保存sku信息 得到skuid
        int insert = pmsSkuInfoMapper.insert(pmsSkuInfo);
        Long pmsSkuInfoId = pmsSkuInfo.getId();

        //保存sku各种id
        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
            pmsSkuAttrValue.setSkuId(pmsSkuInfoId);
            pmsSkuAttrValueMapper.insert(pmsSkuAttrValue);
        }

        //保存sku图片
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : skuImageList) {
            pmsSkuImage.setSkuId(pmsSkuInfoId);
            pmsSkuImageMapper.insert(pmsSkuImage);
        }

        //保存属性值

        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
            pmsSkuSaleAttrValue.setSkuId(pmsSkuInfoId);
            pmsSkuSaleAttrValueMapper.insert(pmsSkuSaleAttrValue);
        }

        return insert;
    }

    @Override
    public PmsSkuInfo selectBySkuId(Long skuId) {
        PmsSkuInfo pmsSkuInfo = pmsSkuInfoMapper.selectByPrimaryKey(skuId);
        return pmsSkuInfo;
    }

    @Override
    public List<PmsSkuInfo> selectBySpuId(Long spuId) {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectBySpuId(spuId);
        return pmsSkuInfos;
    }
}
