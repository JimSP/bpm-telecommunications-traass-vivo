package br.com.interfale.vivo.trass.tools;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;

public enum ProjectType {
	Gradle("gradle") {
		@Override
		public File createSoluction(final ProjectGenDto projectGenDto, final Properties properties,
				final VelocityContext context) throws IOException {
			final File soluctionDir = new File(projectGenDto.getSoluctionName());
			soluctionDir.mkdir();
			createBaseProject(projectGenDto, soluctionDir);
			createBuildSoluction(projectGenDto, properties, context, soluctionDir);
			createBuildSettings(projectGenDto, properties, context, soluctionDir);
			ProjectUtils.createProjects(soluctionDir, projectGenDto, properties, context);
			return soluctionDir;
		}

		private void createBuildSettings(final ProjectGenDto projectGenDto, final Properties properties,
				final VelocityContext context, final File soluctionDir) throws IOException {
			final String settingsGradle = ProjectUtils.processTemplate(projectGenDto, properties,
					"soluction-settings.gradle.vm", context);
			ProjectUtils.writeFile(projectGenDto, settingsGradle,
					new File(soluctionDir.getAbsolutePath() + File.separatorChar + "settings.gradle"));
		}

		private void createBuildSoluction(final ProjectGenDto projectGenDto, final Properties properties,
				final VelocityContext context, final File soluctionDir) throws IOException {
			final String buildGradleContent = ProjectUtils.processTemplate(projectGenDto, properties,
					"soluction-build.gradle.vm", context);
			final File buildGradle = new File(soluctionDir.getAbsolutePath() + File.separatorChar + "build.gradle");
			ProjectUtils.writeFile(projectGenDto, buildGradleContent, buildGradle);
		}

		@Override
		public void createBaseProject(final ProjectGenDto projectGenDto, final File projectDir) throws IOException {
			final File newGradleDir = new File(projectDir.getAbsolutePath() + File.separatorChar + "gradle");
			final File gradleDir = new File("gradle");
			FileUtils.copyDirectory(gradleDir, newGradleDir);
			FileUtils.copyFile(new File("gradlew"),
					new File(projectDir.getAbsolutePath() + File.separatorChar + "gradlew"));
			FileUtils.copyFile(new File("gradlew.bat"),
					new File(projectDir.getAbsolutePath() + File.separatorChar + "gradlew.bat"));
			ProjectUtils.writeFile(projectGenDto, StringUtils.EMPTY,
					new File(projectDir.getAbsolutePath() + File.separatorChar + "gradle.properties"));
		}
		
		public void createBuildProject(final ProjectGenDto projectGenDto, final Properties properties,
				final VelocityContext context, final File soluctionDir, final String templateProjectName)
				throws IOException {
			final String buildGradleContent = ProjectUtils.processTemplate(projectGenDto, properties,
					templateProjectName, context);
			final File buildGradle = new File(soluctionDir.getAbsolutePath() + File.separatorChar + "build.gradle");
			ProjectUtils.writeFile(projectGenDto, buildGradleContent, buildGradle);
		}
	},
	Maven("maven") {
		@Override
		public File createSoluction(final ProjectGenDto projectGenDto, final Properties properties,
				final VelocityContext context) throws IOException {
			return null;
		}

		@Override
		public void createBaseProject(final ProjectGenDto projectGenDto, final File projectDir) throws IOException {

		}

		@Override
		public void createBuildProject(final ProjectGenDto projectGenDto, final Properties properties,
				final VelocityContext context, final File soluctionDir, final String templateProjectName)
				throws IOException {

		}
	};

	private final String dir;

	private ProjectType(final String dir) {
		this.dir = dir;
	}

	public String getDir() {
		return dir;
	}

	public static ProjectType create(final String projectTye) {
		return Arrays //
				.asList(ProjectType.values()) //
				.stream() //
				.filter(predicate -> predicate //
						.name() //
						.equalsIgnoreCase(projectTye)) //
				.findFirst() //
				.get();
	}

	public abstract File createSoluction(final ProjectGenDto projectGenDto, final Properties properties,
			final VelocityContext context) throws IOException;

	public abstract void createBuildProject(final ProjectGenDto projectGenDto, final Properties properties,
			final VelocityContext context, final File soluctionDir, final String templateProjectName)
			throws IOException;

	public abstract void createBaseProject(final ProjectGenDto projectGenDto, final File projectDir) throws IOException;
}
