package br.com.interfile.vivo.traass.integration;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MessageContentBuilder {

	@Autowired
	@Qualifier("resourcesMap")
	private Map<String, String> resourcesMap;
	
	public String build(final String templateName, final Map<String, Object> variables) {

		final String template = resourcesMap.get(templateName);
		
		return variables
				.entrySet()
				.stream()
				.map(mapper->process(template, mapper))
				.reduce((a,b)->b)
				.orElse("");
	}

	private String process(final String template, final Entry<String, Object> mapper) {
		return template.replace("${" + mapper.getKey()  + "}", mapper != null ? String.valueOf(mapper.getValue()) : "");
	}
}
