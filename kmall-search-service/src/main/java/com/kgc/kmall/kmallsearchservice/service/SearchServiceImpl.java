package com.kgc.kmall.kmallsearchservice.service;

import com.kgc.kmall.bean.PmsSearchSkuInfo;
import com.kgc.kmall.bean.PmsSearchSkuParam;
import com.kgc.kmall.service.SearchService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.dubbo.config.annotation.Service;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author shkstart
 * @create 2020-09-21 17:29
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Resource
    JestClient jestClient;

    @Override
    public List<PmsSearchSkuInfo> list(PmsSearchSkuParam pmsSearchSkuParam) {
        List<PmsSearchSkuInfo> pmsSearchSkuInfos=new ArrayList<>();
        String catalog3Id = pmsSearchSkuParam.getCatalog3Id();
        String keyword = pmsSearchSkuParam.getKeyword();
        String[] skuAttrValueList = pmsSearchSkuParam.getValueId();

        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();

        if (keyword!=null&&keyword.isEmpty()==false){
            MatchQueryBuilder matchQueryBuilder=new MatchQueryBuilder("skuName",keyword);
            boolQueryBuilder.must(matchQueryBuilder);
        }

        if (catalog3Id!=null){
            TermQueryBuilder termQueryBuilder=new TermQueryBuilder("skuAttrValueList.valueId",catalog3Id);
            boolQueryBuilder.filter(termQueryBuilder);
        }

        /*if (skuAttrValueList!=null&&skuAttrValueList.length>0){
            for (String valueId : skuAttrValueList) {
                TermQueryBuilder termQueryBuilder=new TermQueryBuilder("skuAttrValueList.valueId",valueId);
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }*/
        if(skuAttrValueList!=null&&skuAttrValueList.length>0){
            List<Long> list=new ArrayList<>();
            for (String pmsSkuAttrValue : skuAttrValueList) {
                list.add(Long.parseLong(pmsSkuAttrValue));
            }
            TermsQueryBuilder termsQueryBuilder=new TermsQueryBuilder("skuAttrValueList.valueId",list);
            boolQueryBuilder.filter(termsQueryBuilder);
        }


        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.sort("id", SortOrder.DESC);



        //高亮
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        highlightBuilder.field("skuName");
        highlightBuilder.preTags("<span style='color:red;'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);

       // String dsl = searchSourceBuilder.toString();
//        System.out.println("语句"+dsl);
        Search search=new Search.Builder(searchSourceBuilder.toString()).addIndex("kmall").addType("PmsSkuInfo").build();
//        System.out.println(search);
        try {
            SearchResult searchResult = jestClient.execute(search);
//            System.out.println(searchResult);
            List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = searchResult.getHits(PmsSearchSkuInfo.class);
            for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
                PmsSearchSkuInfo pmsSearchSkuInfo = hit.source;

                //获取高亮数据
                Map<String, List<String>> highlight = hit.highlight;
                if(highlight!=null){
                    String skuName = highlight.get("skuName").get(0);
                    pmsSearchSkuInfo.setSkuName(skuName);
                }


                pmsSearchSkuInfos.add(pmsSearchSkuInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pmsSearchSkuInfos;

    }
}
