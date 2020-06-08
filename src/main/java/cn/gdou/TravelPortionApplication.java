package cn.gdou;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@MapperScan("cn.gdou.mapper")
public class TravelPortionApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelPortionApplication.class, args);
    }

}
