package br.com.interfile.vivo.traass.configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class UserFacadeConfiguration {

	private static final String SMTP = "smtp";
	private static final String TEST_CONNECTION = "mail.test-connection";
	private static final String SSL_ENABLE = "mail.smtp.ssl.enable";
	private static final String TLS_ENABLE = "mail.smtp.starttls.enable";
	private static final String AUTH = "mail.smtp.auth";
	private static final String PROTOCOL = "mail.transport.protocol";
	private static final String MESSAGE = "<div id=\"message\">${message}</div>";

	@Value("${spring.mail.host:smtp.gmail.com}")
	private String host;

	@Value("${spring.mail.port:465}")
	private Integer port;

	@Value("${spring.mail.username}")
	private String username;

	@Value("${spring.mail.password}")
	private String password;

	@Value("${spring.mail.properties.mail.smtp.auth:true}")
	private String auth;

	@Value("${spring.mail.properties.mail.smtp.starttls.enable:true}")
	private String enableTTls;

	@Value("${spring.mail.properties.mail.smtp.starttls.required:true}")
	private String required;

	@Value("${spring.mail.properties.mail.smtp.ssl.enable:true}")
	private String enableSsl;

	@Value("${spring.mail.test-connection:true}")
	private String testConnection;

	@Bean
	public JavaMailSender javaMailSender() {
		final JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
		javaMailSenderImpl.setHost(host);
		javaMailSenderImpl.setPort(port);
		javaMailSenderImpl.setUsername(username);
		javaMailSenderImpl.setPassword(password);
		javaMailSenderImpl.setJavaMailProperties(javaMailProperties());
		
		return javaMailSenderImpl;
	}

	private Properties javaMailProperties() {
		final Properties properties = new Properties();
		properties.setProperty(PROTOCOL, SMTP);
		properties.setProperty(AUTH, auth);
		properties.setProperty(TLS_ENABLE, enableTTls);
		properties.setProperty(SSL_ENABLE, enableSsl);
		properties.setProperty(TEST_CONNECTION, testConnection);
		return properties;
	}

	@Bean("resourcesMap")
	public Map<String, String> resourcesMap() {
		final Map<String, String> map = Collections.synchronizedMap(new HashMap<>());
		map.put("verify-email", MESSAGE);
		map.put("password-recovery", MESSAGE);
		map.put("notify", MESSAGE);
		return map;
	}

}
