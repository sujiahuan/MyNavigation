package org.jiahuan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.jiahuan.mapper")
public class MyNavigationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyNavigationApplication.class, args);
    }

}
