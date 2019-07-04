package br.com.interfale.vivo.trass.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ToolCodeGenContext {
	
	@Autowired
	private ProjectGen projectGen;
	
	@Autowired
	private CreateProjectGenDto createProjectGenDto;
	
	@Autowired
	private CreateVelocityContext createVelocityContext;
	
	@Autowired
	private CreateProperties createProperties;

	public ProjectGen getProjectGen() {
		return projectGen;
	}

	public void setProjectGen(ProjectGen projectGen) {
		this.projectGen = projectGen;
	}

	public CreateProjectGenDto getCreateProjectGenDto() {
		return createProjectGenDto;
	}

	public void setCreateProjectGenDto(CreateProjectGenDto createProjectGenDto) {
		this.createProjectGenDto = createProjectGenDto;
	}

	public CreateVelocityContext getCreateVelocityContext() {
		return createVelocityContext;
	}

	public void setCreateVelocityContext(CreateVelocityContext createVelocityContext) {
		this.createVelocityContext = createVelocityContext;
	}

	public CreateProperties getCreateProperties() {
		return createProperties;
	}

	public void setCreateProperties(CreateProperties createProperties) {
		this.createProperties = createProperties;
	}
}
