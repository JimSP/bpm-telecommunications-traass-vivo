package br.com.interfile.vivo.traass.configuration;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageCredentials;
import com.microsoft.azure.storage.StorageException;

@Configuration
@EnableScheduling
@EnableTransactionManagement
public class SolicitationFacadeConfiguration {

	@Value("${azure.storage.connection-string}")
	private String connectionString;

	@Bean
	public CloudStorageAccount cloudStorageAccount() throws URISyntaxException, InvalidKeyException, StorageException {
		return new CloudStorageAccount(storageCredentials());
	}

	private StorageCredentials storageCredentials() throws InvalidKeyException, StorageException {
		return StorageCredentials.tryParseCredentials(connectionString);
	}
}
