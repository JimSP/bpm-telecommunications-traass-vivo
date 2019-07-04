package br.com.interfale.vivo.trass.tools;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

@Component
public class CreateProjectGenDto {

	public ProjectGenDto create(final Scanner scanner, final PrintStream printStream) {
		printStream.println("enter soluction name:");
		final String soluctionName = scanner.nextLine();
		printStream.println(String.format("enter project type:(%s)", Arrays //
				.asList(ProjectType.values()) //
				.stream() //
				.map(mapper -> mapper.getDir()) //
				.reduce((a, b) -> a //
						.concat(",") //
						.concat(b)) //
				.get()));
		final String projectTye = scanner.nextLine();
		printStream.println("enter project prefix:");
		final String projectPrefix = scanner.nextLine();
		printStream.println("enter package name:");
		final String packageName = scanner.nextLine();
		printStream.println("enter domain class name:");
		final String domainClass = scanner.nextLine();
		final List<ProjectAction> projectActions = new ArrayList<>();
		final AtomicBoolean continueAdd = new AtomicBoolean(Boolean.TRUE);
		while (continueAdd.get()) {
			printStream.println("enter module type:");
			final String moduleType = scanner.nextLine();
			ProjectUtils //
					.createProjectAction(moduleType) //
					.forEach(action -> {
						projectActions.add(action);
					});
			printStream.println("continue add module?:[y/N]");
			final String nextLine = scanner.nextLine();
			final Boolean verify = "s".equals(nextLine.toLowerCase()) ||  BooleanUtils.toBoolean(nextLine);
			continueAdd.set(verify);
		}
		return new ProjectGenDto(soluctionName, ProjectType.create(projectTye), projectPrefix, domainClass, packageName, projectActions);
	}

}
