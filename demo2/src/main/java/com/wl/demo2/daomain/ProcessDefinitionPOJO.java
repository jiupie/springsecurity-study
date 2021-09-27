package com.wl.demo2.daomain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProcessDefinitionPOJO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String  name;
    private String  key;
    private String  resourceName;
    private String  definitionId;
    private Integer  version;

}
