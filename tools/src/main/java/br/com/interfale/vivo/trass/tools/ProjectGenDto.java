package br.com.interfale.vivo.trass.tools;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class ProjectGenDto {

	public static final String VELOCITY_PATH = "file.resource.loader.path";
	public static final String TEMPLATES_DIR = "templates";

	@NotBlank
	private final String soluctionName;
	
	@NotBlank
	private final ProjectType projectType;

	@NotBlank
	private final String projectPrefix;

	@NotBlank
	private final String domainClass;

	@NotBlank
	private final String packageName;
	
	@NotEmpty
	private final List<ProjectAction> projectActions;

	@JsonCreator
	public ProjectGenDto( //
			@JsonProperty("soluctionName") final String soluctionName, //
			@JsonProperty("projectType") final ProjectType projectType, //
			@JsonProperty("projectPrefix") final String projectPrefix, //
			@JsonProperty("domainClass") final String domainClass, //
			@JsonProperty("packageName") final String packageName,
			@JsonProperty("projectActions") final List<ProjectAction> projectActions) {
		this.soluctionName = soluctionName;
		this.projectType = projectType;
		this.projectPrefix = projectPrefix;
		this.domainClass = domainClass;
		this.packageName = packageName;
		this.projectActions = projectActions;
	}

	public String getSoluctionName() {
		return soluctionName;
	}

	public String getProjectPrefix() {
		return projectPrefix;
	}

	public String getDomainClass() {
		return domainClass;
	}

	public String getPackageName() {
		return packageName;
	}

	public ProjectType getProjectType() {
		return projectType;
	}

	public List<ProjectAction> getProjectActions() {
		return projectActions;
	}
}
