package br.com.interfale.vivo.trass.tools;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

import org.apache.velocity.VelocityContext;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

public enum CommandLineOption {
	Help("HELP", "help	> this menu") {
		@Override
		public Boolean execute(final ApplicationContext applicationContext, final ToolCodeGenContext toolCodeGenContext,
				final Scanner scanner, final PrintStream printStream) throws IOException {
			printStream.println("# Options:");
			Arrays //
					.asList(CommandLineOption.values()) //
					.stream().filter(predicate -> predicate.comment != null).forEach(commandLineOption -> {
						printStream.println(commandLineOption.comment);
					});
			return Boolean.TRUE;
		}
	},
	Create("CREATE", "create	> create hexagonal blueprint") {
		@Override
		public Boolean execute(final ApplicationContext applicationContext, final ToolCodeGenContext toolCodeGenContext,
				final Scanner scanner, final PrintStream printStream) throws IOException {
			printStream.println("# Enter project specifications");
			final ProjectGenDto projectGenDto = toolCodeGenContext.getCreateProjectGenDto().create(scanner, printStream);
			final VelocityContext velocityContext = toolCodeGenContext.getCreateVelocityContext().create(projectGenDto);
			final Properties properties = toolCodeGenContext.getCreateProperties().create();

			final File projectDit = toolCodeGenContext.getProjectGen().createSoluction(projectGenDto, properties, velocityContext);
			printStream.println("project dir: " + projectDit.getAbsolutePath());
			return Boolean.TRUE;
		}
	},
	Exit("EXIT", "exit	> close this console") {
		@Override
		public Boolean execute(final ApplicationContext applicationContext, final ToolCodeGenContext toolCodeGenContext,
				final Scanner scanner, final PrintStream printStream) throws IOException {
			SpringApplication.exit(applicationContext, () -> 0);
			return Boolean.FALSE;
		}
	},
	Invalid("", "") {
		@Override
		public Boolean execute(final ApplicationContext applicationContext, final ToolCodeGenContext toolCodeGenContext,
				final Scanner scanner, final PrintStream printStream) throws IOException {
			printStream.println("Invalid Command, try again!");
			return Boolean.TRUE;
		}
	};

	private final String command;
	private final String comment;

	private CommandLineOption(final String command, final String comment) {
		this.command = command;
		this.comment = comment;
	}

	public abstract Boolean execute(final ApplicationContext applicationContext,
			final ToolCodeGenContext toolCodeGenContext, final Scanner scanner, final PrintStream printStream)
			throws IOException;

	public static CommandLineOption parseCommand(final String command) {
		return Arrays //
				.asList(CommandLineOption.values()) //
				.stream() //
				.filter(predicate -> predicate //
						.command //
								.equalsIgnoreCase(command)) //
				.findFirst() //
				.orElse(CommandLineOption.Invalid);
	}
}
