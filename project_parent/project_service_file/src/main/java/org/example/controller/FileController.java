package org.example.controller;

import org.example.pojo.FastDFSFile;
import org.example.pojo.Result;
import org.example.pojo.StatusCode;
import org.example.util.FastDFSClient;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@RestController
@RequestMapping("/file")
public class FileController {
    @PostMapping("/upload")
    public Result uploadFile(MultipartFile file) {

        try {
            if(file==null){
                throw new RuntimeException("文件不存在");
            }
            String originalFilename= file.getOriginalFilename();
            if(StringUtils.isEmpty(originalFilename)){
                throw new RuntimeException("文件不存在");
            }

            String extName=originalFilename.substring(originalFilename.lastIndexOf("."));
            byte[] content= file.getBytes();
            FastDFSFile fastDFSFile=new FastDFSFile(originalFilename,content,extName);
            String[] uploadResult= FastDFSClient.upload(fastDFSFile);
            String url = FastDFSClient.getTrackerUrl()+uploadResult[0]+"/"+uploadResult[1];
            return new Result(true, StatusCode.OK,"文件上传成功",url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false, StatusCode.ERROR,"文件上传失败");
    }

    @GetMapping("/download")
    public Result downloadFile(String group, String name, HttpServletResponse response) {
        response.setHeader("Content-Disposition", "attachment; filename=" + name + "");


        try {
            InputStream is= FastDFSClient.downloadFile(group,name);
            OutputStream os= response.getOutputStream();

            byte[] bytes=new byte[8192];

            int len=is.read(bytes);
            os.write(bytes,0,len);
            System.out.println(group+name);
            return new Result(true, StatusCode.OK,"文件下载完毕");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false, StatusCode.ERROR,"文件下载失败");
    }
}
