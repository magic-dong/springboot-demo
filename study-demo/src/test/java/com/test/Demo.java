package com.test;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

import com.lzd.learn.StudyDemoApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=StudyDemoApplication.class)
public class Demo {
	
	@Autowired 
	private JavaMailSender mailSender;
	
	public void sendMail(String emailForm,String[] emailTo,String title,String context){
	    MimeMessage mimeMessage = mailSender.createMimeMessage();
	   // mimeMessage.getSession().setDebug(true);
	    MimeMessageHelper helper;
	    try {
	        helper = new MimeMessageHelper(mimeMessage, true);
	        helper.setFrom(emailForm);
	        helper.setTo(emailTo);
	        helper.setSubject(title);//主题
	        helper.setText(context);//正文
	        mailSender.send(mimeMessage);
	    } catch (MessagingException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}
	
	@Test
	public void  test(){
		sendMail("xxxx@163.com",new String[]{"xxxx@163.com"}, "录取通知", "magic dong");
		
//		try {
//			Properties props = new Properties();
//	        props.put("mail.smtp.host", "smtp.163.com");//指定邮件的发送服务器地址
//	        props.put("mail.smtp.auth", "true");//服务器是否要验证用户的身份信息
//
//	        Session session = Session.getInstance(props);//得到Session
//	        session.setDebug(true);//代表启用debug模式，可以在控制台输出smtp协议应答的过程
//
//
//	        //创建一个MimeMessage格式的邮件
//	        MimeMessage message = new MimeMessage(session);
//
//	        //设置发送者
//	        Address fromAddress = new InternetAddress("lzd1842867004@163.com");//邮件地址
//	        message.setFrom(fromAddress);//设置发送的邮件地址
//	        //设置接收者
//	        Address toAddress = new InternetAddress("lzd1842867004@163.com");//要接收邮件的邮箱
//	        message.setRecipient(RecipientType.TO, toAddress);//设置接收者的地址
//
//	        //设置邮件的主题
//	        message.setSubject("录取通知");
//	        //设置邮件的内容
//	        message.setText("magic dong");
//	        //保存邮件
//	        message.saveChanges();
//
//
//	        //得到发送邮件的服务器(这里用的是smtp服务器)
//	        Transport transport = session.getTransport("smtp");
//
//	        //发送者的账号连接到smtp服务器上  @163.com可以不写
//	        transport.connect("smtp.163.com","lzd1842867004@163.com","lzd789068"); 
//	        //发送信息
//	        transport.sendMessage(message, message.getAllRecipients());
//	        //关闭服务器通道
//	        transport.close();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
	}
}
