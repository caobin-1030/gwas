package com.mugu.gene.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author : zp
 * @Description :
 * @Date : 2020/5/14
 */
@Data
@Entity
@Table(name = "user_run")
public class UserRun {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "cmd")
    private String cmd;
    @Column(name = "title")
    private String title;
    @Column(name = "email")
    private String email;
    @Column(name = "cmd_md5")
    private String cmdMd5;
    @Column(name = "stauts")
    private Integer stauts;
    @Column(name = "uuid")
    private String uuid;
    @Column(name = "start_time")
    private String startTime;
    @Column(name = "end_time")
    private String endTime;
    @Column(name = "path")
    private String path;
    @Column(name = "log")
    private String log;
    @Column(name = "is_file")
    private Integer isFile;

    public UserRun(String cmd, String email, String cmdMd5, Integer stauts, String uuid, String startTime, String endTime, String path, String log) {
        this.cmd = cmd;
        this.email = email;
        this.cmdMd5 = cmdMd5;
        this.stauts = stauts;
        this.uuid = uuid;
        this.startTime = startTime;
        this.endTime = endTime;
        this.path = path;
        this.log = log;
    }

    public UserRun(String email, Integer stauts, String startTime, String path,String uuid) {
        this.email = email;
        this.stauts = stauts;
        this.startTime = startTime;
        this.path = path;
        this.uuid = uuid;
    }

    public UserRun() {
    }
}
