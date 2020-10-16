package com.kgc.kmall.kmallsearchweb.controller;

import com.kgc.kmall.bean.*;
import com.kgc.kmall.service.AttrService;
import com.kgc.kmall.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * @author shkstart
 * @create 2020-09-21 16:38
 */
@Controller
public class SearchController {


    @Reference
    SearchService searchService;

    @Reference
    AttrService attrService;


//    @LoginRequired(true)
    @RequestMapping("/index.html")
    public String index(){        return "index";}

    /*@RequestMapping("/list.html")
    public String list(String keyword){
        return "list";
    }*/


    @RequestMapping("/list.html")
    public String list(PmsSearchSkuParam pmsSearchSkuParam, Model model){
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = searchService.list(pmsSearchSkuParam);
        model.addAttribute("skuLsInfoList",pmsSearchSkuInfos);

        //获取平台属性valueId
        Set<Long> valueIdSet=new HashSet<>();
        for (PmsSearchSkuInfo  pmsSearchSkuInfo  : pmsSearchSkuInfos) {
            for (PmsSkuAttrValue pmsSkuAttrValue  : pmsSearchSkuInfo.getSkuAttrValueList()) {
                valueIdSet.add(pmsSkuAttrValue.getValueId());
            }
        }
        System.out.println(Arrays.toString(valueIdSet.toArray()));

        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrService.selectAttrInfoValueListByValueId(valueIdSet);

        //已选中的valueId
        String[] valueId=pmsSearchSkuParam.getValueId();


        //封装面包屑数据
        if(valueId!=null&&valueId.length>0){
            List<PmsSearchCrumb> pmsSearchCrumbs=new ArrayList<>();
            for (String s : valueId) {
                PmsSearchCrumb pmsSearchCrumb=new PmsSearchCrumb();
                pmsSearchCrumb.setUrlParam(getUrlParam(pmsSearchSkuParam,s));
                pmsSearchCrumb.setValueId(s);

                String valueName = getValueName(pmsBaseAttrInfos, s);
                pmsSearchCrumb.setValueName(valueName);

                pmsSearchCrumbs.add(pmsSearchCrumb);
            }
            model.addAttribute("attrValueSelectedList",pmsSearchCrumbs);
        }




        if(valueId!=null){
            //利用迭代器排除已选的平台属性,删除集合元素不能使用for循环,因为会出现数组越界
            Iterator<PmsBaseAttrInfo> iterator=pmsBaseAttrInfos.iterator();
            while (iterator.hasNext()){
                PmsBaseAttrInfo next = iterator.next();
                for (PmsBaseAttrValue pmsBaseAttrValue : next.getAttrValueList()) {
                    for (String s : valueId) {
                        if(s.equals(pmsBaseAttrValue.getId().toString())){
                            iterator.remove();
                        }
                    }
                }
            }
        }

        model.addAttribute("attrList",pmsBaseAttrInfos);





        //给前端返回原始url
        StringBuffer buffer=new StringBuffer();
        String keyword = pmsSearchSkuParam.getKeyword();
        String catalog3Id = pmsSearchSkuParam.getCatalog3Id();
                 valueId = pmsSearchSkuParam.getValueId();

        if (StringUtils.isNotBlank(keyword)){
            buffer.append("&keyword="+keyword);
        }
        if(StringUtils.isNotBlank(catalog3Id)){
            buffer.append("&catalog3Id="+catalog3Id);
        }
        if(valueId!=null){
            for (String s : valueId) {
                buffer.append("&valueId="+s);
            }
        }



        String url=buffer.substring(1);
        model.addAttribute("urlParam",url);





        //显示关键字
        model.addAttribute("keyword",pmsSearchSkuParam.getKeyword());
        return "list";
    }

    //根据参数对象拼接url
    public String  getUrlParam(PmsSearchSkuParam pmsSearchSkuParam,String valueId){
        StringBuffer buffer=new StringBuffer();
        String catalog3Id = pmsSearchSkuParam.getCatalog3Id();
        String keyword = pmsSearchSkuParam.getKeyword();
        String[] valueIds = pmsSearchSkuParam.getValueId();
        if (StringUtils.isNotBlank(catalog3Id)){
            buffer.append("&catalog3Id="+catalog3Id);
        }
        if (StringUtils.isNotBlank(keyword)){
            buffer.append("&keyword="+keyword);
        }
        //面包屑的url是不能包括自身的valueId的，因为点击面包屑以后会从当前URL中去除面包屑的valueId
        if (valueIds!=null){
            for (String pmsSkuAttrValue : valueIds) {
                if (valueId.equals(pmsSkuAttrValue)==false)
                    buffer.append("&valueId="+pmsSkuAttrValue);
            }
        }

        return buffer.substring(1);
    }

    //根据valueId查询valueName
    public String getValueName(List<PmsBaseAttrInfo> pmsBaseAttrInfos,String valueId){
        String valueName="";
        for (PmsBaseAttrInfo pmsBaseAttrInfo : pmsBaseAttrInfos) {
            for (PmsBaseAttrValue pmsBaseAttrValue : pmsBaseAttrInfo.getAttrValueList()) {
                if(valueId.equals(pmsBaseAttrValue.getId().toString())){
                    valueName=pmsBaseAttrInfo.getAttrName()+":"+pmsBaseAttrValue.getValueName();
                        return valueName;
                }
            }
        }
        return valueName;
    }
}


