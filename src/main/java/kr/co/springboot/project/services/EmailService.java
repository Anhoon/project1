package kr.co.springboot.project.services;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import kr.co.springboot.project.util.Util;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
	
    @Value("${spring.mail.username}")
    private String userName;
    
    public void sendMail(Map<String, Object> map) throws MessagingException {
        String email =  Util.objToStr(map.get("email"));
        String obid = Util.objToStr(map.get("obid"));
		StringBuffer emailcontent = new StringBuffer();
		emailcontent.append("<!DOCTYPE html>");
		emailcontent.append("<html>");
		emailcontent.append("<head>");
		emailcontent.append("</head>");
		emailcontent.append("<body>");
		emailcontent.append(
				" <div" 																																																	+ 
				"	style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 400px; height: 600px; border-top: 4px solid #02b875; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">"		+ 
				"	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">"																															+ 
				"		<span style=\"font-size: 15px; margin: 0 0 10px 3px;\">XCUBE</span><br />"																													+ 
				"		<span style=\"color: #02b875\">메일인증</span> 안내입니다."																																				+ 
				"	</h1>\n"																																																+ 
				"	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">"																													+ 
				"		아래 <b style=\"color: #02b875\">'메일 인증'</b> 버튼을 클릭하여 회원가입을 완료해 주세요.<br />"																													+
				"	</p>"																																																	+ 
				"	<a style=\"color: #FFF; text-decoration: none; text-align: center;\""																																	+
				"	href=\"http://localhost:8080/api/user/certified?obid=" + obid + "&email=" + email + "\" target=\"_blank\">"														+ 
				"		<p"																																																	+
				"			style=\"display: inline-block; width: 210px; height: 45px; margin: 30px 5px 40px; background: #02b875; line-height: 45px; vertical-align: middle; font-size: 16px;\">"							+ 
				"			메일 인증</p>"																																														+ 
				"	</a>"																																																	+
				"	<div style=\"border-top: 1px solid #DDD; padding: 5px;\"></div>"																																		+
				" </div>"
		);
		emailcontent.append("</body>");
		emailcontent.append("</html>");

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    	MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
		helper.setFrom(userName);
    	helper.setTo(email);
    	helper.setSubject("[XCUBE 이메일 인증]");
    	helper.setText(emailcontent.toString(), true);
        javaMailSender.send(mimeMessage);

	}

	public void sendPassWordMail(Map<String, Object> map) throws MessagingException {
        String email = Util.objToStr(map.get("email"));
        String password = Util.objToStr(map.get("password"));
		StringBuffer emailcontent = new StringBuffer();
		emailcontent.append("<!DOCTYPE html>");
		emailcontent.append("<html>");
		emailcontent.append("<head>");
		emailcontent.append("</head>");
		emailcontent.append("<body>");
		emailcontent.append(
				" <div" 																																																	+ 
				"	style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 400px; height: 600px; border-top: 4px solid #02b875; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">"		+ 
				"	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">"																															+ 
				"		<span style=\"font-size: 15px; margin: 0 0 10px 3px;\">XCUBE</span><br />"																													+ 
				"		<span style=\"color: #02b875\">임시비밀번호</span> 안내입니다."																																				+ 
				"	</h1>\n"																																																+ 
				"	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">"																													+ 
				"		아래 <b style=\"color: #02b875\">'임시비밀번호로'</b> 로그인 해주세요.<br />"																													+
				"	</p>"																																																	+ 
				"	<a style=\"color: #FFF; text-decoration: none; text-align: center;\">"														+ 
				"		<p"																																																	+
				"			style=\"display: inline-block; width: 400px; height: 45px; margin: 30px 5px 40px; background: #02b875; line-height: 45px; vertical-align: middle; font-size: 12px;\">"							+ 
				"			"+password+"</p>"																																														+ 
				"	</a>"																																																	+
				"	<div style=\"border-top: 1px solid #DDD; padding: 5px;\"></div>"																																		+
				" </div>"
		);
		emailcontent.append("</body>");
		emailcontent.append("</html>");

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    	MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
		helper.setFrom(userName);
    	helper.setTo(email);
    	helper.setSubject("[XCUBE 임시비밀번호]");
    	helper.setText(emailcontent.toString(), true);
        javaMailSender.send(mimeMessage);

	}

}