package com.hzf.auth.models.system;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "sys_api")
public class Api {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String path;

    private String method;

    private String controllerClass;

    private String methodName;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "sys_permission")
    @OrderBy("roleSort ASC")
    private List<Role> roles;

}
