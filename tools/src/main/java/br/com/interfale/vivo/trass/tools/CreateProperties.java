package br.com.interfale.vivo.trass.tools;

import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class CreateProperties {

	public Properties create() {
		final Properties properties = new Properties();
		properties.setProperty(ProjectGenDto.VELOCITY_PATH, ProjectGenDto.TEMPLATES_DIR);
		return properties;
	}
}
