package com.naturemobility.seoul.config;

import com.zaxxer.hikari.HikariConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DBConfig {
    @Autowired
    GlobalPropertyConfig globalPropertyConfig;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari") //다음의 prefix로 시작하는 설정을 이용해서 hikariCP의 설정 파일을 만듦
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Bean
    @Primary
    public DataSource customDataSource() { // 위에서 만든 설정 파일을 이용해서 디비와 연결하는 데이터 소스를 생성
        return DataSourceBuilder
                .create()
                .url(globalPropertyConfig.getUrl())
                .driverClassName(globalPropertyConfig.getDriverClassName())
                .username(globalPropertyConfig.getUsername())
                .password(globalPropertyConfig.getPassword())
                .build();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception { //DataSource를 참조하여 MyBatis와 Mysql 서버를 연동시켜준다.
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(customDataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factoryBean.setMapperLocations(resolver.getResources("classpath:/mapper/**/*Mapper.xml"));
        //Mapper 파일 위치를 설정. [classpath]: resource 폴더 의미, [/mapper/**/]: mapp 폴더 밑의 모든 폴더를 의미, [*Mapper.xml]: 이름이 Mapper로 끝나고 확장자가 xml인 모든 파일을 의미

        factoryBean.setTypeAliasesPackage("Nature_Mobility.Whole_In_One.Backend.domain");
        factoryBean.setConfiguration(mybatisConfig()); //Mybatis의 설정파일의 위치를 참조
        return factoryBean.getObject();
    }

    @Bean
    @ConfigurationProperties(prefix = "mybatis.configuration")
    public org.apache.ibatis.session.Configuration mybatisConfig() {
        return new org.apache.ibatis.session.Configuration();
    }

    @Bean
    public SqlSessionTemplate sqlSession() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        return new DataSourceTransactionManager(customDataSource());
    }
}