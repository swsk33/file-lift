package io.github.swsk33.fileliftspringboot2test;

import io.github.swsk33.filelifttestcommon.FileLiftTestCommonApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(FileLiftTestCommonApplication.class)
public class FileLiftSpringBootTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileLiftSpringBootTestApplication.class, args);
	}

}