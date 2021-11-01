package kr.co.springboot.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import kr.co.springboot.project.common.FileUploadProperties;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties({
    FileUploadProperties.class
})
public class ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}

}
