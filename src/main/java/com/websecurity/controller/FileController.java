package com.websecurity.controller;

import com.websecurity.annotation.NeedToken;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class FileController {

    final private String[] FILENAME_BLACK_LIST = {".jsp", ".php", ".exe", ".dll", "vxd", "html"};

    final private String imgPath = "C:\\Users\\dyh\\Desktop\\demo\\src\\main\\resources\\image\\";


    @RequestMapping("/fileupload")
    public ModelAndView fileUpload(MultipartFile photo) {
        String fileType = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf("."));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String fileName = imgPath + uuid + fileType;
        ModelAndView mav = new ModelAndView("main");
        try {
//            File.createTempFile(uuid, fileType.substring(fileType.lastIndexOf(".") + 1), new File(path));
            photo.transferTo(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return mav.addObject("filemsg", "上传失败");
        }
        return mav.addObject("filemsg", "上传成功，文件路径：" + fileName);
    }


    @RequestMapping("/fileuploadsec")
    public ModelAndView fileUpLoadSec(MultipartFile photo) {
        String fileType = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf("."));
        boolean IsIllegalFileName = false;
        for (String s : FILENAME_BLACK_LIST) {
            if (s.toLowerCase().equals(fileType)) {
                IsIllegalFileName = true;
                break;
            }
        }
        ModelAndView mav = new ModelAndView("main");
        if (!IsIllegalFileName) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String fileName = imgPath + uuid + fileType;
            try {
//            File.createTempFile(uuid, fileType.substring(fileType.lastIndexOf(".") + 1), new File(path));
                photo.transferTo(new File(fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mav.addObject("filemsgsec", "上传成功，文件路径：" + fileName);
        } else {
            return mav.addObject("filemsgsec", "文件非法！上传失败");
        }
    }
}
