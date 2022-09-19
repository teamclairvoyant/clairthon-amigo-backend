package com.services.dm;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.services.dm.constants.Constant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean(Constant.S3_CLIENT)
	public AmazonS3 s3Client() {
		return AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTH_1).build();
	}

}
