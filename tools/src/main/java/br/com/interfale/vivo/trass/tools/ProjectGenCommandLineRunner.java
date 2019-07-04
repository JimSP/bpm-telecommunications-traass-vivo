package br.com.interfale.vivo.trass.tools;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ProjectGenCommandLineRunner implements CommandLineRunner {

	@Autowired
	private ToolCodeGenContext toolCodeGenContext;

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	@Async
	public void run(String... args) throws Exception {

		try (final Scanner scanner = new Scanner(System.in)) {
			CommandLineOption.Help.execute(applicationContext, toolCodeGenContext, scanner, System.out);
			final AtomicBoolean atomicBoolean = new AtomicBoolean(Boolean.TRUE);
			while (atomicBoolean.get()) {
				try {
					System.out.println("CodeGen>");
					final String command = scanner.nextLine();
					atomicBoolean.set( //
							CommandLineOption //
									.parseCommand(command) //
									.execute(applicationContext, //
											toolCodeGenContext, //
											scanner, //
											System.out));
				} catch (Throwable e) {
					System.err.println(e);
				} finally {
					System.out.println();
				}
			}
			System.out.println(":-) Bye bye babay!");
		}
	}
}
