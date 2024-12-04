package com.mayab.quality.loginunittest.service;

import java.util.ArrayList;
import java.util.List;

import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.model.User;

public class UserService {
	
	private IDAOUser dao;
	
	public UserService(IDAOUser dao) {
		this.dao=dao;
	}
	
	public User createUser(String name, String email, String password) {
		User existingUser = dao.findUserByEmail(email);
	    if (existingUser != null) {
	        // email en uso no se crea usuario
	        return null;
	    }
		
	    if (password.length() >= 8 && password.length() <= 16) {
	        User newUser = new User(name, email, password); //nuevo usuario
	        int id = dao.save(newUser);
	        newUser.setId(id);
	        return newUser;
	    }
		return null;
	}
	public List<User> findAllUsers(){
		List<User> users = new ArrayList<User>();
		users = dao.findAll();
	
		return users;
	}

	public User findUserByEmail(String email) {
		
		return dao.findUserByEmail(email);
	}

public User findUserById(int id) {
		
		return dao.findById(id);
	}
    
    public User updateUser(User user) {
    	User userOld = dao.findById(user.getId());
    	userOld.setName(user.getName());
    	userOld.setPassword(user.getPassword());
    	return dao.updateUser(userOld);
    }

    

    public boolean deleteUser(int id) {
    	return dao.deleteById(id);
    }

}
