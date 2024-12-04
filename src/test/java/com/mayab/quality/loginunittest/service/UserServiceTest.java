package com.mayab.quality.loginunittest.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.model.User;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserServiceTest {
	
	private UserService service;
	private IDAOUser dao;
	private User user;
	private HashMap<Integer, User> db;

	@BeforeEach
	public void setup() throws Exception {
		dao = mock(IDAOUser.class);
		service = new UserService(dao);
		db = new HashMap<Integer, User>();
	}
	
	@Test
	public void whenPasswordShort_test() {
		//initialize
		String shortPass ="123";
		String name = "user1";
		String email = "user@email.com";
		
		//Fake code for findbyuserbyemail & save methods
		when(dao.findUserByEmail(anyString())).thenReturn(user);
		when(dao.save(any(User.class))).thenReturn(0);
		
		//Exercise
		user = service.createUser(name, email, shortPass);
		
		//Verify
		assertThat(user, is(nullValue()));
	}
	
	@Test
	public void whenPasswordLong_test() {
		//initialize
		String longPass = "12345678987654321";
		String name = "user1";
		String email = "user@email.com";
		
		//Fake code for findbyuserbyemail & save methods
		when(dao.findUserByEmail(anyString())).thenReturn(user);
		when(dao.save(any(User.class))).thenReturn(0);
		
		//Exercise
		user = service.createUser(name, email, longPass);
		
		//Verify
		assertThat(user, is(nullValue()));
	}
	
	@Test
	// Create New User EMAIL DUPLICATED
	public void whenEmailTaken_test() {
		String correctPass = "123456789";
		String name = "user1";
		String email = "user@email.com";
		
	    User user = new User( "user2" , email, "987654321");
		when(dao.findUserByEmail(email)).thenReturn(user);
		when(dao.save(any(User.class))).thenReturn(0);
		
		User user1 = service.createUser(name, email, correctPass);
		System.out.println(user1); 

		assertThat(user1, is(nullValue()));
	}
	
	@Test
	// Create New User HAPPY PATH
	void whenSaveUser_test() {
		//Initialization
		int sizeBefore = db.size();
		
		//Fake code for findUserByEmail & save
		when(dao.findUserByEmail((anyString()))).thenReturn(null);
		when(dao.save(any(User.class))).thenAnswer(new Answer<Integer>() {
			
			//Method within the class
			public Integer answer(InvocationOnMock invocation) throws Throwable {
				//set behavior in every invocation
				User arg = (User) invocation.getArguments()[0];
				db.put(db.size()+1, arg);
				System.out.println("Size after = " + db.size() + "\n");
				
				//Return the invoked value
				return db.size()+1;
				}
			}
		);
		//Exercise
		User user = service.createUser("hola", "hola@email.com", "123456789");
		
		//verify
		assertThat(db.size(),is(sizeBefore+1));
			
	}
	
	@Test
	// Update User 
    public void whenUpdateUser_test() {
        // Initialize
        User oldUser = new User("oldUser", "oldEmail", "oldPassword");
        oldUser.setId(1);
        db.put(1, oldUser);
        
        User newUser = new User("newUser", "oldEmail", "newPassword");
        newUser.setId(1);
        
        when(dao.findById(1)).thenReturn(oldUser);
        when(dao.updateUser(any(User.class))).thenAnswer(new Answer<User>() {
        	//Method within the class
			public User answer(InvocationOnMock invocation) throws Throwable {
				//set behavior in every invocation
				User arg = (User) invocation.getArguments()[0];
				db.replace(arg.getId(),arg);
								
				//Return the invoked value
				return db.get(arg.getId());
				}
        });
        // Exercise
        User result = service.updateUser(newUser);
        
        // Verification
        assertThat(result.getName(), is("newUser"));
        assertThat(result.getPassword(), is("newPassword"));

}
	@Test
	// Update User UPDATE PASSWORD
	public void whenUpdatePassword_test() {
	    // Initialize
	    User existingUser = new User("currentUser", "currentEmail", "oldPassword");
	    existingUser.setId(1);
	    db.put(1, existingUser);

	    // New pass
	    String newPassword = "newPassword";

	    when(dao.findById(1)).thenReturn(existingUser);
	    when(dao.updateUser(any(User.class))).thenAnswer(new Answer<User>() {
	        public User answer(InvocationOnMock invocation) throws Throwable {
	            User arg = (User) invocation.getArguments()[0];
	            User userToUpdate = db.get(arg.getId());
	            userToUpdate.setPassword(arg.getPassword());
	            return userToUpdate;
	        }
	    });

	    //exercise
	    existingUser.setPassword(newPassword); 
	    User result = service.updateUser(existingUser);
	    
	    // verification
	    assertThat(result.getPassword(), is(newPassword));
	    assertThat(result.getName(), is("currentUser")); 
	    assertThat(result.getEmail(), is("currentEmail"));
	}

	//test: finduserBydId y finduserByEmail y deleteUser y findAllUsers
	@Test
	// Delete User
	public void whenDeleteUser_test() {
	    // Initialize
	    User existingUser = new User("userElimination", "userElimination@email.com", "elimination123");
	    existingUser.setId(1);
	    db.put(1, existingUser); // simulamos un usuario en la db

	    when(dao.findById(1)).thenReturn(existingUser);
	    when(dao.deleteById(1)).thenAnswer(new Answer<Boolean>() {
	        public Boolean answer(InvocationOnMock invocation) throws Throwable {
	            db.remove(1); // eliminamos usuario
	            return true; // devuelve true (o sea exitoso)
	        }
	    });

	    // Exercise
	    boolean result = service.deleteUser(1);

	    // Verification
	    assertThat(result, is(true)); // verify que se borro el usuario
	    assertThat(db.containsKey(1), is(false)); // verify que el usuario ya no existe 
	}
	
	@Test
	// Find User by Email HAPPY PATH
	public void whenFindUserByEmail_test() {
	    // Initialize
	    User existingUser = new User("userSearch", "userSearch@email.com", "search123");
	    existingUser.setId(1);
	    
	    when(dao.findUserByEmail("userSearch@email.com")).thenReturn(existingUser);

	    // Exercise
	    User result = service.findUserByEmail("userSearch@email.com");

	    // Verify
	    assertThat(result, is(notNullValue())); // verify que encontramos un usuario
	    assertThat(result.getName(), is("userSearch")); // verify que el nombre haga match
	    assertThat(result.getEmail(), is("userSearch@email.com")); // verify que el correo haga match
	}
	
	@Test
	// Find User by Email USER NOT FOUND
	public void whenUserNotFoundByEmail_test() {
	    when(dao.findUserByEmail("userLost@email.com")).thenReturn(null);

	    // Exercise
	    User result = service.findUserByEmail("userLost@email.com");

	    // Verification
	    assertThat(result, is(nullValue())); // verify que no encontramos un usuario
	}
	
	@Test
	// Find All Users
	public void whenFindAllUsers_test() {
	    // Initialize
	    User user1 = new User("user1", "user1@email.com", "password1");
	    user1.setId(1);
	    User user2 = new User("user2", "user2@email.com", "password2");
	    user2.setId(2);
	    
	    List<User> userList = new ArrayList<>();
	    userList.add(user1);
	    userList.add(user2);
	    
	    when(dao.findAll()).thenReturn(userList);

	    // Exercise
	    List<User> result = service.findAllUsers();

	    // Verify
	    assertThat(result, is(not(empty())));
	    assertThat(result.size(), is(2)); // verify encontramos 2 usuarios
	}



}

