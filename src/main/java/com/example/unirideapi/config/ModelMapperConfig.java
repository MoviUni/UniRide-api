package com.example.unirideapi.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        //para utilizar el modelMapper debe agregarse la dependencia en el archivo pom.xml
        return new ModelMapper();
    }
}
