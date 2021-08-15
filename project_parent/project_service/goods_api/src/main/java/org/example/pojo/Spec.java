package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="tb_spec")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Spec {
    @Id
    private Integer id;
    private String name;
    private String options;
    private Integer seq;
    private Integer template_id;
}
