package com.codely.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

//@Configuration
//@EnableWebMvc
//class GlobalCorsConfiguration : WebMvcConfigurer {
//
//    override fun addCorsMappings(registry: CorsRegistry) {
//        registry.addMapping("/**")
//            .allowedOrigins("*")
//            .allowedMethods("GET", "POST", "PUT", "DELETE")
//            .allowedHeaders("*")
//            .exposedHeaders("*")
//            .allowCredentials(true).maxAge(3600)
//    }
//}

//@Configuration
//@EnableWebSecurity
//class WebSecurityConfig {
//
//    @Bean
//    fun filterChain(http: HttpSecurity): SecurityFilterChain {
//        http.cors()
//        return http.build();
//    }
//}
