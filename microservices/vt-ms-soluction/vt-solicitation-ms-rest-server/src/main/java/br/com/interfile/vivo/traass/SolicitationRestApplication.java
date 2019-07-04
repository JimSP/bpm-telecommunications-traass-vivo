package br.com.interfile.vivo.traass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages= {"br.com.interfile.vivo.traass"})
public class SolicitationRestApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder springApplicationBuilder) {
		return springApplicationBuilder.sources(SolicitationRestApplication.class);
	}

	public static void main(final String[] args) {
		SpringApplication.run(SolicitationRestApplication.class, args);
	}
}
