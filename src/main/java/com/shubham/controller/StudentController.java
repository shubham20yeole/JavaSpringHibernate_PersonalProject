package com.shubham.controller;
import com.sendgrid.SendGrid;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.shubham.model.Blog;
import com.shubham.model.ListItems;
import com.shubham.model.Location;
import com.shubham.service.LocationService;
import com.shubham.model.SmsServiceUser;
import com.shubham.model.Student;
import com.shubham.model.ToDoList;
import com.shubham.controller.Example;
import com.shubham.service.SmsServiceUserService;
import com.shubham.service.ToDoListService;
import com.shubham.service.BlogService;
import com.shubham.service.ListItemsService;
import com.shubham.service.StudentService;
import com.twilio.sdk.TwilioRestException;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart; 
@Controller
public class StudentController {
	@Autowired
	private StudentService studentService;

	@Autowired
	private SmsServiceUserService smsServiceUserService;
	
	@Autowired
	private LocationService LocationService;
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private ToDoListService ToDoListService;
	
	@Autowired
	private ListItemsService ListItemsService;

	@RequestMapping("/")
	public ModelAndView setupForm(Map<String, Object> map){
		Student student = new Student();
		ModelAndView mav = new ModelAndView("index");
		List<Location> Location = LocationService.getAllLocation();
		List<SmsServiceUser> users = smsServiceUserService.getAllUsers();
		List<ToDoList> todolist = ToDoListService.getAllToDoList();
		List<ListItems> items = ListItemsService.getAllListItems();
		List<Blog> blogitems = blogService.getAllBlog();
//		mav.addObject("items", items);
		
		mav.addObject("blogitems", blogitems);
		mav.addObject("todolist", todolist);
		mav.addObject("userSize", users.size());
		mav.addObject("Location", Location);
		mav.addObject("users", users);
		
		return mav;
	}

