package kr.co.smartcube.xcube;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import kr.co.smartcube.xcube.common.FileUploadProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    FileUploadProperties.class
})
public class XcubeApplication {

	public static void main(String[] args) {
		SpringApplication.run(XcubeApplication.class, args);
	}

}
