package com.miniblog.viewcount.config;


import com.miniblog.viewcount.repository.BaseRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.miniblog.viewcount.repository",
        repositoryBaseClass = BaseRepositoryImpl.class
)
public class JpaConfig {
}
