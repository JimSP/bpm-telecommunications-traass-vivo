package br.com.interfale.vivo.trass.tools;

import org.apache.velocity.VelocityContext;
import org.springframework.stereotype.Component;

@Component
public class CreateVelocityContext {

	public VelocityContext create(final ProjectGenDto projectGenDto) {
		final VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("soluctionName", projectGenDto.getSoluctionName());
		velocityContext.put("projectPrefix", projectGenDto.getProjectPrefix());
		velocityContext.put("packageName", projectGenDto.getPackageName());
		velocityContext.put("domainClass", projectGenDto.getDomainClass());
		velocityContext.put("springBootVersion", "2.0.3.RELEASE");
		return velocityContext;
	}
}
