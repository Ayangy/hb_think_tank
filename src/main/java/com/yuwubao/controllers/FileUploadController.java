package com.yuwubao.controllers;

import com.yuwubao.util.Const;
import com.yuwubao.util.RestApiResponse;
import com.yuwubao.util.ThinkTankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by yangyu on 2017/11/15.
 */
@RestController
@RequestMapping("/resource")
@CrossOrigin
public class FileUploadController {

    private final static Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Value("${resourcesPath}")
    private String resourcesPath;

    /**
     * 将上传的图片保存到本地
     * @param file  上传的文件
     * @return
     */
    @PostMapping("/upload")
    public RestApiResponse<String> saveFile(@RequestParam int type,  MultipartFile file) {
        RestApiResponse<String> result = new RestApiResponse<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(new Date());
        String path;
        if (type == 0) {
            path = resourcesPath + "\\img\\" + time + "\\";
        } else {
            path = resourcesPath + "\\video\\" + time + "\\";
        }
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf('.'));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        filename = uuid + suffix;
        String visit;
        if (type == 0) {
            visit = "img/" + time + "/" + filename;
        } else {
             visit = "video/" + time + "/" + filename;
        }
        try {
            file.transferTo(new File(path + filename));
            String address = ThinkTankUtil.getLocalHostLANAddress().getHostAddress();
            String ip= "http://" + address + "/";
            result.successResponse(Const.SUCCESS, ip + visit);
        } catch (Exception e) {
            logger.warn("文件上传失败", e);
            result.failedApiResponse(Const.FAILED, "文件上传失败");
        }
        return result;
    }

    /**
     * 删除上传的文件
     * @param urlPath 文件路径
     */
    @DeleteMapping("/delete")
    public RestApiResponse<Boolean> deleteFile(String urlPath) {
        RestApiResponse<Boolean> result = new RestApiResponse<Boolean>();
        try {
            String partFilePath = urlPath.substring(ThinkTankUtil.getCharacterPosition(urlPath));
            String filePath= resourcesPath + partFilePath;
            File deleteFile = new File(filePath);
            boolean state = deleteFile.delete();
            result.successResponse(Const.SUCCESS, state, "删除成功");
        } catch (Exception e) {
            logger.warn("删除上传的文件失败", e);
            result.failedApiResponse(Const.FAILED, "删除失败");
        }
        return result;
    }

}