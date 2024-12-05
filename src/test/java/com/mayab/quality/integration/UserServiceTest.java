package com.mayab.quality.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import java.io.File;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.InputStream;
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
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,"jdbc:mysql://localhost:3307/calidadSoftware2024");
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
        service.createUser("user5", "user5@email.com", "12345678");

        // Verify data in database
        try {
            // This is the full database
            IDatabaseConnection conn = getConnection();
            conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
            IDataSet databaseDataSet = conn.createDataSet();

            ITable actualTable = databaseDataSet.getTable("usuariosCool");

            // Read XML with the expected result
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/resources/addUser.xml"));
            ITable expectedTable = expectedDataSet.getTable("usuariosCool");

            Assertion.assertEquals(expectedTable, actualTable);

        } catch (Exception e) {
            fail("Error in insert test: " + e.getMessage());
        }
    }
	
	@Test
    public void whenEmailTaken_test() {
        // USERE SERVICE VERIFIES THAT EMAIL IS NOT ALREADY IN DB AND THAT THE PASSWORD
        // IS THE APPROPIATE LENGTH
        service.createUser("user4", "user4@email.com", "12345678");

        // Verify data in database
        try {
            // This is the full database
            IDatabaseConnection conn = getConnection();
            conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
            IDataSet databaseDataSet = conn.createDataSet();

            ITable actualTable = databaseDataSet.getTable("usuariosCool");

            // Read XML with the expected result
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
                    .build(new File("src/resources/emailTaken.xml"));
            ITable expectedTable = expectedDataSet.getTable("usuariosCool");

            Assertion.assertEquals(expectedTable, actualTable);

        } catch (Exception e) {
            fail("Error in insert test: " + e.getMessage());
        }
    }

	@Test
    public void whenShortAndLongPassword_test() {
        service.createUser("user4", "user@email.com", "123456"); // short password
        service.createUser("user4", "user@email.com", "01234567890123456789"); // long password

        // Verify data in database
        try {
            // This is the full database
            IDatabaseConnection conn = getConnection();
            conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
            IDataSet databaseDataSet = conn.createDataSet();

            ITable actualTable = databaseDataSet.getTable("usuariosCool");

            // Read XML with the expected result
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
                    .build(new File("src/resources/passwordLength.xml"));
            ITable expectedTable = expectedDataSet.getTable("usuariosCool");

            Assertion.assertEquals(expectedTable, actualTable);

        } catch (Exception e) {
            fail("Error in insert test: " + e.getMessage());
        }
    }

	@Test
	public void testUpdateUser() {
	    // Primero creamos el usuario para asegurarnos de que existe
	    service.createUser("user5", "user5@email.com", "12345678");

	    // Ahora actualizamos el usuario con un nuevo nombre de usuario y correo
	    service.updateUser(new User("newName5", "newEmail5@email.com", "12345678"));

	    // Verificar los datos en la base de datos
	    try {
	        // Esta es la conexión a la base de datos
	        IDatabaseConnection conn = getConnection();
	        conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
	        IDataSet databaseDataSet = conn.createDataSet();

	        ITable actualTable = databaseDataSet.getTable("usuariosCool");

	        // Leer el XML con el resultado esperado
	        IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
	                .build(new File("src/resources/updateUser.xml"));
	        ITable expectedTable = expectedDataSet.getTable("usuariosCool");

	        Assertion.assertEquals(expectedTable, actualTable);

	    } catch (Exception e) {
	        fail("Error en el test de actualización: " + e.getMessage());
	    }
	}

	@Test
    public void testDeleteUser() {
        service.deleteUser(new User(null, null, null));

        // Verify data in database
        try {
            // This is the full database
            IDatabaseConnection conn = getConnection();
            conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
            IDataSet databaseDataSet = conn.createDataSet();

            ITable actualTable = databaseDataSet.getTable("usuariosCool");

            // Read XML with the expected result
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
                    .build(new File("src/resources/deleteUser.xml"));
            ITable expectedTable = expectedDataSet.getTable("usuariosCool");

            Assertion.assertEquals(expectedTable, actualTable);

        } catch (Exception e) {
            fail("Error in insert test: " + e.getMessage());
        }
    }

	@Test
    public void findAll_test() {
        List<User> actualUsers = service.findAllUsers();

        // Verify data in database
        try {
            // This is the full database
            IDatabaseConnection conn = getConnection();
            conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
            IDataSet databaseDataSet = conn.createDataSet();

            ITable actualTable = databaseDataSet.getTable("usuariosCool");

            // Read XML with the expected result
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
                    .build(new File("src/resources/findAll.xml"));
            ITable expectedTable = expectedDataSet.getTable("usuariosCool");

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

        // Verify data in database
        try {
            // This is the full database
            IDatabaseConnection conn = getConnection();
            conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);

            // Read XML with the expected result
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
                    .build(new File("src/resources/findbyEmail.xml"));
            int expectedId = Integer.parseInt((String) expectedDataSet.getTable("usuariosCool").getValue(0, "id"));
            String expectedName = (String) expectedDataSet.getTable("usuariosCool").getValue(0, "name");
            String expectedEmail = (String) expectedDataSet.getTable("usuariosCool").getValue(0, "email");
            String expectedPassword = (String) expectedDataSet.getTable("usuariosCool").getValue(0, "password");
            assertEquals(userToFind.getId(), expectedId);
            assertEquals(userToFind.getName(), expectedName);
            assertEquals(userToFind.getEmail(), expectedEmail);
            assertEquals(userToFind.getPassword(), expectedPassword);

        } catch (Exception e) {
            fail("Error in insert test: " + e.getMessage());
        }
    }

	@Test
    public void testFindByID() {
        User userToFind = service.findUserById(2);

        // Verify data in database
        try {
            // This is the full database
            IDatabaseConnection conn = getConnection();
            conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
            
         // Read XML with the expected result
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("src/resources/findById.xml");
            if (inputStream == null) {
                fail("XML file not found in resources.");
                return;
            }

            // Read XML with the expected result
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
                    .build(new File("src/resources/findById.xml"));
            int expectedId = Integer.parseInt((String) expectedDataSet.getTable("usuariosCool").getValue(0, "id"));
            String expectedName = (String) expectedDataSet.getTable("usuariosCool").getValue(0, "name");
            String expectedEmail = (String) expectedDataSet.getTable("usuariosCool").getValue(0, "email");
            String expectedPassword = null; 
            assertEquals(userToFind.getId(), expectedId);
            assertEquals(userToFind.getName(), expectedName);
            assertEquals(userToFind.getEmail(), expectedEmail);
            assertEquals(userToFind.getPassword(), expectedPassword);

        } catch (Exception e) {
            fail("Error in insert test: " + e.getMessage());
        }
    }

}
