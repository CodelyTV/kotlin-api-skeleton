package com.codely.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

//
//class GlobalCorsConfiguration : WebMvcConfigurer {
//
//    override fun addCorsMappings(registry: CorsRegistry) {
//        registry.addMapping("/**")
//            .allowedOrigins("*")
//            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//            .allowedHeaders("*")
//            .exposedHeaders("*")
//            .allowCredentials(true).maxAge(3600)
//    }
//}
