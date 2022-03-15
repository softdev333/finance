package com.mb.finance.service.impl;

import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.util.MimeMessageParser;
import org.springframework.stereotype.Service;

import com.mb.finance.service.TestingService;

@Service
public class TestingServiceImpl implements TestingService {

	@Override
	public void testing() {

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, null);

		Store store;
		try {
			store = session.getStore("imaps");
			store.connect("smtp.gmail.com", "softdev333@gmail.com", "Yoyoyoyo7");

			Folder inbox = store.getFolder("inbox");
			inbox.open(Folder.READ_WRITE);

			// gets unread messages only
			Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

			for (int i = 0, n = messages.length; i < n; i++) {
				Message message = messages[i];
				// checks if the email is from user and if its of "FINANCE"
				if ("FINANCE".equals(message.getSubject())
						&& StringUtils.contains(message.getFrom()[0].toString(), "softdev333@gmail.com")) {
					try {
						String plainMessage = new MimeMessageParser((MimeMessage) message).parse().getPlainContent();
						System.out.print(plainMessage);
					} catch (Exception e) {
						e.printStackTrace();
					}
					message.setFlag(Flag.DELETED, true);
				}
			}

			inbox.close(true);
			store.close();

		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

}
