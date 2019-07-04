package br.com.interfale.vivo.trass.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectGenRestController {

	@Autowired
	private ProjectGen projectGen;

	@Autowired
	private CreateVelocityContext createVelocityContext;

	@Autowired
	private CreateProperties createProperties;
	
	@GetMapping("moduleTypes")
	public ProjectAction[] listProjectAction() {
		return ProjectAction.values();
	}
	
	@GetMapping("projectTypes")
	public ProjectType[] listProjectType() {
		return ProjectType.values();
	}

	@PostMapping(value = "/", consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE }, produces = { "application/zip" })
	public ResponseEntity<InputStreamResource> createProject(@RequestBody final ProjectGenDto projectGenDto)
			throws IOException {
		final VelocityContext velocityContext = createVelocityContext.create(projectGenDto);
		final Properties properties = createProperties.create();
		final File soluctionDir = projectGen.createSoluction(projectGenDto, properties, velocityContext);
		final File soluctionZip = projectGen.getSoluctionZip(soluctionDir);
		return ResponseEntity //
				.ok() //
				.contentLength(soluctionZip.length()) //
				.contentType(MediaType.parseMediaType("application/zip")) //
				.body(new InputStreamResource(new FileInputStream(soluctionZip)));
	}
}
