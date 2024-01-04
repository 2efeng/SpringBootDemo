package com.hzf.auth.models.system;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@Entity
@Table(name = "sys_user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String password;
    private String nickname;
    private String phone;
    private String email;
    private byte status;
    private byte gender;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "sys_user_role")
    @OrderBy("roleSort ASC")
    private List<Role> roles;

    private boolean isDelete;
    private long createAt;
    private long updateAt;
    @Column(updatable = false, insertable = false, columnDefinition = "DATETIME DEFAULT NOW()")
    private Date createTime;
    @Column(insertable = false, columnDefinition = "DATETIME DEFAULT NOW()")
    private Date updateTime;
}
