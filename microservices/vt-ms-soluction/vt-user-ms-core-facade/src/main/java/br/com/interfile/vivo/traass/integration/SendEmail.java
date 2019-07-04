package br.com.interfile.vivo.traass.integration;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.domain.User;

@Component
public class SendEmail {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private MessageContentBuilder messageContentBuilder;

	@Value("${interfole.vivo.traass.mail.from:noreply@interfile.com.br}")
	private String from;

	@Async
	public void execute(final User user, final String templateName, final String subject, final String message) {
		try {
			javaMailSender.send(mimeMessage -> {
				createMineMessage(user.getEmail(), templateName, subject, message, mimeMessage);
			});
		} catch (MailException e) {
			throw new RuntimeException(e);
		}
	}

	@Async
	public void sendEmail(final String templateName, final String to, final String subject, final String message) {
		try {
			javaMailSender.send(mimeMessage -> {
				createMineMessage(to, templateName, subject, message, mimeMessage);
			});
		} catch (MailException e) {
			throw new RuntimeException(e);
		}
	}

	private void createMineMessage(final String to, final String templateName, final String subject,
			final String message, final MimeMessage mimeMessage)
			throws MessagingException, UnsupportedEncodingException {

		final Map<String, Object> map = new HashMap<>();
		map.put("message", message);

		final MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
		messageHelper.setFrom(from);
		messageHelper.setTo(to);
		messageHelper.setSubject(subject);
		final String text = messageContentBuilder.build(templateName, map);
		messageHelper.setText(text, true);
	}
}
