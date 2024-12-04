package com.mayab.quality.loginunittest.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

class LoginServiceTest {
	
	private IDAOUser idaouser;
	private User user;
	private LoginService login;
	
	public String pass = "prueba123";	
	
	@BeforeEach
	public void setupMock() {
		idaouser = mock(IDAOUser.class);
		user = mock(User.class);
		login = new LoginService(idaouser);
	}
	//when then for findbyusername
	
	@Test
	public void testLoginPassword() {
		when(user.getPassword()).thenReturn(pass);
		when(idaouser.findByUserName(anyString())).thenReturn(user);
		
		assertThat(true, is(login.login("prueba@correo.com", pass)));
		
		
	}
}

	

