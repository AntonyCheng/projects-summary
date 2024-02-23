package top.sharehome.proapiclientsdk;

import top.sharehome.proapiclientsdk.client.ProApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author:AntonyCheng
 * @CreateTime:2023/1/16 13:13
 */
@Configuration
@ConfigurationProperties("proapi.client")
@Data
@ComponentScan
public class ProApiClientConfig {
    private String accessKey;
    private String secretKey;

    @Bean
    public ProApiClient proApiClient() {
        return new ProApiClient(accessKey, secretKey);
    }
}
