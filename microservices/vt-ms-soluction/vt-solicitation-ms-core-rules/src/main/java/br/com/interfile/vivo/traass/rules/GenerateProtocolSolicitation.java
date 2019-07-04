package br.com.interfile.vivo.traass.rules;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class GenerateProtocolSolicitation {

	private final Random random = new Random(System.currentTimeMillis());

	public String createProtocolNumber() {
		return new SimpleDateFormat("yyMMddHHmmss").format(new Date(System.currentTimeMillis())) + random.nextInt(999);
	}
}
