package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "tb_sku")
public class Sku {
    @Id
    private String id;
    private String sn;
    private String name;
    private Integer price;
    private Integer num;
    private Integer alert_num;
    private String image;
    private String images;
    private Integer weight;
    private Date create_time;
    private Date update_time;
    private String spu_id;
    private Integer category_id;
    private String category_name;
    private String brand_name;
    private String spec;
    private Integer sale_num;
    private Integer comment_num;
    private Integer status;
    private Integer version;
}
