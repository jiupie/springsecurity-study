package com.wl.demo2.web;

import com.wl.demo2.daomain.DeploymentPOJO;
import com.wl.demo2.daomain.ProcessDefinitionPOJO;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloController {
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @GetMapping("/hello")
    public String hello(String name) {
        repositoryService.createDeployment().addClasspathResource("processes/holiday.bpmn")
                .name("假期测试").deploy();
        return name;
    }

    /**
     * 获取部署的流程
     *
     * @return
     */
    @GetMapping("/getDeployment")
    public ResponseEntity<List<DeploymentPOJO>> getDeployment() {
        List<Deployment> list = repositoryService.createDeploymentQuery().list();
        List<DeploymentPOJO> data = new ArrayList<>();
        DeploymentPOJO deploymentPOJO = null;
        for (Deployment deployment : list) {
            deploymentPOJO = new DeploymentPOJO();
            deploymentPOJO.setName(deployment.getName());
            deploymentPOJO.setDeploymentTime(deployment.getDeploymentTime());
            deploymentPOJO.setVersion(deployment.getVersion());
            deploymentPOJO.setId(deployment.getId());
            data.add(deploymentPOJO);
        }
        return ResponseEntity.ok(data);
    }

    /**
     * 获取流程定义
     *
     * @return
     */
    @GetMapping("/getProcessDefinition")
    public ResponseEntity<List<ProcessDefinitionPOJO>> getProcessDefinition() {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        List<ProcessDefinition> list = processDefinitionQuery.list();
        ProcessDefinitionPOJO processDefinitionPOJO = null;
        ArrayList<ProcessDefinitionPOJO> processDefinitionPOJOS = new ArrayList<>();
        for (ProcessDefinition processDefinition : list) {
            processDefinitionPOJO = new ProcessDefinitionPOJO();
            processDefinitionPOJO.setDefinitionId(processDefinition.getId());
            processDefinitionPOJO.setName(processDefinition.getName());
            processDefinitionPOJO.setResourceName(processDefinition.getResourceName());
            processDefinitionPOJO.setVersion(processDefinition.getVersion());
            processDefinitionPOJO.setKey(processDefinition.getKey());
            processDefinitionPOJOS.add(processDefinitionPOJO);
        }
        return ResponseEntity.ok(processDefinitionPOJOS);
    }
}
