package org.example.pojo;

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
    private Integer brand_id;
    private Integer category1_id;
    private Integer category2_id;
    private Integer category3_id;
    private Integer template_id;
    private Integer freight_id;
    private String image;
    private String images;
    private String sale_service;
    private String introduction;
    private String spec_items;
    private String para_items;
    private Integer sale_num;
    private Integer comment_num;
    private Integer is_marketable;
    private Integer is_enable_spec;
    private Integer is_delete;
    private Integer status;
}
