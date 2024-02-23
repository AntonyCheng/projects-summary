package top.sharehome.project.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.sharehome.procommon.service.InnerUserInterfaceInfoService;

import javax.annotation.Resource;

@SpringBootTest
class UserInterfaceInfoServiceImplTest {
    @Resource
    private InnerUserInterfaceInfoService userInterfaceInfoService;

    @Test
    void invokeCount() {
        boolean b = userInterfaceInfoService.invokeCount(1L, 1L);
        Assertions.assertTrue(b);
    }
}