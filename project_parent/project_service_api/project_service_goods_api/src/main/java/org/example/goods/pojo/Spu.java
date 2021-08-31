package org.example.goods.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "tb_spu")
public class Spu {
    @Id
    private String id;
    private String sn;
    private String name;
    private String caption;
    private Integer brandId;
    private Integer category1Id;
    private Integer category2Id;
    private Integer category3Id;
    private Integer templateId;
    private Integer freightId;
    private String image;
    private String images;
    private String saleService;
    private String introduction;
    private String specItems;
    private String paraItems;
    private Integer saleNum;
    private Integer commentNum;
    private Integer isMarketable;
    private Integer isEnableSpec;
    private Integer isDelete;
    private Integer status;
}
