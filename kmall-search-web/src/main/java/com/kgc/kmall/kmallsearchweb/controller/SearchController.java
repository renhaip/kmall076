package com.kgc.kmall.kmallsearchweb.controller;

import com.kgc.kmall.bean.PmsSearchSkuInfo;
import com.kgc.kmall.bean.PmsSearchSkuParam;
import com.kgc.kmall.service.SearchService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-09-21 16:38
 */
@Controller
public class SearchController {


    @Reference
    SearchService searchService;

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
        return "list";
    }

}
