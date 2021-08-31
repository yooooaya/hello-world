package org.example.service;

import com.alibaba.fastjson.JSON;
import org.example.goods.feign.CategoryFeign;
import org.example.goods.feign.SkuFeign;
import org.example.goods.feign.SpuFeign;
import org.example.goods.pojo.Category;
import org.example.goods.pojo.Sku;
import org.example.goods.pojo.Spu;
import org.example.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageServiceImpl implements PageService{
    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SpuFeign spuFeign;

    @Autowired
    private CategoryFeign categoryFeign;


    @Value("${pagepath}")
    private String pagepath;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void generateItemPage(String spuId) {
        //获取context对象,用于存放商品详情数据
        Context context=new Context();

        Map<String, Object> itemData = this.findItemData(spuId);
        System.out.println(itemData.get("skuList"));
        System.out.println(itemData);
        context.setVariables(itemData);
        //获取商品详情页生成的指定位置
        File dir = new File(pagepath);
        //判断商品详情页文件夹是否存在,不存在则创建
        if (!dir.exists()){
            dir.mkdirs();
        }
        //定义输出流,进行文件生成
        File file = new File(dir+"/"+spuId+".html");
        Writer out = null;
        try{
            out = new PrintWriter(file);
            //生成文件
            /**
             * 1.模板名称
             * 2.context对象,包含了模板需要的数据
             * 3.输出流,指定文件输出位置
             */
            templateEngine.process("item",context,out);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭流
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private Map<String,Object> findItemData(String spu_id){
        Map<String,Object> resultMap=new HashMap<>();

        //获取spu信息
        Result<Spu> spuResult= spuFeign.findSpuById(spu_id);
        Spu spu= spuResult.getData();
        resultMap.put("spu",spu);

        //获取图片信息
        if (spu != null){
            if(!StringUtils.isEmpty(spu.getImages())){
                resultMap.put("imageList",spu.getImages().split(","));
            }
        }

        //获取分类信息
        Category category1=categoryFeign.findById(spu.getCategory1Id()).getData();
        resultMap.put("category1",category1);
        Category category2=categoryFeign.findById(spu.getCategory2Id()).getData();
        resultMap.put("category2",category2);
        Category category3=categoryFeign.findById(spu.getCategory3Id()).getData();
        resultMap.put("category3",category3);

        //获取sku集合信息
        List<Sku> skuList = skuFeign.findSkuListBySpuId(spu_id);
        System.out.println(skuList);
        resultMap.put("skuList",skuList);
        System.out.println(resultMap.get("skuList"));
        resultMap.put("specificationList", JSON.parseObject(spu.getSpecItems(), Map.class));
        return resultMap;
    }
}
