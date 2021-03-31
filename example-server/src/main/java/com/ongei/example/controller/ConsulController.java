package com.ongei.example.controller;

import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/consul")
public class ConsulController {
    private static final Logger logger = LoggerFactory.getLogger(ConsulController.class);

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @RequestMapping("/list")
    public Object getConsulList(String serverName) {
        final List<ServiceInstance> instances = discoveryClient.getInstances(serverName);
        logger.info("搜索：[{}] 服务实例结果：{}", serverName, JSONUtil.toJsonStr(instances));
        return instances;
//        final ServiceInstance choose = loadBalancerClient.choose(serverName);
//        return choose;
    }
}
