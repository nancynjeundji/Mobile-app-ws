package com.nancy.app.ws.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	// @GetMapping("/{id}")
	/** le endpoint retourne les infos en xml*/
	// @GetMapping(path="/{id}", produces = MediaType.APPLICATION_XML_VALUE)
	/** le endpoint retourne les infos en XML ou Json*/
	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest getUSer(@PathVariable String id) {

		UserRest returnedValue = new UserRest();

		UserDto userDto = userService.getUserByUserId(id);

		BeanUtils.copyProperties(userDto, returnedValue);

		return returnedValue;
	}

	//@PostMapping
	/**ce endpoint accepte  et retourne les infos en xml ou json  */
	@PostMapping(  
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, 
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
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
