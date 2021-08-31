package org.example.system.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    @Id
    private Integer id;
    private String login_name;
    private String password;
    private Integer status;
}
