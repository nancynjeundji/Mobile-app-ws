package com.nancy.app.ws.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nancy.app.ws.service.UserService;
import com.nancy.app.ws.shared.dto.UserDto;
import com.nancy.app.ws.ui.model.request.UserDetailsRequestModel;
import com.nancy.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping 
	public String getUSer() {
		return "get user was called";
	}

	@PostMapping 
	public UserRest createUSer(@RequestBody UserDetailsRequestModel userDetails) {
		
		UserRest returnedValue = new UserRest();
		
		UserDto userDto = new UserDto();
		
		BeanUtils.copyProperties(userDetails, userDto);
		
		UserDto createdUSer = userService.createUser(userDto);
		
		BeanUtils.copyProperties(createdUSer, returnedValue);
		
		return returnedValue;
	}
	
	@PutMapping 
	public String updateUSer() {
		return "update user was called";
	}
	
	@DeleteMapping 
	public String deleteUSer() {
		return "delete user was called";
	}
}
