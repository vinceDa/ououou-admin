package com.ou.common.util;

import com.ou.common.response.BaseResponse;
import com.ou.common.response.FailedResponse;
import com.ou.common.response.SuccessResponse;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 *  文件存储工具类
 * @author vince
 * @date 2020/1/6 19:45
 */
@Slf4j
public class MinioUtil {

    /**
     *  MinIO服务的URL+端口
     */
    private static final String END_POINT = "http://47.106.148.107:9000/";

    private static final String ACCESS_KEY = "minioadmin";

    private static final String SECRET_KEY = "minioadmin";

    /**
     *  文件上传
     * @param file 源文件, 用户上传
     * @return 上传结果
     */
    public static BaseResponse upload(MultipartFile file) {
        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient(END_POINT, ACCESS_KEY, SECRET_KEY);
            String filename = file.getOriginalFilename();
            String bucket = getFileType(filename);
            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(bucket);
            if(!isExist) {
                // 创建一个存储桶
                minioClient.makeBucket(bucket);
            }
            // 使用putObject上传一个文件到存储桶中
            minioClient.putObject(bucket, filename, file.getInputStream(), file.getContentType());
        } catch(MinioException | XmlPullParserException | InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            log.error(e.toString());
            return new FailedResponse(e.toString());
        }
        return new SuccessResponse();
    }

    /**
     *  文件删除
     * @param filename 文件名
     * @return 删除结果
     */
    public static BaseResponse delete(String filename) {
        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient(END_POINT, ACCESS_KEY, SECRET_KEY);
            String bucket = getFileType(filename);
            minioClient.removeObject(bucket, filename);
            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(bucket);
            if(!isExist) {
                log.error("delete error: bucket is not exist");
                return new FailedResponse("未知文件");
            }
            minioClient.removeObject(bucket, filename);
        } catch(MinioException | XmlPullParserException | InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            log.error("file delete error: {}", e.toString());
            return new FailedResponse(e.toString());
        }
        return new SuccessResponse();
    }

    /**
     *  获取下载链接
     * @param filename 文件名
     * @return 下载链接地址
     */
    public static String getDownloadUrl(String filename) {
        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient(END_POINT, ACCESS_KEY, SECRET_KEY);
            String bucket = getFileType(filename);
            minioClient.removeObject(bucket, filename);
            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(bucket);
            if(!isExist) {
                log.error("delete error: bucket is not exist");
                return null;
            }
            String url = minioClient.presignedGetObject(bucket, filename);
            log.info("generate downloadUrl success: {}", url);
            return url;
        } catch(MinioException | XmlPullParserException | InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            log.error("file delete error: {}", e.toString());
            return null;
        }
    }

    public static String getFileType(String fileName) {
        String[] split = fileName.split(".");
        return split[split.length - 1];
    }

}
