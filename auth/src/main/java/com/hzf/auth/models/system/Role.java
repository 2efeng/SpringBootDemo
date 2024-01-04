package com.hzf.auth.models.system;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "sys_role")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String roleName;
    private String description;
    private int roleSort;

    private boolean isDelete;
    private long createAt;
    private long updateAt;
    @Column(updatable = false, insertable = false, columnDefinition = "DATETIME DEFAULT NOW()")
    private Date createTime;
    @Column(insertable = false, columnDefinition = "DATETIME DEFAULT NOW()")
    private Date updateTime;

}
