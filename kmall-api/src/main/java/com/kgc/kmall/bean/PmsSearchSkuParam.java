package com.kgc.kmall.bean;

import java.io.Serializable;

/**
 * @author shkstart
 * @create 2020-09-21 17:26
 */
public class PmsSearchSkuParam implements Serializable {
    //根据关键字查询
    private String keyword;
    //根据三级分类查询
    private String catalog3Id;
    //根据平台属性id查询
//    private List<PmsSkuAttrValue> skuAttrValueList;

    private String[] valueId;

    public String[] getValueId() {
        return valueId;
    }

    public void setValueId(String[] valueId) {
        this.valueId = valueId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCatalog3Id() {
        return catalog3Id;
    }

    public void setCatalog3Id(String catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

  /*  public List<PmsSkuAttrValue> getSkuAttrValueList() {
        return skuAttrValueList;
    }

    public void setSkuAttrValueList(List<PmsSkuAttrValue> skuAttrValueList) {
        this.skuAttrValueList = skuAttrValueList;
    }*/
}
