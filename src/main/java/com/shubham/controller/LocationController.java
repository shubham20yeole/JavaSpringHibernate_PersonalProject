package com.shubham.controller;

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

import com.shubham.model.Location;
import com.shubham.service.LocationService;

import javax.mail.internet.MimeMultipart; 
@Controller
public class LocationController {
	@Autowired
	private LocationService LocationService;
	
	
//	@RequestMapping(value="/user/location/savelocation", method = RequestMethod.POST)
//	public ModelAndView serviceUserSubmit(
//			@RequestParam(required=false, value="latitude") String latitude,
//			@RequestParam(required=false, value="longitude") String longitude,
//			@RequestParam(required=false, value="state") String state,
//			@RequestParam(required=false, value="country") String country,
//			HttpServletRequest request){
//		ModelAndView mav = new ModelAndView("redirect:/");
//		System.out.println("Here we go");
//		Location loc = new Location();
//		loc.setLatitude(latitude);
//		loc.setLongitude(longitude);
//		loc.setState(state);
//		loc.setCountry(country);
//		LocationService.add(loc);
//		
//		List<Location> Location = LocationService.getAllLocation();
//		mav.addObject("Location", Location);
//		return mav;
//	}

}
