package com.nancy.app.ws.service.impl;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nancy.app.ws.UserRepository;
import com.nancy.app.ws.io.entity.UserEntity;
import com.nancy.app.ws.service.UserService;
import com.nancy.app.ws.shared.Utils;
import com.nancy.app.ws.shared.dto.UserDto;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDto createUser(UserDto userDto) {
		
		//pour verifier si le champs existe deja dans la base et renvoyer un message user friendly
		//NOTE: on peut mettre la contrainte "unique = true" dans la column concernée de la classe entity
		if(userRepository.findByEmail(userDto.getEmail()) != null) throw new RuntimeException("Record already exists"); //TODO créer mes propres checked exceptions
		 
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(userDto, userEntity);
		
		//userEntity.setEncryptedPassword("test");
		//utilisation de Spring Secu pour crypter le mot de passe de l'utilisateur avant de la storer
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		
		//userEntity.setUserId("testUSerId"); je génère manuellement un secure public user id
		//NOTE: verifier ce que ca donnerait avec  UUID.randomUUID().toString();
		userEntity.setUserId(utils.genererateUserId(30));
		
		//
		userEntity.setEmailVerificationStatus(false);
	
		UserEntity storedUSerDetails = userRepository.save(userEntity);
		UserDto returned1value = new UserDto();
		
		BeanUtils.copyProperties(storedUSerDetails, returned1value);
		
		return returned1value;
	}

	
	//Vu que UserService étend UserDetailService de SpringSécu, cette methode est rajoutée à l'implem
	//TODO: il faut mettre quoi dedans? 
	//Reponse: la gestion du login, ici l'utilisateur se loggue avec l'email
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);
		
		if (userEntity == null) throw new UsernameNotFoundException(email);
		
		//TODO collection d'authorities???
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}

}
