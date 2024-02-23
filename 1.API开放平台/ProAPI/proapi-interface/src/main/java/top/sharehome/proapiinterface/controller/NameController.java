package top.sharehome.proapiinterface.controller;

import org.springframework.web.bind.annotation.*;
import top.sharehome.proapiclientsdk.model.User;
import top.sharehome.proapiclientsdk.utils.SignUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description 查询名称接口
 * @Author:AntonyCheng
 * @CreateTime:2023/1/16 00:00
 */
@RestController
@RequestMapping("/name")
public class NameController {
    @GetMapping("/get")
    public String getNameByGet(String name) {
        return "GET 你的名字是" + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name) {
        return "POST 你的名字是" + name;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user) {
        return "POST 用户名是：" + user.getUserName();
    }
}
