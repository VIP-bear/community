package site.bearblog.community.provider;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.bearblog.community.exception.CustomizeErrorCode;
import site.bearblog.community.exception.CustomizeException;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
public class AliyunProvider {

    @Value("${aliyun.file.endpoint}")
    private String endpoint;

    @Value("${aliyun.file.bucket-name}")
    private String bucketName;

    @Value("${aliyun.file.folder}")
    private String folder;

    @Value("${aliyun.file.public-key}")
    private String accessKeyId;

    @Value("${aliyun.file.private-key}")
    private String accessKeySecret;

    @Value("${aliyun.file.expires}")
    private Integer expires;

    public String upload(MultipartFile uploadFile){
        URL url;
        String imageUrl = null;
        // 图片名
        String fileName = UUID.randomUUID().toString() + uploadFile.getOriginalFilename();
        try {
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            // 上传
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(uploadFile.getInputStream().available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(uploadFile.getContentType());
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            ossClient.putObject(bucketName, folder+fileName, uploadFile.getInputStream(), objectMetadata);
            // 获取图片url,第二个参数图片地址，第三个参数图片地址有效期
            url = ossClient.generatePresignedUrl(bucketName, folder+fileName, new Date(new Date().getTime() + expires));
            if (url != null){
                imageUrl = url.toString();
            }else {
                throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
            }
            // 关闭client
            ossClient.shutdown();

        }catch (IOException e){
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
        return imageUrl;

    }

}
