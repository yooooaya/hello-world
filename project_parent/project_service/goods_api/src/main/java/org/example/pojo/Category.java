package org.example.pojo;

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
    private Integer is_show;
    private Integer is_menu;
    private Integer seq;
    private Integer parent_id;
    private Integer template_id;
}
