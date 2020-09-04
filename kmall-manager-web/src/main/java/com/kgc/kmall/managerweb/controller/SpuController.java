package com.kgc.kmall.managerweb.controller;

import com.kgc.kmall.bean.PmsBaseSaleAttr;
import com.kgc.kmall.bean.PmsProductImage;
import com.kgc.kmall.bean.PmsProductInfo;
import com.kgc.kmall.bean.PmsProductSaleAttr;
import com.kgc.kmall.service.SpuService;
import org.apache.commons.io.FilenameUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author shkstart
 * @create 2020-08-28 14:12
 */
@CrossOrigin
@RestController
public class SpuController {


    @Reference
    SpuService spuService;

    @Value("${fileServer.url}")
    String fileUrl;


    @RequestMapping("/spuList")
    public List<PmsProductInfo> spuList(Integer catalog3Id) {
        List<PmsProductInfo> pmsProductInfos = spuService.spuList(catalog3Id);
        return pmsProductInfos;
    }


    @RequestMapping("/fileUpload")
    public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException, MyException {
        /*//文件上传
        //返回文件上传后的路径
        return "https://m.360buyimg.com/babel/jfs/t5137/20/1794970752/352145/d56e4e94/591417dcN4fe5ef33.jpg";*/


        //文件上传
        //返回文件上传后的路径
      /*
        if(file!=null){
            System.out.println("multipartFile = " + file.getName()+"|"+file.getSize());*/

        String configFile = this.getClass().getResource("/tracker.conf").getFile();
        ClientGlobal.init(configFile);
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        //源文件名
        String filename = file.getOriginalFilename();
        //新的文件名称
        String extName = FilenameUtils.getExtension(filename);

        String[] upload_file = storageClient.upload_file(file.getBytes(), extName, null);
//            String imgUrl="http://192.168.183.135";
        String imgUrl = fileUrl;
        for (int i = 0; i < upload_file.length; i++) {
            String path = upload_file[i];
            imgUrl += "/" + path;
        }
        System.out.println(imgUrl);
        return imgUrl;

    }

    @RequestMapping("/baseSaleAttrList")
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        List<PmsBaseSaleAttr> saleAttrList = spuService.baseSaleAttrList();
        return saleAttrList;
    }

    @RequestMapping("/saveSpuInfo")
    public Integer saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo) {
        Integer integer = spuService.saveSpuInfo(pmsProductInfo);
        return integer;
    }

    @RequestMapping("/spuSaleAttrList")
    public  List<PmsProductSaleAttr> spuSaleAttrList(Integer spuId){
        List<PmsProductSaleAttr> pmsProductSaleAttrs=spuService.spuSaleAttrList(spuId);
        return pmsProductSaleAttrs;
    }

    @RequestMapping("/spuImageList")
    public List<PmsProductImage> spuImageList(Long spuId){
        List<PmsProductImage>   pmsProductImages=spuService.spuImageList(spuId);
        return pmsProductImages;
    }
}
