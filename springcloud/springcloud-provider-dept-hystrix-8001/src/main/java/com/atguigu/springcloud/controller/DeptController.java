package com.atguigu.springcloud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.springcloud.entities.Dept;
import com.atguigu.springcloud.service.DeptService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class DeptController {

	@Autowired
	private DeptService service;
	
	@Autowired
	private DiscoveryClient client;
	
	@RequestMapping(value="/dept/add", method=RequestMethod.POST)
	public boolean add(@RequestBody Dept dept) {
		return service.add(dept);
	}
	
	@RequestMapping(value="/dept/get/{id}", method=RequestMethod.GET)
	@HystrixCommand(fallbackMethod="processHystrix_Get")
	public Dept get(@PathVariable Long id) {
		Dept dept = service.get(id);
		if(null == dept) {
			throw new RuntimeException("该ID: " + id + "没有对应 信息.");
		}
		return dept;
	}
	
	/**
	 * @return
	 */
	public Dept processHystrix_Get(@PathVariable("id") Long id) {
		return new Dept().setDeptno(id).setDname("该ID"+id+"没有对应的信息，是null,@HystrixCommand").setDb_source("no this database in Mysql");
	}
	
	@RequestMapping(value="/dept/list", method=RequestMethod.GET)
	public List<Dept> list(){
		return service.list();
	}
	
	@RequestMapping(value="/dept/discovery", method=RequestMethod.GET)
	public Object discovery() {
		List<String> list = client.getServices();
		System.out.println("*********" + list);
		
		List<ServiceInstance> srvList = client.getInstances("springcloud-dept");
		
		srvList.stream().forEach((x)->{
			System.out.println(((ServiceInstance)x).getServiceId() + ((ServiceInstance)x).getHost() + ((ServiceInstance)x).getUri() + ((ServiceInstance)x).getPort());
		});
		return this.client;
	}
}
