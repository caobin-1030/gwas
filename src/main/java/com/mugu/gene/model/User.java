package com.mugu.gene.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author : zp
 * @Description :
 * @Date : 2020/5/12
 */
@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;
    @Column(name = "code")
    private String code;
}
