package br.com.interfale.vivo.trass.tools;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectGen {

	@Autowired
	private CompactDirectory compactDirectory;

	public File getSoluctionZip(final File soluctionDir) throws IOException {
		return compactDirectory.zipDir(soluctionDir);
	}

	public File createSoluction(final ProjectGenDto projectGenDto, final Properties properties,
			final VelocityContext context) throws IOException {
		return ProjectUtils.createSoluction(projectGenDto, properties, context);
	}
}
