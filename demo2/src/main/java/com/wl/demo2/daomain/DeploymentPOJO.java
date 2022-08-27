package com.wl.demo2.daomain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DeploymentPOJO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private Date deploymentTime;
    private Integer version;

}