	@RequestMapping(value="smsservice", method = RequestMethod.GET)
	public ModelAndView smsservice(@RequestParam(required=false, value="From") String From,
			@RequestParam(required=false, value="Body") String Body,
			HttpServletRequest request, HttpServletResponse response){

		SmsServiceUser user = new SmsServiceUser();
		String dateAndTime = new SimpleDateFormat("yyyy/MM/dd - h:mm a").format(new Date());
		String dateString=null, timeString =null; int count = 0;
		for (String retval: dateAndTime.split("-")) {
			if(count==0) dateString = retval;
			if(count==1) timeString = retval;
			count++;
		}
		String name = "Anonymous";
		if(From == "9292168151" || From=="+19292168151") name = "Dr. Scharff";
		user.setName(name);
		user.setPhone(From);
		user.setTimeOn(timeString);
		user.setDateOn(dateString);
		smsServiceUserService.add(user);
		String pattern="[\\s]";
		String replace="";

		Pattern p= Pattern.compile(pattern);
		Matcher m=p.matcher(Body);

		Body=m.replaceAll(replace);
		System.out.println(Body); 
		TwilioServlet hh = new TwilioServlet();
		try {
			hh.service(request, response, Body, From);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ModelAndView mav = new ModelAndView("twilio");
		System.out.println("twilio");
		return mav;
	}

	@RequestMapping(value="collectData", method = RequestMethod.POST)
	public ModelAndView serviceUserSubmit(
			@RequestParam(required=false, value="name") String name,
			@RequestParam(required=false, value="phone") String phone,
			HttpServletRequest request){
		ModelAndView mav = new ModelAndView("redirect:/");
		String platformUrl = "https://shubhamtwilio.herokuapp.com/";
		String githubUrl = "https://github.com/shubham20yeole/Twilio-Part-2";
		String dateAndTime = new SimpleDateFormat("yyyy/MM/dd - h:mm a").format(new Date());
		String dateString=null, timeString =null; int count = 0;
		for (String retval: dateAndTime.split("-")) {
			if(count==0) dateString = retval;
			if(count==1) timeString = retval;
			count++;
		}
		if(phone.equals("9292168151") || phone.equals("+19292168151")) name = "Dr. Scharff";
		else name = name;
		SmsServiceUser user = new SmsServiceUser();
		user.setName(name);
		user.setPhone(phone);
		user.setTimeOn(timeString);
		user.setDateOn(dateString);
		smsServiceUserService.add(user);
		Example ex = new Example();
		try {
			ex.SMS(phone, "This is the SMS service designed by Shubham Yeole from Pace University Computer Science major. \n\n This SMS service assists guardians of high school students to learn about their children progress reports at school. \n\nIt also inform parents about the new facilities, events and future goals of school. \n\nPlease select the option given below. \n\nA Progress Report.\nB New facilities in school.\nC School future goals.\nD School events. \n\n For example, reply with A or B or C or D to this message\n\n View platform at "+platformUrl+"\nGitHub URL: "+githubUrl);
		} catch (TwilioRestException e) {
			e.printStackTrace();
		}
		List<SmsServiceUser> users = smsServiceUserService.getAllUsers();
		String sentStatus = "Message sent to "+name+" on "+phone;
		mav.addObject("sentStatus", sentStatus);
		mav.addObject("userSize", users.size());
		mav.addObject("users", users);
		return mav;
	}

	@RequestMapping(value="sms.do", method = RequestMethod.GET)
	public ModelAndView sms(
			@RequestParam(required=false, value="phone") String phone,
			@RequestParam(required=false, value="msg") String msg,
			HttpServletRequest request){

		ModelAndView mav = new ModelAndView("sendsms");
		String drmsg = "Hello, My name is Shubham Yeole. This message is to confirm that you visited my project on Heroku Server. I am reachable at +1 (201)-887 5323. Thank you.";
		Example ex = new Example();
		try {
			ex.SMS(phone, drmsg);
			if(msg==null) msg="Message sent to DR Scharff from ";
			ex.SMS("2018875323", msg+" "+phone);
		} catch (TwilioRestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sentStatus = "<i class='material-icons'>check_circle</i> Message sent to "+phone;
		mav.addObject("sentStatus");
		return mav;
	}
	
	@RequestMapping(value="finttodolist", method = RequestMethod.POST)
	public ModelAndView findToDoList(
			@RequestParam(required=false, value="listid") int listid,
			HttpServletRequest request){

		ModelAndView mav = new ModelAndView("index");
		
		List<ToDoList> list = ToDoListService.getListById(listid);
		List<ListItems> items = ListItemsService.getItemsByListId(listid);
		List<Location> Location = LocationService.getAllLocation();
		List<SmsServiceUser> users = smsServiceUserService.getAllUsers();
		List<ToDoList> todolist = ToDoListService.getAllToDoList();
		String listname = list.get(0).getFullname();
		List<Blog> blogitems = blogService.getAllBlog();
		
		mav.addObject("blogitems", blogitems);
		mav.addObject("listname", listname);
		mav.addObject("listIdToUpdate", listid);
		mav.addObject("todolist", todolist);
		mav.addObject("userSize", users.size());
		mav.addObject("Location", Location);
		mav.addObject("users", users);
		mav.addObject("list", list);
		mav.addObject("items", items);
		return mav;
	}

	@RequestMapping(value="test", method = RequestMethod.GET)
	public ModelAndView smsservice(HttpServletRequest request, HttpServletResponse response){
		ModelAndView mav = new ModelAndView("test");
		System.out.println("test");
		return mav;
	}

	@RequestMapping(value="test2", method = RequestMethod.GET)
	public ModelAndView test2(HttpServletRequest request, HttpServletResponse response){
		ModelAndView mav = new ModelAndView("test2");
		List<Location> Location = LocationService.getAllLocation();
		mav.addObject("Location", Location);
		try {
			sendMail("shubham20.yeole@gmail.com", "Message test", "body test");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mav;
	}
	
	@RequestMapping(value="test3", method = RequestMethod.GET)
	public void test3(HttpServletRequest request, HttpServletResponse response){
		
		String accesstoken = "";
//		FacebookClient fbClient = new DfaultFacebookClient(accesstoken);
	}

	@RequestMapping(value="savetodolist", method = RequestMethod.POST)
	public ModelAndView saveToDoList(
			@RequestParam(required=false, value="listofitems") String ListItem[],
			@RequestParam(required=false, value="fullname") String fullname,
			@RequestParam(required=false, value="latitude") String latitude,
			@RequestParam(required=false, value="longitude") String longitude,
			@RequestParam(required=false, value="state") String state,
			@RequestParam(required=false, value="country") String country,
			@RequestParam(required=false, value="city") String city,
			@RequestParam(required=false, value="updatestatus") String updatestatus,
			HttpServletResponse response, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("redirect:/");
		System.out.println(latitude+" | "+longitude+" | "+state+" | "+country+" | "+city);
		List<Location> Location = LocationService.getAllLocation();
		
		if(updatestatus!=null && updatestatus.equals("yes")){
			String updateid = request.getParameter("updateid");
			ToDoListService.deleteToDoListById(Integer.valueOf(updateid));
			ListItemsService.deleteItemsByListId(Integer.valueOf(updateid));
		}
		
		ToDoList toDolist = new ToDoList();
		toDolist.setCity(city);
		toDolist.setState(state);
		toDolist.setCountry(country);
		toDolist.setDatecreated(getDate());
		toDolist.setTimecreated(getTime());
		toDolist.setLatitude(latitude);
		toDolist.setLongitude(longitude);
		toDolist.setFullname(fullname);
		
		ToDoListService.add(toDolist);
		
		for(int i=0;i<ListItem.length;i++){
			ListItems items = new ListItems();
			items.setListid(toDolist.getListid());
			items.setItemname(ListItem[i]);
			ListItemsService.add(items);
		}

		mav.addObject("Location", Location);
		return mav;
	}

	//	******************************** BLOG SECTION *****************************************

	@RequestMapping(value="saveblog", method = RequestMethod.POST)
	public ModelAndView saveBlog(
			@RequestParam(required=false, value="author") String author,
			@RequestParam(required=false, value="blogcontent") String blogcontent,
			HttpServletRequest request){
		ModelAndView mav = new ModelAndView("redirect:/");
		System.out.println("In Save Blog method");
		Blog blog = new Blog();
		blog.setAuthor(author);
		blog.setBlogcontent(blogcontent);
		blog.setDatecreated(getDate());
		blog.setTimecreated(getTime());
		blogService.add(blog);

		return mav;
	}

	public static String getTime(){

		String dateAndTime = new SimpleDateFormat("yyyy/MM/dd - h:mm a").format(new Date());
		String dateString=null, timeString =null; int count = 0;
		for (String retval: dateAndTime.split("-")) {
			if(count==0) dateString = retval;
			if(count==1) timeString = retval;
			count++;
		}
		return timeString;
	}

	public static String getDate(){

		String dateAndTime = new SimpleDateFormat("yyyy/MM/dd - h:mm a").format(new Date());
		String dateString=null, timeString =null; int count = 0;
		for (String retval: dateAndTime.split("-")) {
			if(count==0) dateString = retval;
			if(count==1) timeString = retval;
			count++;
		}
		return dateString;
	}
	private void sendMail(String to, String subject, String emailBody) throws MessagingException{
	    final String username = "shubham20.yeole@gmail.com";
	    final String password = "Shubham4194";

	    Properties props = new Properties();
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "587");

	    Session session = Session.getInstance(props,
	            new javax.mail.Authenticator() {
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(username, password);
	        }
	    });

	    try {

	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress("shubham20.yeole@gmail.com"));
	        message.setRecipients(Message.RecipientType.TO,
	                InternetAddress.parse(to));
	        message.setSubject(subject);
	        message.setContent(emailBody, "text/html; charset=utf-8");

	        Transport.send(message);

	        System.out.println("Done");

	    } catch (MessagingException e) {
	        throw new RuntimeException(e);
	    }
	}
}
