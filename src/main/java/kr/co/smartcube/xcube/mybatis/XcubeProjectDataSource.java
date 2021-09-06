package kr.co.smartcube.xcube.mybatis;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class XcubeProjectDataSource {
 
    @Bean(name="xcubeMybatisHikariConfig")
    @ConfigurationProperties(prefix = "xcube.datasource.hikari")
    public HikariConfig xcubeMybatisHikariConfig() {
        HikariConfig config = new HikariConfig();
        return config;
    }

    @Bean(name="mybatisDataSource")
    public DataSource mybatisDataSource() {
        HikariConfig conf = xcubeMybatisHikariConfig();

        HikariDataSource source = new HikariDataSource(conf);
        return source;
    }
 
    @Bean(name="sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(@Autowired @Qualifier("mybatisDataSource") DataSource dataSource, ApplicationContext applicationContext)
            throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
        factoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/xcube/**/*.xml"));
        return factoryBean.getObject();
    }
 
    @Bean(name="sqlSession")
    public SqlSessionTemplate sqlSession(@Autowired @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
 
    @Bean(name="mybatisTransactionManager")
    public DataSourceTransactionManager mybatisTransactionManager(@Autowired @Qualifier("mybatisDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
 
}
