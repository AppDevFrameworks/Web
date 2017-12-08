package com.phillies.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.phillies.domain.Account;
import com.phillies.domain.Flower;
import com.phillies.domain.FlowerPackage;
import com.phillies.domain.Order;
import com.phillies.domain.Account;
import com.phillies.repository.FlowerRepo;
import com.phillies.repository.PackageRepo;

@Controller
public class MainController {

	@Autowired
	FlowerRepo flowerRepo;
	
	@Autowired
	PackageRepo packageRepo;

	@RequestMapping(value="/", method=RequestMethod.GET)
	public String index(Model model, Order order) {
		model.addAttribute("order", order);
		List<FlowerPackage> packages =  packageRepo.findAll();
		model.addAttribute("packages", packages);
		return "index";
	}
	
	@RequestMapping(value="/order", method=RequestMethod.POST)
	public String orderSubmit(Order order, BindingResult bindingResult, Model model) throws MalformedURLException, IOException {
		if (bindingResult.hasErrors()) {
			return "index";
		}
		model.addAttribute("firstName", order.getFirstName());
		model.addAttribute("lastName", order.getLastName());
		model.addAttribute("status", orderMore("Red Flowers", 1000));
		return "orderReturn";
	}
	
	public String orderMore(String item, int amount) throws MalformedURLException, IOException {
		String url = "http://localhost:8090/getOrder";
		String charset = "UTF-8";
		String query = String.format("item=%s&amount=%s", 
			     URLEncoder.encode(item, charset), 
			     URLEncoder.encode(amount+"", charset));
		URLConnection connection = new URL(url).openConnection();
		connection.setDoOutput(true); // Triggers POST.
		connection.setRequestProperty("Accept-Charset", charset);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

		try (OutputStream output = connection.getOutputStream()) {
		    output.write(query.getBytes(charset));
		}

		InputStream response = connection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(response));
		StringBuilder result = new StringBuilder();
		String line;
		while((line = reader.readLine()) != null) {
		    result.append(line);
		}
		System.out.println(result.toString());
		return result.toString();
	}
}