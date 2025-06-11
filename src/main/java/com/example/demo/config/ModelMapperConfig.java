package com.example.demo.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  // Springboot啟動前會先執行此配置
public class ModelMapperConfig {
	
	/* Springboot 會自動建立此物件
	 * 其他城市可以透過@Autowired 來取得該實體物件
	 * */	
	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
