package chocoteamteam.togather.component.aws;

import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.S3FileUtilException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Component
public class S3FileUtil {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.bucket}")
    private String bucket;

    public String upload(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new S3FileUtilException(ErrorCode.NOT_FOUND_IMAGE);
        }

        String fileName = UUID.randomUUID() + file.getOriginalFilename();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getInputStream().available());

        amazonS3.putObject(bucket, fileName, file.getInputStream(), objectMetadata);

        return amazonS3.getUrl(bucket, fileName).toString();

    }

}
