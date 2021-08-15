package org.example.service;

import com.alibaba.fastjson.JSON;
import org.example.dao.*;
import org.example.pojo.*;
import org.example.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private SkuDao skuDao;

    @Autowired
    private SpuDao spuDao;

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private CategoryBrandDao categoryBrandDao;
    @Override
    public void add(Goods goods) {
        Spu spu2=new Spu();
        spu2.setId("12");
        spuDao.insert(spu2);

        Spu spu= goods.getSpu();
        long spuId= idWorker.nextId();
        spu.setId(String.valueOf(spuId));

        spu.setIs_delete(0);
        spu.setIs_marketable(0);
        spu.setIs_enable_spec(0);
        System.out.println(spu);
        spuDao.insert(spu);
        saveSkuList(goods);
    }
    /**
     * 保存sku列表
     * @param goods
     */
    private void saveSkuList(Goods goods){
        //获取spu对象
        Spu spu = goods.getSpu();
        //当前日期
        Date date = new Date();
        //获取品牌对象
        Brand brand = brandDao.selectByPrimaryKey(spu.getBrand_id());

        //获取分类对象
        Category category = categoryDao.selectByPrimaryKey(spu.getCategory3_id());
        /**
         * 添加分类与品牌之间的关联
         */
        CategoryBrand categoryBrand = new CategoryBrand();
        categoryBrand.setBrandId(spu.getBrand_id());
        categoryBrand.setCategoryId(spu.getCategory3_id());
        int count = categoryBrandDao.selectCount(categoryBrand);
        //判断是否有这个品牌和分类的关系数据
        if(count == 0) {
            //如果没有关系数据则添加品牌和分类关系数据
            categoryBrandDao.insert(categoryBrand);
        }

        //获取sku集合对象
        List<Sku> skuList = goods.getSkuList();
        if (skuList != null) {
            for (Sku sku : skuList) {
                //设置sku主键ID
                sku.setId(String.valueOf(idWorker.nextId()));
                //设置sku规格
                if (sku.getSpec() == null || "".equals(sku.getSpec())) {
                    sku.setSpec("{}");
                }
                //设置sku名称(商品名称 + 规格)
                String name = spu.getName();
                //将规格json字符串转换成Map
                Map<String, String> specMap = JSON.parseObject(sku.getSpec(), Map.class);
                if (specMap != null && specMap.size() > 0) {
                    for(String value : specMap.values()){
                        name += " "+ value;
                    }
                }

                sku.setName(name);//名称
                sku.setSpu_id(spu.getId());//设置spu的ID
                sku.setCreate_time(date);//创建日期
                sku.setUpdate_time(date);//修改日期
                sku.setCategory_id(category.getId());//商品分类ID
                sku.setCategory_name(category.getName());//商品分类名称
                System.out.println(spu);
                sku.setBrand_name(brand.getName());//品牌名称
                System.out.println(sku);
                skuDao.insert(sku);//插入sku表数据

            }
        }
    }
    public List<Spu> findSpu(){
        return spuDao.selectAll();
    }
    public Goods findGoodById(String key){
        Spu spu=spuDao.selectByPrimaryKey(key);
        //查询SKU 列表
        Example example=new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spu_id",key);
        List<Sku> skuList = skuDao.selectByExample(example);
        return new Goods(spu,skuList);
    }
    @Override
    public void update(Goods goods ) {
        //取出spu部分
        Spu spu = goods.getSpu();
        spuDao.updateByPrimaryKey(spu);
        //删除原sku列表
        Example example=new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spu_id",spu.getId());
        skuDao.deleteByExample(example);
        saveSkuList(goods);//保存sku列表
    }
    @Transactional
    public void audit(String id) {
        //查询spu对象
        Spu spu = spuDao.selectByPrimaryKey(id);
        if (spu == null){
            throw new RuntimeException("当前商品不存在");
        }
        //判断当前spu是否处于删除状态
        if (1==spu.getIs_delete()){
            throw new RuntimeException("当前商品处于删除状态");
        }
        //不处于删除状态,修改审核状态为1,上下架状态为1
        spu.setStatus(1);
        spu.setIs_marketable(1);
        //执行修改操作
        spuDao.updateByPrimaryKeySelective(spu);
    }
    @Transactional
    public void pull(String id) {
        //查询spu
        Spu spu = spuDao.selectByPrimaryKey(id);
        if (spu == null){
            throw new RuntimeException("当前商品不存在");
        }
        //判断当前商品是否处于删除状态
        if (1==spu.getIs_delete()){
            throw new RuntimeException("当前商品处于删除状态");
        }
        //商品处于未删除状态的话,则修改上下架状态为已下架(0)
        spu.setIs_marketable(0);
        spuDao.updateByPrimaryKeySelective(spu);
    }
    /**
     * 上架商品
     * @param id
     */
    @Override
    public void put(String id) {
        Spu spu = spuDao.selectByPrimaryKey(id);
        if(spu.getStatus()!=1){
            throw new RuntimeException("未通过审核的商品不能上架！");
        }
        spu.setIs_marketable(1);//上架状态
        spuDao.updateByPrimaryKeySelective(spu);
    }
    @Override
    public void delete(String id){
        Spu spu = spuDao.selectByPrimaryKey(id);
        //检查是否下架的商品
        if(spu.getIs_marketable()!=0){
            throw new RuntimeException("必须先下架再删除！");
        }
        spu.setIs_delete(1);//删除
        spu.setStatus(1);//未审核
        spuDao.updateByPrimaryKeySelective(spu);
    }
    @Override
    public void restore(String id) {
        Spu spu = spuDao.selectByPrimaryKey(id);
        //检查是否删除的商品
        if(spu.getIs_delete()!=1){
            throw new RuntimeException("此商品未删除！");
        }
        spu.setIs_delete(0);//未删除
        spu.setStatus(0);//未审核
        spuDao.updateByPrimaryKeySelective(spu);
    }
    @Override
    public void realDelete(String id) {
        Spu spu = spuDao.selectByPrimaryKey(id);
        //检查是否删除的商品
        if(spu.getIs_delete()!=1){
            throw new RuntimeException("此商品未删除！");
        }
        spuDao.deleteByPrimaryKey(id);

    }
}
