package top.sharehome.reggie;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Description
 * @Author:AntonyCheng
 * @CreateTime:2023/1/24 14:21
 */
@SpringBootApplication
// Slf4j主要作用是启用log()方法
@Slf4j
// MapperScan注解一定要加入mapper所在的包全类名
@MapperScan("top.sharehome.reggie.mapper")
@ServletComponentScan
@EnableTransactionManagement
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class, args);
    }
}
