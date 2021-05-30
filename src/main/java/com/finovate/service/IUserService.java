package com.finovate.service;

import com.finovate.model.LoginDto;
import com.finovate.model.RegisterDto;
import com.finovate.model.UpdatePassDto;
import com.finovate.model.User;

public interface IUserService {

	public boolean register(RegisterDto UserDto);

	public boolean isVerified(String token);

	public User login(LoginDto lDto);

	public boolean is_User_exists(String email);

	public boolean updatePassword(UpdatePassDto updatePasswordInformation, String token);

	

}
