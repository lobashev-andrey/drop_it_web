package org.example.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class DbConfig {

    @Bean
    public DataSource dataSource(){
        return new EmbeddedDatabaseBuilder()
                .generateUniqueName(false)    //  чтобы не заморачиваться
                .setName("book_store")             //  имя БД
                .setType(EmbeddedDatabaseType.H2)  //  тип
                .addDefaultScripts()               //  выполнит скрипты по популяции таблиц и некоторых данных
                .setScriptEncoding("UTF-8")        //
                .ignoreFailedDrops(true)      //
                .build();
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(){ // этот объект позволяет
        return new NamedParameterJdbcTemplate(dataSource());        // выполнять операции с БД
    }
}
