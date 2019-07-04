package br.com.interfile.vivo.traass.configuration;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfiguration {

	@Value("${interfile.vivo.token.password:0123456789ABCDEF}")
	private String password;
	
	@Value("${interfile.vivo.token.algorithm:PBEWithMD5AndDES}")
	private String algorithm;

	@Bean
	public StringEncryptor stringEncryptor() {
	    final PooledPBEStringEncryptor stringEncryptor = new PooledPBEStringEncryptor();
	    final SimpleStringPBEConfig simpleStringPBEConfig = createSimpleStringPBEConfig();
	    stringEncryptor.setConfig(simpleStringPBEConfig);
	    return stringEncryptor;
	}

	private SimpleStringPBEConfig createSimpleStringPBEConfig() {
		final SimpleStringPBEConfig simpleStringPBEConfig = new SimpleStringPBEConfig();
	    simpleStringPBEConfig.setPassword(password);
	    simpleStringPBEConfig.setAlgorithm(algorithm);
	    simpleStringPBEConfig.setKeyObtentionIterations("1000");
	    simpleStringPBEConfig.setPoolSize("1");
	    simpleStringPBEConfig.setProviderName("SunJCE");
	    simpleStringPBEConfig.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
	    simpleStringPBEConfig.setStringOutputType("base64");
		return simpleStringPBEConfig;
	}
}
