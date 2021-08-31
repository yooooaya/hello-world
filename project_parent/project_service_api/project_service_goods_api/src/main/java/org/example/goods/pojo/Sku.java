package org.example.goods.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * sku实体类
 *
 */
@Data
@Table(name="tb_sku")
public class Sku implements Serializable {
	@Id
	private String id;
	private String sn;
	private String name;
	private Integer price;
	private Integer num;
	private Integer alertNum;
	private String image;
	private String images;
	private Integer weight;
	private Date createTime;
	private Date updateTime;
	private String spuId;
	private Integer categoryId;
	private String categoryName;
	private String brandName;
	private String spec;
	private Integer saleNum;
	private Integer commentNum;
	private Integer status;
	private Integer version;

}
