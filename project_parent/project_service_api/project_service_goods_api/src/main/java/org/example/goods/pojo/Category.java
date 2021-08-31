package org.example.goods.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_category")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Category {
    @Id
    private Integer id;
    private String name;
    private Integer goods_num;
    private Integer isShow;
    private Integer isMenu;
    private Integer seq;
    private Integer parentId;
    private Integer templateId;
}
