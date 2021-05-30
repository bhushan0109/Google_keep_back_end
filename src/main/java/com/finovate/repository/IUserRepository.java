package com.finovate.repository;

import com.finovate.model.UpdatePassDto;
import com.finovate.model.User;

public interface IUserRepository {

	public User save(User newUser);

	public User getUser(String emailId);

	public boolean verify(Long id);

	public boolean updatePassword(UpdatePassDto updatePasswordinformation, long id);

	public User getUser(Long id);
	
	

}
