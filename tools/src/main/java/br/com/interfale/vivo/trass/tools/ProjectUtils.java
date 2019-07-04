package br.com.interfale.vivo.trass.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class ProjectUtils {
	
	public static final boolean DEBUG_MODE = Boolean.FALSE;
	
	public static void writeFile(final ProjectGenDto projectGenDto, final String content, final File file)
			throws IOException {
		if (DEBUG_MODE) {
			System.out.println(content);
		} else {
			try (final FileWriter fileWriter = new FileWriter(file)) {
				fileWriter.write(content);
				fileWriter.flush();
			}
		}
	}

	public static String processTemplate(final ProjectGenDto projectGenDto, final Properties properties,
			final String templateName, final VelocityContext context) throws IOException {
		final String templateDir = projectGenDto.getProjectType().getDir();
		final VelocityEngine ve = new VelocityEngine();
		ve.init(properties);
		final Template template = ve.getTemplate(templateDir + File.separatorChar + templateName);
		final StringWriter writer = new StringWriter();
		template.merge(context, writer);
		return writer.toString();
	}
	
	public static List<ProjectAction> createProjectAction(final String... moduleType) {
		return Arrays //
				.asList(ProjectAction //
						.values()) //
				.stream() //
				.filter(predicate -> //
				filterModuleType(predicate, moduleType)) //
				.collect(Collectors.toList());
	}

	private static boolean filterModuleType(final ProjectAction projectAction, final String... moduleType) {
		return Arrays.asList(moduleType).stream().filter(predicate -> projectAction.name().equalsIgnoreCase(predicate))
				.findFirst().isPresent();
	}

	public static void createModule(final String moduleName, final String templateName,
			final ProjectGenDto projectGenDto, final Consumer<File> srcConsumer, final File soluctionDir,
			final Properties properties, final VelocityContext context) {
		final String projectPrefix = (String) context.get(projectGenDto.getProjectPrefix());
		final File projectDir = new File(
				soluctionDir.getAbsolutePath() + File.separatorChar + projectPrefix + "-" + moduleName);
		try {
			final ProjectType projectType = projectGenDto.getProjectType();
			projectType.createBaseProject(projectGenDto, projectDir);
			projectType.createBuildProject(projectGenDto, properties, context, projectDir, templateName);
			srcConsumer.accept(projectDir);
		} catch (IOException e) {
			throw new RuntimeException(String.format(
					"moduleName=%s, templateName=%s, projectGenDto=%s, soluctionDir=%s, properties=%s, context=%s",
					moduleName, templateName, projectGenDto, soluctionDir, properties, context), e);
		}
	}

	public static void createSrc(final ProjectGenDto projectGenDto, final File projectDir, final String packageSufix,
			final String templateName, final Properties properties, final VelocityContext context) {
		final String packageName = (String) context.get("packageName");
		final String domainClass = (String) context.get("domainClass");
		final File srcDir = new File(projectDir.getAbsolutePath() + File.separatorChar + "src/main/java");
		srcDir.mkdirs();
		final File packageDir = new File(srcDir.getAbsolutePath() + File.separatorChar
				+ packageName.concat(".").concat(packageSufix).replace(".", File.separator));
		packageDir.mkdirs();
		try {
			final String contentJavaFile = ProjectUtils.processTemplate(projectGenDto, properties, templateName,
					context);
			final File javaFile = new File(packageDir.getAbsolutePath() + File.separatorChar + domainClass + ".java");
			ProjectUtils.writeFile(projectGenDto, contentJavaFile, javaFile);
			final File resourceDir = new File(projectDir.getAbsolutePath() + File.separatorChar + "src/main/resource");
			resourceDir.mkdirs();
		} catch (IOException e) {
			throw new RuntimeException(String.format("templateName=%s, projectGenDto=%s, properties=%s, context=%s",
					templateName, projectGenDto, properties, context), e);
		}
	}
	
	public static File createSoluction(final ProjectGenDto projectGenDto, final Properties properties,
			final VelocityContext context) throws IOException {
		return projectGenDto //
				.getProjectType() //
				.createSoluction(projectGenDto, properties, context);
	}

	public static void createProjects(final File soluctionDir, final ProjectGenDto projectGenDto,
			final Properties properties, final VelocityContext context) {
		projectGenDto //
				.getProjectActions() //
				.forEach(action -> {
					action.createModule(projectGenDto, soluctionDir, properties, context);
				});
	}

}
