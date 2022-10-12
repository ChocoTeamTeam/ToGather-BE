package chocoteamteam.togather.service;

import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.S3FileUtilException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.bucket}")
    private String bucket;

    public String upload(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new S3FileUtilException(ErrorCode.NOT_FOUND_IMAGE);
        }

        String originName = file.getOriginalFilename();
        String type = Objects.requireNonNull(originName).substring(originName.lastIndexOf(".") + 1);

        validationFileType(type.toUpperCase());

        String fileName = UUID.randomUUID() + originName;
        ObjectMetadata objectMetadata = new ObjectMetadata();

        try {
            objectMetadata.setContentLength(file.getInputStream().available());
            amazonS3.putObject(bucket, fileName, file.getInputStream(), objectMetadata);
        } catch (IOException e) {
            throw new S3FileUtilException(ErrorCode.IMAGE_UPLOAD_FAIL);
        }

        return amazonS3.getUrl(bucket, fileName).toString();

    }

    private void validationFileType(String type) {
        switch (type){
            case "PNG":
            case "JPG":
            case "JPEG":
            case "GIF":
                return;
            default:
                throw new S3FileUtilException(ErrorCode.MISS_MATCH_IMAGE_TYPE);
        }
    }

}
