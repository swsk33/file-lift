package io.github.swsk33.fileliftspringlocaltest;

import io.github.swsk33.fileliftcore.service.UploadFileService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("minio.xml");
		UploadFileService service = context.getBean(UploadFileService.class);
		System.out.println(service.findByMainName("305364024627717"));
	}

}