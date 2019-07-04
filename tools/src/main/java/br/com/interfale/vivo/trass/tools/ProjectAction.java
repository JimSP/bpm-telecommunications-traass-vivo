package br.com.interfale.vivo.trass.tools;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.velocity.VelocityContext;

public enum ProjectAction {

	CommumDomain {
		@Override
		public void createModule(final ProjectGenDto projectGenDto, final File soluctionDir,
				final Properties properties, final VelocityContext context) {
			ProjectUtils.createModule(
					"commum-domain", "lib-build.gradle.vm", projectGenDto, projectDir -> ProjectUtils
							.createSrc(projectGenDto, projectDir, "domain", "domain.java.vm", properties, context),
					soluctionDir, properties, context);
		}
	},
	CommumFacade {
		@Override
		public void createModule(final ProjectGenDto projectGenDto, final File soluctionDir,
				final Properties properties, final VelocityContext context) {
			ProjectUtils.createModule(
					"commum-core-facade", "facade-build.gradle.vm", projectGenDto, projectDir -> ProjectUtils
							.createSrc(projectGenDto, projectDir, "facade", "facade.java.vm", properties, context),
					soluctionDir, properties, context);
		}
	},
	CommumEntity {
		@Override
		public void createModule(final ProjectGenDto projectGenDto, final File soluctionDir,
				final Properties properties, final VelocityContext context) {
			ProjectUtils.createModule("commum-repository-entity", "entity-build.gradle.vm", projectGenDto,
					projectDir -> ProjectUtils.createSrc(projectGenDto, projectDir, "jpa.entities", "entity.java.vm",
							properties, context),
					soluctionDir, properties, context);
		}
	},
	ComumRepository {
		@Override
		public void createModule(final ProjectGenDto projectGenDto, final File soluctionDir,
				final Properties properties, final VelocityContext context) {
			ProjectUtils.createModule("commum-repository-jpa", "repository-build.gradle.vm", projectGenDto,
					projectDir -> createSrcRepository(projectGenDto, projectDir, properties, context), soluctionDir,
					properties, context);
		}

		private void createSrcRepository(final ProjectGenDto projectGenDto, final File soluctionDir,
				final Properties properties, final VelocityContext context) {
			final String packageName = projectGenDto.getPackageName();
			final String domainClass = projectGenDto.getDomainClass();
			final String projectPrefix = (String) context.get(projectGenDto.getProjectPrefix());
			final File projectDir = new File(soluctionDir.getAbsolutePath() + File.separatorChar + projectPrefix + "-"
					+ "commum-repository-jpa");
			final String sourceClass = domainClass;
			final String destinationClass = domainClass + "Entity";
			context.put("sourceClass", sourceClass);
			context.put("destinationClass", destinationClass);
			final File srcDir = new File(projectDir.getAbsolutePath() + File.separatorChar + "src/main/java");
			srcDir.mkdirs();
			final File packageRepositoryDir = new File(srcDir.getAbsolutePath() + File.separatorChar
					+ packageName.concat(".jpa.repositories").replace(".", File.separator));
			packageRepositoryDir.mkdirs();
			final File packageServiceDir = new File(srcDir.getAbsolutePath() + File.separatorChar
					+ packageName.concat(".jpa.services").replace(".", File.separator));
			packageServiceDir.mkdirs();
			final File packageConfigurationDir = new File(srcDir.getAbsolutePath() + File.separatorChar
					+ packageName.concat(".jpa.configuration").replace(".", File.separator));
			packageConfigurationDir.mkdirs();
			final File packageConverterDir = new File(srcDir.getAbsolutePath() + File.separatorChar
					+ packageName.concat(".jpa.converter").replace(".", File.separator));
			packageConverterDir.mkdirs();
			try {
				final String contentJavaFile = ProjectUtils.processTemplate(projectGenDto, properties,
						"repository.java.vm", context);
				final File javaFile = new File(packageRepositoryDir.getAbsolutePath() + File.separatorChar + domainClass
						+ "JpaRepository.java");
				ProjectUtils.writeFile(projectGenDto, contentJavaFile, javaFile);
				final String contentServiceJavaFile = ProjectUtils.processTemplate(projectGenDto, properties,
						"service.java.vm", context);
				final File javaServiceFile = new File(
						packageServiceDir.getAbsolutePath() + File.separatorChar + domainClass + "Service.java");
				ProjectUtils.writeFile(projectGenDto, contentServiceJavaFile, javaServiceFile);
				final String contentConfigurationJavaFile = ProjectUtils.processTemplate(projectGenDto, properties,
						"configuration.java.vm", context);
				final File javaConfigurationFile = new File(packageConfigurationDir.getAbsolutePath()
						+ File.separatorChar + domainClass + "Configuration.java");
				ProjectUtils.writeFile(projectGenDto, contentConfigurationJavaFile, javaConfigurationFile);
				final String contentConverterJavaFile = ProjectUtils.processTemplate(projectGenDto, properties,
						"converter.java.vm", context);
				final File javaConverterFile = new File(
						packageConverterDir.getAbsolutePath() + File.separatorChar + domainClass + "Converter.java");
				ProjectUtils.writeFile(projectGenDto, contentConverterJavaFile, javaConverterFile);
				final File resourceDir = new File(
						projectDir.getAbsolutePath() + File.separatorChar + "src/main/resource");
				resourceDir.mkdirs();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	},
	CommumRestDto {
		@Override
		public void createModule(ProjectGenDto projectGenDto, File soluctionDir, Properties properties,
				VelocityContext context) {
			ProjectUtils.createModule("commum-rest-dto", "rest-dto.gradle.vm", projectGenDto,
					projectDir -> createSrcRestDto(projectGenDto, projectDir, properties, context), soluctionDir,
					properties, context);
		}
	},
	CommumConverterRestDto {
		@Override
		public void createModule(ProjectGenDto projectGenDto, File soluctionDir, Properties properties,
				VelocityContext context) {
			ProjectUtils.createModule("commum-rest-dto-converter", "rest-dto-converter-build.gradle.vm", projectGenDto,
					projectDir -> {
						try {
							createSrcRestDtoConvertert(projectGenDto, projectDir, properties, context);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}, soluctionDir, properties, context);
		}

		private void createSrcRestDtoConvertert(final ProjectGenDto projectGenDto, final File projectDir,
				final Properties properties, final VelocityContext context) throws IOException {
			final String packageName = projectGenDto.getPackageName();
			final String sourceClass = "CommumDomain";
			final String destinationClass = "CommumDto";
			context.put("sourceClass", sourceClass);
			context.put("destinationClass", destinationClass);
			final File srcDir = new File(projectDir.getAbsolutePath() + File.separatorChar + "src/main/java");
			srcDir.mkdirs();
			final File packageAppDir = new File(srcDir.getAbsolutePath() + File.separatorChar
					+ packageName.concat(".converter").replace(".", File.separator));
			packageAppDir.mkdirs();
			final File packageConfigDir = new File(srcDir.getAbsolutePath() + File.separatorChar
					+ packageName.concat(".configuration").replace(".", File.separator));
			packageConfigDir.mkdirs();
			final String contentJavaFile = ProjectUtils.processTemplate(projectGenDto, properties, "converter.java.vm",
					context);
			final String contentConfigurationJavaFile = ProjectUtils.processTemplate(projectGenDto, properties,
					"configuration.java.vm", context);
			final File converterJavaFile = new File(packageConfigDir.getAbsolutePath() + File.separatorChar
					+ sourceClass + "To" + destinationClass + "Converter.java");
			ProjectUtils.writeFile(projectGenDto, contentJavaFile, converterJavaFile);
			final File configurationJavaFile = new File(
					packageConfigDir.getAbsolutePath() + File.separatorChar + "ConverterConfiguration.java");
			ProjectUtils.writeFile(projectGenDto, contentConfigurationJavaFile, configurationJavaFile);
			final File resourceDir = new File(projectDir.getAbsolutePath() + File.separatorChar + "src/main/resource");
			resourceDir.mkdirs();
		}
	},
	CoreDomain {
		@Override
		public void createModule(ProjectGenDto projectGenDto, File soluctionDir, Properties properties,
				VelocityContext context) {
			ProjectUtils.createModule(
					"core-domain", "lib-build.gradle.vm", projectGenDto, projectDir -> ProjectUtils
							.createSrc(projectGenDto, projectDir, "domain", "domain.java.vm", properties, context),
					soluctionDir, properties, context);
		}
	},
	CoreFacade {
		@Override
		public void createModule(ProjectGenDto projectGenDto, File soluctionDir, Properties properties,
				VelocityContext context) {
			ProjectUtils.createModule(
					"core-facade", "facade-build.gradle.vm", projectGenDto, projectDir -> ProjectUtils
							.createSrc(projectGenDto, projectDir, "facade", "facade.java.vm", properties, context),
					soluctionDir, properties, context);
		}
	},
	CoreRules {
		@Override
		public void createModule(final ProjectGenDto projectGenDto, final File soluctionDir,
				final Properties properties, final VelocityContext context) {
			ProjectUtils.createModule(
					"core-rules", "rules-build.gradle.vm", projectGenDto, projectDir -> ProjectUtils
							.createSrc(projectGenDto, projectDir, "rules", "rules.java.vm", properties, context),
					soluctionDir, properties, context);
		}
	},
	Entity {
		@Override
		public void createModule(final ProjectGenDto projectGenDto, final File soluctionDir,
				final Properties properties, final VelocityContext context) {
			ProjectUtils.createModule("core-rules", "rules-build.gradle.vm", projectGenDto, projectDir -> ProjectUtils
					.createSrc(projectGenDto, projectDir, "jpa.entities", "entity.java.vm", properties, context),
					soluctionDir, properties, context);
		}
	},
	Repository {
		@Override
		public void createModule(final ProjectGenDto projectGenDto, final File soluctionDir,
				final Properties properties, final VelocityContext context) {
			ProjectUtils.createModule("repository-jpa", "repository-build.gradle.vm", projectGenDto, projectDir -> {
				try {
					createSrcRepository(projectGenDto, projectDir, properties, context);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}, soluctionDir, properties, context);
		}

		private void createSrcRepository(final ProjectGenDto projectGenDto, final File projectDir,
				final Properties properties, final VelocityContext context) throws IOException {
			final String packageName = projectGenDto.getPackageName();
			final String domainClass = projectGenDto.getDomainClass();
			final String sourceClass = domainClass;
			final String destinationClass = domainClass + "Entity";
			context.put("sourceClass", sourceClass);
			context.put("destinationClass", destinationClass);
			final File srcDir = new File(projectDir.getAbsolutePath() + File.separatorChar + "src/main/java");
			srcDir.mkdirs();
			final File packageRepositoryDir = new File(srcDir.getAbsolutePath() + File.separatorChar
					+ packageName.concat(".jpa.repositories").replace(".", File.separator));
			packageRepositoryDir.mkdirs();
			final File packageServiceDir = new File(srcDir.getAbsolutePath() + File.separatorChar
					+ packageName.concat(".jpa.services").replace(".", File.separator));
			packageServiceDir.mkdirs();
			final File packageConfigurationDir = new File(srcDir.getAbsolutePath() + File.separatorChar
					+ packageName.concat(".jpa.configuration").replace(".", File.separator));
			packageConfigurationDir.mkdirs();
			final File packageConverterDir = new File(srcDir.getAbsolutePath() + File.separatorChar
					+ packageName.concat(".jpa.converter").replace(".", File.separator));
			packageConverterDir.mkdirs();
			final String contentJavaFile = ProjectUtils.processTemplate(projectGenDto, properties, "repository.java.vm",
					context);
			final File javaFile = new File(
					packageRepositoryDir.getAbsolutePath() + File.separatorChar + domainClass + "JpaRepository.java");
			ProjectUtils.writeFile(projectGenDto, contentJavaFile, javaFile);
			final String contentServiceJavaFile = ProjectUtils.processTemplate(projectGenDto, properties,
					"service.java.vm", context);
			final File javaServiceFile = new File(
					packageServiceDir.getAbsolutePath() + File.separatorChar + domainClass + "Service.java");
			ProjectUtils.writeFile(projectGenDto, contentServiceJavaFile, javaServiceFile);
			final String contentConfigurationJavaFile = ProjectUtils.processTemplate(projectGenDto, properties,
					"configuration.java.vm", context);
			final File javaConfigurationFile = new File(packageConfigurationDir.getAbsolutePath() + File.separatorChar
					+ domainClass + "Configuration.java");
			ProjectUtils.writeFile(projectGenDto, contentConfigurationJavaFile, javaConfigurationFile);
			final String contentConverterJavaFile = ProjectUtils.processTemplate(projectGenDto, properties,
					"converter.java.vm", context);
			final File javaConverterFile = new File(
					packageConverterDir.getAbsolutePath() + File.separatorChar + domainClass + "Converter.java");
			ProjectUtils.writeFile(projectGenDto, contentConverterJavaFile, javaConverterFile);
			final File resourceDir = new File(projectDir.getAbsolutePath() + File.separatorChar + "src/main/resource");
			resourceDir.mkdirs();
		}
	},
	RestCLient {

		@Override
		public void createModule(ProjectGenDto projectGenDto, File soluctionDir, Properties properties,
				VelocityContext context) {
			ProjectUtils.createModule(
					"rest-client", "rest-client-build.gradle.vm", projectGenDto, projectDir -> ProjectUtils.createSrc(projectGenDto,
							projectDir, "rest.client", "restClient.java.vm", properties, context),
					soluctionDir, properties, context);
		};
	},
	RestDto {

		@Override
		public void createModule(final ProjectGenDto projectGenDto, final File soluctionDir,
				final Properties properties, final VelocityContext context) {
			ProjectUtils.createModule("rest-dto", "rest-dto.gradle.vm", projectGenDto,
					projectDir -> createSrcRestDto(projectGenDto, projectDir, properties, context), soluctionDir,
					properties, context);

		}

	},
	RestServer {

		@Override
		public void createModule(final ProjectGenDto projectGenDto, final File soluctionDir,
				final Properties properties, final VelocityContext context) {
			ProjectUtils.createModule("rest-server", "rest-server-build.gradle.vm", projectGenDto, projectDir -> {
				try {
					createSrcRestServer(projectGenDto, projectDir, properties, context);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}, soluctionDir, properties, context);
		}

		private void createSrcRestServer(final ProjectGenDto projectGenDto, final File projectDir,
				final Properties properties, final VelocityContext context) throws IOException {
			final String packageName = projectGenDto.getPackageName();
			final String domainClass = projectGenDto.getDomainClass();
			final File srcDir = new File(projectDir.getAbsolutePath() + File.separatorChar + "src/main/java");
			srcDir.mkdirs();
			final File packageAppDir = new File(srcDir.getAbsolutePath() + File.separatorChar
					+ packageName.concat(".app").replace(".", File.separator));
			packageAppDir.mkdirs();
			final File packageConfigDir = new File(srcDir.getAbsolutePath() + File.separatorChar
					+ packageName.concat(".configuration").replace(".", File.separator));
			packageConfigDir.mkdirs();
			final File packageControllerDir = new File(srcDir.getAbsolutePath() + File.separatorChar
					+ packageName.concat(".controller").replace(".", File.separator));
			packageControllerDir.mkdirs();
			final File packageAdviceDir = new File(srcDir.getAbsolutePath() + File.separatorChar
					+ packageName.concat(".advice").replace(".", File.separator));
			packageAdviceDir.mkdirs();
			final String contentJavaFile = ProjectUtils.processTemplate(projectGenDto, properties, "restServer.java.vm",
					context);
			final String contentApplicationJavaFile = ProjectUtils.processTemplate(projectGenDto, properties,
					"application.java.vm", context);
			final String contentConfigurationJavaFile = ProjectUtils.processTemplate(projectGenDto, properties,
					"configuration.java.vm", context);
			final String contentAdviceJavaFile = ProjectUtils.processTemplate(projectGenDto, properties,
					"advice.java.vm", context);
			final File javaFile = new File(
					packageControllerDir.getAbsolutePath() + File.separatorChar + domainClass + "RestController.java");
			ProjectUtils.writeFile(projectGenDto, contentJavaFile, javaFile);
			final File adviceJavaFile = new File(
					packageAdviceDir.getAbsolutePath() + File.separatorChar + "Advice.java");
			ProjectUtils.writeFile(projectGenDto, contentAdviceJavaFile, adviceJavaFile);
			final File applicationJavaFile = new File(
					packageAppDir.getAbsolutePath() + File.separatorChar + "Application.java");
			ProjectUtils.writeFile(projectGenDto, contentApplicationJavaFile, applicationJavaFile);
			final File configurationJavaFile = new File(
					packageConfigDir.getAbsolutePath() + File.separatorChar + "RestConfiguration.java");
			ProjectUtils.writeFile(projectGenDto, contentConfigurationJavaFile, configurationJavaFile);
			final File resourceDir = new File(projectDir.getAbsolutePath() + File.separatorChar + "src/main/resource");
			resourceDir.mkdirs();
		}

	},
	Invalid {
		@Override
		public void createModule(final ProjectGenDto projectGenDto, final File soluctionDir,
				final Properties properties, final VelocityContext context) {
			throw new RuntimeException("invalid project type");
		}
	};

	public abstract void createModule(final ProjectGenDto projectGenDto, final File soluctionDir,
			final Properties properties, final VelocityContext context);

	private static void createSrcRestDto(final ProjectGenDto projectGenDto, final File projectDir,
			final Properties properties, final VelocityContext context) {
		final String packageName = projectGenDto.getPackageName();
		final String domainClass = projectGenDto.getDomainClass();
		final File srcDir = new File(projectDir.getAbsolutePath() + File.separatorChar + "src/main/java");
		srcDir.mkdirs();
		final File packageDir = new File(srcDir.getAbsolutePath() + File.separatorChar
				+ packageName.concat(".dto").replace(".", File.separator));
		packageDir.mkdirs();
		try {
			final String contentRequestJavaFile = ProjectUtils.processTemplate(projectGenDto, properties,
					"requestRestDto.java.vm", context);
			final String contentResponseJavaFile = ProjectUtils.processTemplate(projectGenDto, properties,
					"responseRestDto.java.vm", context);
			final File javaRequestFile = new File(
					packageDir.getAbsolutePath() + File.separatorChar + domainClass + "RequestDto.java");
			ProjectUtils.writeFile(projectGenDto, contentRequestJavaFile, javaRequestFile);
			final File javaResponseFile = new File(
					packageDir.getAbsolutePath() + File.separatorChar + domainClass + "ResponseDto.java");
			ProjectUtils.writeFile(projectGenDto, contentResponseJavaFile, javaResponseFile);
			final File resourceDir = new File(projectDir.getAbsolutePath() + File.separatorChar + "src/main/resource");
			resourceDir.mkdirs();
		} catch (IOException e) {
			throw new RuntimeException(
					String.format(" projectGenDto=%s, properties=%s, context=%s", projectGenDto, properties, context),
					e);
		}
	}
}
