package org.example.service;

import com.alibaba.fastjson.JSON;
import org.example.dao.*;
import org.example.goods.pojo.*;
import org.example.pojo.OrderItem;
import org.example.pojo.Result;
import org.example.pojo.StatusCode;
import org.example.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SkuServiceImpl implements SkuService {
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

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void add(Goods goods) {
        Spu spu2=new Spu();
        spu2.setId("12");
        spuDao.insert(spu2);

        Spu spu= goods.getSpu();
        long spu_id= idWorker.nextId();
        spu.setId(String.valueOf(spu_id));

        spu.setIsDelete(0);
        spu.setIsMarketable(0);
        spu.setIsEnableSpec(0);
        System.out.println(spu);
        spuDao.insert(spu);
        saveSkuList(goods);
    }

    @Override
    public Sku findById(String id) {
        return skuDao.selectByPrimaryKey(id);
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
        Brand brand = brandDao.selectByPrimaryKey(spu.getBrandId());

        //获取分类对象
        Category category = categoryDao.selectByPrimaryKey(spu.getCategory3Id());
        /**
         * 添加分类与品牌之间的关联
         */
        CategoryBrand categoryBrand = new CategoryBrand();
        categoryBrand.setBrandId(spu.getBrandId());
        categoryBrand.setCategoryId(spu.getCategory3Id());
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
                sku.setSpuId(spu.getId());//设置spu的ID
                sku.setCreateTime(date);//创建日期
                sku.setUpdateTime(date);//修改日期
                sku.setCategoryId(category.getId());//商品分类ID
                sku.setCategoryName(category.getName());//商品分类名称
                System.out.println(spu);
                sku.setBrandName(brand.getName());//品牌名称
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
        criteria.andEqualTo("spuId",key);
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
        criteria.andEqualTo("spuId",spu.getId());
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
        if (1==spu.getIsDelete()){
            throw new RuntimeException("当前商品处于删除状态");
        }
        //不处于删除状态,修改审核状态为1,上下架状态为1
        spu.setStatus(1);
        spu.setIsMarketable(1);
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
        if (1==spu.getIsDelete()){
            throw new RuntimeException("当前商品处于删除状态");
        }
        //商品处于未删除状态的话,则修改上下架状态为已下架(0)
        spu.setIsMarketable(0);
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
        spu.setIsMarketable(1);//上架状态
        spuDao.updateByPrimaryKeySelective(spu);
    }
    @Override
    public void delete(String id){
        Spu spu = spuDao.selectByPrimaryKey(id);
        //检查是否下架的商品
        if(spu.getIsMarketable()!=0){
            throw new RuntimeException("必须先下架再删除！");
        }
        spu.setIsDelete(1);//删除
        spu.setStatus(1);//未审核
        spuDao.updateByPrimaryKeySelective(spu);
    }
    @Override
    public void restore(String id) {
        Spu spu = spuDao.selectByPrimaryKey(id);
        //检查是否删除的商品
        if(spu.getIsDelete()!=1){
            throw new RuntimeException("此商品未删除！");
        }
        spu.setIsDelete(0);//未删除
        spu.setStatus(0);//未审核
        spuDao.updateByPrimaryKeySelective(spu);
    }
    @Override
    public void realDelete(String id) {
        Spu spu = spuDao.selectByPrimaryKey(id);
        //检查是否删除的商品
        if(spu.getIsDelete()!=1){
            throw new RuntimeException("此商品未删除！");
        }
        spuDao.deleteByPrimaryKey(id);

    }
    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    @Override
    public List<Sku> findList(Map<String, Object> searchMap){
        System.out.println(searchMap.get("spuId"));
        Example example = createExample(searchMap);
        return skuDao.selectByExample(example);
    }

    @Override
    public boolean decrCount(String username) {
        //获取购物车数据
        List<OrderItem> orderItems = redisTemplate.boundHashOps("Cart_" + username).values();
        //循环递减
        for (OrderItem orderItem : orderItems) {
            //递减库存
            int count = skuDao.decrCount(orderItem);
            if (count <= 0) {
                return false;
            }

        }
        return true;
    }

    /**
     * 构建查询对象
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 商品id
            if(searchMap.get("id")!=null && !"".equals(searchMap.get("id"))){
                criteria.andEqualTo("id", searchMap.get("id"));
            }
            // 商品条码
            if(searchMap.get("sn")!=null && !"".equals(searchMap.get("sn"))){
                criteria.andEqualTo("sn",searchMap.get("sn"));
            }
            // SKU名称
            if(searchMap.get("name")!=null && !"".equals(searchMap.get("name"))){
                criteria.andLike("name","%"+searchMap.get("name")+"%");
            }
            // 商品图片
            if(searchMap.get("image")!=null && !"".equals(searchMap.get("image"))){
                criteria.andLike("image","%"+searchMap.get("image")+"%");
            }
            // 商品图片列表
            if(searchMap.get("images")!=null && !"".equals(searchMap.get("images"))){
                criteria.andLike("images","%"+searchMap.get("images")+"%");
            }
            // SPUID
            if(searchMap.get("spuId")!=null && !"".equals(searchMap.get("spuId"))){
                criteria.andEqualTo("spuId",searchMap.get("spuId"));
            }
            // 类目名称
            if(searchMap.get("categoryName")!=null && !"".equals(searchMap.get("categoryName"))){
                criteria.andLike("categoryName","%"+searchMap.get("categoryName")+"%");
            }
            // 品牌名称
            if(searchMap.get("brandName")!=null && !"".equals(searchMap.get("brandName"))){
                criteria.andLike("brandName","%"+searchMap.get("brandName")+"%");
            }
            // 规格
            if(searchMap.get("spec")!=null && !"".equals(searchMap.get("spec"))){
                criteria.andLike("spec","%"+searchMap.get("spec")+"%");
            }
            // 商品状态 1-正常，2-下架，3-删除
            if(searchMap.get("status")!=null && !"".equals(searchMap.get("status"))){
                criteria.andEqualTo("status", searchMap.get("status"));
            }

            // 价格（分）
            if(searchMap.get("price")!=null ){
                criteria.andEqualTo("price",searchMap.get("price"));
            }
            // 库存数量
            if(searchMap.get("num")!=null ){
                criteria.andEqualTo("num",searchMap.get("num"));
            }
            // 库存预警数量
            if(searchMap.get("alertNum")!=null ){
                criteria.andEqualTo("alertNum",searchMap.get("alertNum"));
            }
            // 重量（克）
            if(searchMap.get("weight")!=null ){
                criteria.andEqualTo("weight",searchMap.get("weight"));
            }
            // 类目ID
            if(searchMap.get("categoryId")!=null ){
                criteria.andEqualTo("categoryId",searchMap.get("categoryId"));
            }
            // 销量
            if(searchMap.get("saleNum")!=null ){
                criteria.andEqualTo("saleNum",searchMap.get("saleNum"));
            }
            // 评论数
            if(searchMap.get("commentNum")!=null ){
                criteria.andEqualTo("commentNum",searchMap.get("commentNum"));
            }

        }
        return example;
    }
}
