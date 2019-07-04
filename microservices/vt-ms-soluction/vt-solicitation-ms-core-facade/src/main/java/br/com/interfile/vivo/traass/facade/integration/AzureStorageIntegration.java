package br.com.interfile.vivo.traass.facade.integration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import br.com.interfile.vivo.traass.domain.DigitalDocument;

@Component
public class AzureStorageIntegration {

	@Autowired
	private CloudStorageAccount storageAccount;

	@Value("${interfile.vivo.traass.azure-reference-container:interfile}")
	private String containerReference;

	public DigitalDocument uploadData(final DigitalDocument digitalDocument) {
		try(final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(digitalDocument.getData())) {
			final CloudBlockBlob cloudBlockBlob = storageAccount //
					.createCloudBlobClient() //
					.getContainerReference(containerReference) //
					.getBlockBlobReference(digitalDocument.getReferenceName());

			cloudBlockBlob //
					.getProperties() //
					.setContentType(digitalDocument.getMineType());

			
			cloudBlockBlob.upload(byteArrayInputStream, digitalDocument.getData().length);
			byteArrayInputStream.close();
			
			return digitalDocument //
					.toBuilder() //
					.url(cloudBlockBlob //
							.getUri() //
							.toURL() //
							.toExternalForm()) //
					.build();
		} catch (StorageException | URISyntaxException | IOException e) {
			throw new RuntimeException("problem with upload images.", e);
		}

	}

}
