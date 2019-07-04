package br.com.interfale.vivo.trass.tools;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreatePayload {

	@Test
	public void create() throws JsonProcessingException {
		System.out //
				.println(new ObjectMapper() //
						.writeValueAsString( //
								new ProjectGenDto( //
										"teste", //
										ProjectType.Gradle, //
										"ts", //
										"Teste", //
										"com.teste", //
										Arrays.asList(ProjectAction.values()))));
	}
}
