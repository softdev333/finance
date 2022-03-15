package com.mb.finance.service.impl;

import java.time.LocalDate;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mb.finance.entities.BacklogData;
import com.mb.finance.entities.User;
import com.mb.finance.repository.UserRepository;
import com.mb.finance.service.BacklogDataService;
import com.mb.finance.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BacklogDataService backlogDataService;

	@Override
	public User saveUser(User user) {		
		return userRepository.save(user);
	}

	@Override
	public User getUser(String userId) {
		return userRepository.findById(userId).get();
	}

	@Override
	public String authenticate(String username, String password) {
		User user = userRepository.findByUserName(username);
		if(user.checkPassword(password))
			return user.getId();
		else 
			return null;
	}

	@Override
	public void loadEmailData(String userId) {
		
		User user = getUser(userId);
		
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
			store.connect("smtp.gmail.com", user.getEmail(), user.getEmailPassword());

			Folder inbox = store.getFolder("inbox");
			inbox.open(Folder.READ_WRITE);

			// gets unread messages only
			Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

			for (int i = 0, n = messages.length; i < n; i++) {
				Message message = messages[i];
				// checks if the email is from user and if its of "FINANCE"
				if ("FINANCE".equals(message.getSubject())
						&& StringUtils.contains(message.getFrom()[0].toString(), user.getEmail())) {
					try {
						String plainMessage = new MimeMessageParser((MimeMessage) message).parse().getPlainContent();
						
						//create backlogData object and save it before processing
						BacklogData bd = new BacklogData();
						bd.setIsProcessed(false);
						bd.setRecord(plainMessage);
						bd.setUserId(user.getId());
						bd.setCreationDate(LocalDate.now());
						
						backlogDataService.saveBacklogData(bd);
						
						message.setFlag(Flag.DELETED, true);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			inbox.close(true);
			store.close();

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	

}
