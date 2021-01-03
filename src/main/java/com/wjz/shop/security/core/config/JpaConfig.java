package com.wjz.shop.security.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.wjz.shop.security.repository")
public class JpaConfig {
}
