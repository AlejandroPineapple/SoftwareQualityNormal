package com.mayab.quality.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.util.List;

import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mayab.quality.loginunittest.model.User;
import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.dao.UserMysqlDAO;
import com.mayab.quality.loginunittest.service.UserService;


class UserServiceTest extends DBTestCase {
	
	private IDAOUser dao;
	private UserService service;
	
	public UserServiceTest() {
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,"com.mysql.cj.jdbc.Driver");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,"jdbc:mysql://127.0.0.1:3307/calidadSoftware2024");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME,"root");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD,"123456");	
	
	}
	
	@BeforeEach
	void setup() throws Exception {
		dao = new UserMysqlDAO();
		service = new UserService(dao);
		IDatabaseConnection connection = getConnection(); 
		if (connection == null) {
	        fail("Failed to establish a connection to the database.");
	    } else {
	        System.out.println("Connection established successfully.");
	    }
		
		try {
			DatabaseOperation.TRUNCATE_TABLE.execute(connection,getDataSet());
			DatabaseOperation.CLEAN_INSERT.execute(connection, getDataSet());
			
		} catch(Exception e) {
			fail("Error in setup: "+ e.getMessage()); 
		} finally {
			connection.close(); 
		}
	}
	
	protected IDataSet getDataSet() throws Exception
    {
        return new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/initDB.xml"));
    }

	@Test
	// Create New User HAPPY PATH (DONE)
	void whenSaveUser_test() {
		//Initialization
        User newUser = new User("newUser1", "newEmail@email.com1", "newPassword1");

		//Exercise
		User result = service.createUser("newUser1", "newEmail@email.com1", "newPassword1");
		
		//verify
		assertThat(result.getName(), is(newUser.getName()));
		assertThat(result.getEmail(), is(newUser.getEmail()));
		assertThat(result.getPassword(), is(newUser.getPassword()));
	}
	
	@Test
	// Create User when Email Taken (DONE)
	void whenEmailTaken_test() {
	    String email = "userTaken@email.com";
	    
	    User user1 = service.createUser("newUser", email, "ValidPassword123");

	    User user2 = service.createUser("userTaken", email, "ValidPassword321");

	    assertNull(user2);
	}

	@Test
	// Create User when Short Password (DONE)
	void whenShortPassword_test() {
	    String shortPassword = "corto";

	    User result = service.createUser("userShort", "userShort@email.com", shortPassword);

	    assertNull(result);
	}
	
	@Test
	// Create User when Long Password (DONE)
	void whenLongPassword_test() {
	    String longPassword = "daledaledalenopierdaselritmo1234";
	    
	    User result = service.createUser("userLong", "userLong@email.com", longPassword);

	    assertNull(result);
	}

	@Test
	// Update User only Name and Password
	void whenUpdateUser_test() {
	    User createdUser = service.createUser("OldName", "oldemail@email.com", "oldpassword123");

	    assertThat(createdUser.getName(), is("OldName"));
	    assertThat(createdUser.getPassword(), is("oldpassword123"));

	    createdUser.setName("NewName");
	    createdUser.setPassword("newpassword123");

	    User updatedUser = service.updateUser(createdUser);

	    assertThat(updatedUser.getName(), is("NewName"));
	    assertThat(updatedUser.getPassword(), is("newpassword123"));
	}

	@Test
	// Delete User
	void whenDeleteUser_test() {
	    User newUser = new User("userDelete", "userDelete@email.com", "passDelete123");

	    User createdUser = service.createUser(newUser.getName(), newUser.getEmail(), newUser.getPassword());
	    assertNotNull(createdUser);

	    boolean deleteResult = service.deleteUser(createdUser.getId());

	    assertTrue(deleteResult);

	    User deletedUser = dao.findById(createdUser.getId());
	    assertNull(deletedUser);
	}

	@Test
	// Find All Users
	void whenFindAllUsers_test() {
	    service.createUser("user1", "user1@email.com", "password123");
	    service.createUser("user2", "user2@email.com", "password456");

	    List<User> users = service.findAllUsers();

	    assertThat(users.size(), is(2));
	}
	
	@Test
	// Find User by Email (DONE)
	public void whenFindUserByEmail_test() { 
	    String email = "useremail@email.com";
	    String name = "useremail";
	    String password = "password123";
	    
	    User createdUser = service.createUser(name, email, password);
	    
	    User foundUser = service.findUserByEmail(email);
	    
	    assertEquals(createdUser.getEmail(), foundUser.getEmail());
	}
	
	@Test
	// Find User by ID (DONE)
	void whenFindUserById_test() {
	    User createdUser = service.createUser("userid", "userid@email.com", "password123");

	    User result = service.findUserById(createdUser.getId());

	    assertThat(result.getId(), is(createdUser.getId()));
	}

}
