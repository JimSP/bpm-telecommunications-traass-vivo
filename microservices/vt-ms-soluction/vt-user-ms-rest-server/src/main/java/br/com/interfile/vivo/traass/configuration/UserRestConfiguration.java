package br.com.interfile.vivo.traass.configuration;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import br.com.interfile.vivo.traass.converter.AddressToAddressDtoConverter;
import br.com.interfile.vivo.traass.converter.AuthUserRequestDtoToUserConverter;
import br.com.interfile.vivo.traass.converter.UserRequestDtoToUserConverter;
import br.com.interfile.vivo.traass.converter.UserToUserResponseDtoConverter;
import br.com.interfile.vivo.traass.validation.CpfOrCnpjValidator;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableJpaRepositories(basePackages = "br.com.interfile.vivo.traass.jpa")
@EnableSwagger2
@ComponentScan(basePackages = { "br.com.interfile.vivo.traass" })
public class UserRestConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRestConfiguration.class);

	@Autowired
	private AuthUserRequestDtoToUserConverter authUserRequestDtoToUserConverter;

	@Autowired
	private UserRequestDtoToUserConverter userRequestDtoToUserConverter;

	@Autowired
	private UserToUserResponseDtoConverter userToUserResponseDtoConverter;

	@Autowired
	private AddressToAddressDtoConverter addressToAddressDtoConverter;

	@Bean
	public ConversionService conversionService() {
		final DefaultConversionService defaultConversionService = new DefaultConversionService();
		defaultConversionService.addConverter(authUserRequestDtoToUserConverter);
		defaultConversionService.addConverter(userRequestDtoToUserConverter);
		defaultConversionService.addConverter(userToUserResponseDtoConverter);
		defaultConversionService.addConverter(addressToAddressDtoConverter);
		return defaultConversionService;
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("DELETE");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2) //
				.select() //
				.apis(RequestHandlerSelectors //
						.basePackage("br.com.interfile.vivo.traass.controller")) //
				.paths(PathSelectors.any()) //
				.build();
	}

	@Bean
	public RestTemplate restTemplate() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		final TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

		final SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
				.loadTrustMaterial(null, acceptingTrustStrategy).build();
		final SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
		final CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
		final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		final RestTemplate restTemplate = new RestTemplate(requestFactory);
		restTemplate.setInterceptors(Arrays.asList(new ClientHttpRequestInterceptor() {

			@Override
			public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
					throws IOException {
				LOGGER.debug("header:" + request.getHeaders());
				LOGGER.debug("method:" + request.getMethodValue());
				LOGGER.debug("   uri:" + request.getURI());
				LOGGER.debug("  body:" + new String(body));
				return execution.execute(request, body);
			}
		}));

		return restTemplate;
	}

	@Bean
	public CpfOrCnpjValidator cpfOrCnpjValidator() {
		return new CpfOrCnpjValidator();
	}
}
