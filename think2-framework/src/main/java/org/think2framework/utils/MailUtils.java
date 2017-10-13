package org.think2framework.utils;

import org.think2framework.exception.SimpleException;
import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * 发送邮件
 */
public class MailUtils {

	/**
	 * 发送文本信息邮件
	 *
	 * @param host
	 *            SMTP邮件服务器host
	 * @param port
	 *            SMTP邮件服务器端口
	 * @param user
	 *            发送邮件邮箱地址
	 * @param password
	 *            发送邮件邮箱密码
	 * @param subject
	 *            邮件主题
	 * @param to
	 *            发送给哪些邮箱，多个用,隔开
	 * @param content
	 *            邮件内容
	 * @throws Exception
	 *             异常
	 */
	public static void sendTextMail(String host, Integer port, String user, String password, String subject, String to,
			String content) {
		sendMail(host, port, user, password, subject, to, null, null, content, "text");
	}

	/***
	 * 发送html邮件
	 *
	 * @param host
	 *            smtp邮件服务器host
	 * @param port
	 *            smtp邮件服务器端口
	 * @param user
	 *            发送邮件邮箱地址
	 * @param password
	 *            发送邮件邮箱密码
	 * @param subject
	 *            邮件主题
	 * @param to
	 *            发送给哪些邮箱，多个用,隔开
	 * @param content
	 *            邮件内容
	 * @throws Exception
	 *             异常
	 */
	public static void sendMail(String host, Integer port, String user, String password, String subject, String to,
			String content) {
		sendMail(host, port, user, password, subject, to, null, null, content, "html");
	}

	/**
	 * 发送邮件
	 *
	 * @param host
	 *            smtp邮件服务器host
	 * @param port
	 *            smtp邮件服务器端口
	 * @param user
	 *            发送邮件邮箱地址
	 * @param password
	 *            发送邮件邮箱密码
	 * @param subject
	 *            邮件主题
	 * @param to
	 *            发送给哪些邮箱，多个用,隔开
	 * @param cc
	 *            抄送哪些邮箱，多个,隔开
	 * @param bcc
	 *            密送哪些邮箱，多个,隔开
	 * @param content
	 *            邮件内容
	 * @param type
	 *            发送类型 text或者html
	 */
	public static void sendMail(String host, Integer port, String user, String password, String subject, String to,
			String cc, String bcc, String content, String type) {
		try {
			// 配置发送邮件的环境属性
			final Properties props = new Properties();
			// 表示SMTP发送邮件，需要进行身份验证 SSL
			MailSSLSocketFactory sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.ssl.enable", "true");
			props.put("mail.smtp.ssl.socketFactory", sf);
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", port);
			// 构建授权信息，用于进行SMTP进行身份验证
			Authenticator authenticator = new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					// 用户名、密码
					return new PasswordAuthentication(user, password);
				}
			};
			// 使用环境属性和授权信息，创建邮件会话
			Session mailSession = Session.getInstance(props, authenticator);
			// 创建邮件消息
			MimeMessage message = new MimeMessage(mailSession);
			// 设置发件人
			message.setFrom(new InternetAddress(user));
			// 设置收件人
			message.setRecipient(RecipientType.TO, new InternetAddress(to));
			// 设置抄送
			if (null != cc) {
				message.setRecipient(RecipientType.CC, new InternetAddress(cc));
			}
			// 设置密送，其他的收件人不能看到密送的邮件地址
			if (null != bcc) {
				message.setRecipient(RecipientType.BCC, new InternetAddress(bcc));
			}
			// 设置邮件标题
			message.setSubject(subject);
			// 设置邮件的内容体
			if (type.toLowerCase().equals("text")) {
				message.setText(content);
			} else {
				// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
				MimeMultipart mainPart = new MimeMultipart();
				// 创建一个包含HTML内容的MimeBodyPart
				MimeBodyPart html = new MimeBodyPart();
				// 设置HTML内容
				html.setContent(content, "text/html; charset=utf-8");
				mainPart.addBodyPart(html);
				// 将MiniMultipart对象设置为邮件内容
				message.setContent(mainPart);
			}
			// 发送邮件
			Transport.send(message);
		} catch (Exception e) {
			throw new SimpleException(e);
		}
	}
}
