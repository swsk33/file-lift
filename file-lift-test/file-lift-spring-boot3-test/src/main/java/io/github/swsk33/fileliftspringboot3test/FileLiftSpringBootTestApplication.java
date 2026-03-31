package io.github.swsk33.fileliftspringboot3test;

import io.github.swsk33.filelifttestcommon.api.FileLiftTestApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {FileLiftSpringBootTestApplication.class, FileLiftTestApi.class})
public class FileLiftSpringBootTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileLiftSpringBootTestApplication.class, args);
	}

}
