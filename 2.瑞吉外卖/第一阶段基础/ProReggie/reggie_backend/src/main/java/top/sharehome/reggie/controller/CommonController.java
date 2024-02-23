package top.sharehome.reggie.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.sharehome.reggie.common.R;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * @Description
 * @Author:AntonyCheng
 * @CreateTime:2023/1/31 18:14
 */
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;

    /**
     * 上传文件
     *
     * @param file 目标文件的二进制临时文件
     *             MultipartFile类型的参数名一定要和前端保持一致，且file拿到后是一个临时文件，需要转存到指定位置，否则会被自动销毁
     * @return 上传的结果R对象
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        // 获取原始文件名，但是为了避免重复，这里最好使用随机字符串，比如UUID
        // String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString().toUpperCase();
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        fileName = fileName + suffix;
        try {
            File desFile = new File(basePath + fileName);
            if (!desFile.exists()) {
                desFile.mkdirs();
            }
            file.transferTo(desFile);
        } catch (IOException e) {
            return R.error("上传文件发生意外错误，请联系管理员……");
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     *
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        FileInputStream fileInputStream = null;
        ServletOutputStream outputStream = null;
        try {
            fileInputStream = new FileInputStream(basePath + name);
            outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int length = 0;
            byte[] bytes = new byte[1024];
            while ((length = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, length);
                outputStream.flush();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
