package br.com.interfile.vivo.traass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages= {"br.com.interfile.vivo.traass"})
public class UserRestApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(UserRestApplication.class);
	}

	public static void main(final String[] args) {
		SpringApplication.run(UserRestApplication.class, args);
	}

}
