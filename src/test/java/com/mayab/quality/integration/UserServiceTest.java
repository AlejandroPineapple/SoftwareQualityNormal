package com.mayab.quality.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import java.io.File;

import java.io.FileInputStream;
import java.util.List;

import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mayab.quality.loginunittest.model.User;
import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.dao.UserMysqlDAO;
import com.mayab.quality.loginunittest.service.UserService;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

class UserServiceTest extends DBTestCase {
	
	private IDAOUser dao;
	private UserService service;
	
	public UserServiceTest() {
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,"com.mysql.cj.jdbc.Driver");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,"jdbc:mysql://localhost:3306/calidad");
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
    public void whenSaveUser_test() {
        service.createUser("user1", "user1@email.com", "12345678");

        try {
            IDatabaseConnection conn = getConnection();
            conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
            IDataSet databaseDataSet = conn.createDataSet();

            ITable actualTable = databaseDataSet.getTable("usuarios");

            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/resources/addUser.xml"));
            ITable expectedTable = expectedDataSet.getTable("usuarios");

            Assertion.assertEquals(expectedTable, actualTable);

        } catch (Exception e) {
            fail("Error in insert test: " + e.getMessage());
        }
    }
		
	@Test
	public void whenEmailTaken_test() {
	    service.createUser("user4", "user3@email.com", "12345678");

	    try {
	        IDatabaseConnection conn = getConnection();
	        conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
	        IDataSet databaseDataSet = conn.createDataSet();

	        ITable actualTable = databaseDataSet.getTable("usuarios");

	        IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
	                .build(new File("src/resources/emailTaken.xml"));
	        ITable expectedTable = expectedDataSet.getTable("usuarios");

	        Assertion.assertEquals(expectedTable, actualTable);

	    } catch (Exception e) {
	        fail("Error en el test de insertar usuario con email repetido: " + e.getMessage());
	    }
	}

	@Test
    public void whenShortAndLongPassword_test() {
        service.createUser("user4", "user@email.com", "123456");
        service.createUser("user4", "user@email.com", "01234567890123456789");

        try {
            IDatabaseConnection conn = getConnection();
            conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
            IDataSet databaseDataSet = conn.createDataSet();

            ITable actualTable = databaseDataSet.getTable("usuarios");

            IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
                    .build(new File("src/resources/passwordLength.xml"));
            ITable expectedTable = expectedDataSet.getTable("usuarios");

            Assertion.assertEquals(expectedTable, actualTable);

        } catch (Exception e) {
            fail("Error in insert test: " + e.getMessage());
        }
    }

	@Test
	public void updateUser_test() {
	    User userToUpdate = service.findUserById(2);

	    assertNotNull(userToUpdate);

	    userToUpdate.setName("newName");  
	    userToUpdate.setPassword("newPassword123");

	    User updatedUser = service.updateUser(userToUpdate);

	    assertNotNull(updatedUser);
	    assertEquals("newName", updatedUser.getName());  
	    assertEquals("newPassword123", updatedUser.getPassword());

	    try {
	        IDatabaseConnection conn = getConnection();
	        conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);

	        IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
	                .build(new File("src/resources/updateUser.xml"));

	        String expectedName = (String) expectedDataSet.getTable("usuarios").getValue(0, "name");
	        String expectedPassword = (String) expectedDataSet.getTable("usuarios").getValue(0, "password");

	        assertEquals(userToUpdate.getName(), expectedName);
	        assertEquals(userToUpdate.getPassword(), expectedPassword);

	    } catch (Exception e) {
	        fail("Error in update test: " + e.getMessage());
	    }
	}

	@Test
	public void deleteUser_test() {
	    service.deleteUser(2);

	    try {
	        IDatabaseConnection conn = getConnection();
	        conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
	        IDataSet databaseDataSet = conn.createDataSet();

	        ITable actualTable = databaseDataSet.getTable("usuarios");

	        IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
	                .build(new File("src/resources/deleteUser.xml"));
	        ITable expectedTable = expectedDataSet.getTable("usuarios");

	        Assertion.assertEquals(expectedTable, actualTable);

	    } catch (Exception e) {
	        fail("Error en la prueba de eliminación: " + e.getMessage());
	    }
	}

	@Test
    public void findAll_test() {
        List<User> actualUsers = service.findAllUsers();

        try {
            IDatabaseConnection conn = getConnection();
            conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
            IDataSet databaseDataSet = conn.createDataSet();

            ITable actualTable = databaseDataSet.getTable("usuarios");

            IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
                    .build(new File("src/resources/findAll.xml"));
            ITable expectedTable = expectedDataSet.getTable("usuarios");

            Assertion.assertEquals(expectedTable, actualTable);
            assertEquals(actualUsers.size(), actualTable.getRowCount());

            for (int i = 0; i < actualUsers.size(); i++) {
                User user = actualUsers.get(i);

                int expectedId = Integer.parseInt(expectedTable.getValue(i, "id").toString());
                String expectedName = expectedTable.getValue(i, "name").toString();
                String expectedPassword = expectedTable.getValue(i, "password").toString();
                String expectedEmail = expectedTable.getValue(i, "email").toString();

                assertEquals(expectedId, user.getId());
                assertEquals(expectedName, user.getName());
                assertEquals(expectedPassword, user.getPassword());
                assertEquals(expectedEmail, user.getEmail());
            }

        } catch (Exception e) {
            fail("Error in insert test: " + e.getMessage());
        }
    }
	
	@Test
    public void whenFindByEmail_test() {
        User userToFind = service.findUserByEmail("user1@email.com");

        try {
            IDatabaseConnection conn = getConnection();
            conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);

            IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
                    .build(new File("src/resources/findByEmail.xml"));
            int expectedId = Integer.parseInt((String) expectedDataSet.getTable("usuarios").getValue(0, "id"));
            String expectedName = (String) expectedDataSet.getTable("usuarios").getValue(0, "name");
            String expectedEmail = (String) expectedDataSet.getTable("usuarios").getValue(0, "email");
            String expectedPassword = (String) expectedDataSet.getTable("usuarios").getValue(0, "password");
            assertEquals(userToFind.getId(), expectedId);
            assertEquals(userToFind.getName(), expectedName);
            assertEquals(userToFind.getEmail(), expectedEmail);
            assertEquals(userToFind.getPassword(), expectedPassword);

        } catch (Exception e) {
            fail("Error in insert test: " + e.getMessage());
        }
    }
	
	@Test
	public void findById_test() {
	    User userToFind = service.findUserById(2);

	    try {
	        IDatabaseConnection conn = getConnection();
	        conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);

	        IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
	                .build(new File("src/resources/findById.xml"));
	        int expectedId = Integer.parseInt((String) expectedDataSet.getTable("usuarios").getValue(0, "id"));
	        String expectedName = (String) expectedDataSet.getTable("usuarios").getValue(0, "name");
	        String expectedEmail = (String) expectedDataSet.getTable("usuarios").getValue(0, "email");
	        String expectedPassword = (String) expectedDataSet.getTable("usuarios").getValue(0, "password"); 

	        assertEquals(userToFind.getId(), expectedId);
	        assertEquals(userToFind.getName(), expectedName);
	        assertEquals(userToFind.getEmail(), expectedEmail);
	        assertEquals(userToFind.getPassword(), expectedPassword);

	    } catch (Exception e) {
	        fail("Error in insert test: " + e.getMessage());
	    }
	}


}
