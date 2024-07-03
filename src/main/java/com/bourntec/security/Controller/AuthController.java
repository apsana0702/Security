package com.bourntec.security.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bourntec.security.JwtUtil;
import com.bourntec.security.UserEntity;
import com.bourntec.security.UserService;
import com.bourntec.security.UserProfile.UserProfile;

@Controller
@RequestMapping("/auth")
public class AuthController {
	 @Autowired
	    private UserService userService;

	    @Autowired
	    private AuthenticationManager authenticationManager;

	    @Autowired
	    private JwtUtil jwtUtil;

	    @Autowired
	    private UserDetailsService userDetailsService;
	    
	    @GetMapping("/registerpage")
	    public String showRegistrationForm(Model model) {
	        model.addAttribute("user", new UserEntity());
	        return "register";
	    }
	    
	    

	    @PostMapping("/register")
	    public String register(@ModelAttribute UserEntity user, Model model) {
	        String message;
	        try {
	            userService.save(user);
	            message = "User registered successfully!";
	        } catch (Exception e) {
	            message = "User registration failed!";
	        }
	        model.addAttribute("message", "User registered successfully");
	        return "login"; // Redirect to login page after successful registration
	    }

     
	    
	    
	    @GetMapping("/loginpage")
	    public String loginPage() {
	    	return "login";
	    }
	    
	    @PostMapping("/login")
	    public String login(@ModelAttribute UserEntity user) {
	    	Authentication authentication = authenticationManager.authenticate(
		            new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
	    	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    	String tok=jwtUtil.generateToken(userDetails);
	        System.out.println(tok);
	    	return "success";
	    }
	    
	    @GetMapping("/profile")
	    public ResponseEntity<?> getProfile() {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String username = authentication.getName();
	        UserProfile profile = userService.getProfileByUsername(username);
	        if (profile != null) {
	            return ResponseEntity.ok(profile);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
//	    @GetMapping("/validate")
//	    public Boolean validateToken(@RequestParam String token, @RequestParam String username) {
//	    	System.out.println("Validate");
//	        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//	        return jwtUtil.validateToken(token, userDetails);
//	    }
}

class AuthRequest {
    private String username;
    private String password;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

   
}
