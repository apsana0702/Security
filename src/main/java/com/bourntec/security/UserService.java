package com.bourntec.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bourntec.security.UserProfile.UserProfile;

@Service
public class UserService {
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public UserEntity register(UserEntity user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepo.save(user);
	}
	
	public UserEntity findByUsername(String username) {
        return userRepo.findByUsername(username);
    }
	
	public UserProfile getProfileByUsername(String username) {
        // Fetch user entity from the database
        UserEntity user = userRepo.findByUsername(username);

        // Convert user entity to UserProfile
        if (user != null) {
            return new UserProfile(user.getUsername());
        }
        return null;
    }

	public void save(UserEntity user) {
		// TODO Auto-generated method stub
		user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
	}
	
}
