package top.sharehome.proapiinterface;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.sharehome.proapiclientsdk.client.ProApiClient;
import top.sharehome.proapiclientsdk.model.User;

import javax.annotation.Resource;

@SpringBootTest
class ProapiInterfaceApplicationTests {

    @Resource
    private ProApiClient proApiClient;

    @Test
    void contextLoads() {
        String name1 = proApiClient.getNameByGet("name1");
        String name2 = proApiClient.getNameByPost("name2");
        User user = new User();
        user.setUserName("name3");
        String name3 = proApiClient.getUserNameByPost(user);
        System.out.println(name1);
        System.out.println(name2);
        System.out.println(name3);
    }

}
