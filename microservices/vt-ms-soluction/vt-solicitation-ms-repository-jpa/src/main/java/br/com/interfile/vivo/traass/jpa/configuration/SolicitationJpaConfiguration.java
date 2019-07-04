package br.com.interfile.vivo.traass.jpa.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "br.com.interfile.vivo.traass.jpa.repositories")
@EnableTransactionManagement
public class SolicitationJpaConfiguration {

}
