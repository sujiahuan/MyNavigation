package org.jiahuan;

import org.jiahuan.netty.NettyClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("org.jiahuan.mapper")
@EnableScheduling
//@EnableAsync
public class MyNavigationApplication implements CommandLineRunner {
//public class MyNavigationApplication{

    @Autowired
    private NettyClient nettyClient;

    public static void main(String[] args) {
        SpringApplication.run(MyNavigationApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        nettyClient.init();
    }
}
