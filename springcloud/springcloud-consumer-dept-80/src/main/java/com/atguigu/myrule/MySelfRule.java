package com.atguigu.myrule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.loadbalancer.IRule;

@Configuration
public class MySelfRule{

	@Bean
	public IRule myRule() {
		//return new RandomRule();//随机
		//return new RoundRobinRule();
		return new RandomRule_ZY(); //自定义每台机器访问 5次
	}
}
