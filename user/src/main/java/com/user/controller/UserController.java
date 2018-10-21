package com.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.user.model.ErrorResponseBean;
import com.user.model.LoginBean;
import com.user.model.UserBean;
import com.user.model.UserProfileResponseBean;
import com.user.service.IUserService;

/**
 * The Class UserController.
 */
@RestController
@Component
public class UserController {
	
	/** The user service. */
	@Autowired
	private IUserService userService;
	/**
	 * User Login function.
	 *
	 * @param LoginBean 
	 * @return the ResponseEntity
	 */
	@PostMapping("/users")
	public ResponseEntity<String> login(@RequestBody LoginBean loginBean){
		Integer userProfileId;
		userProfileId=authenticate(loginBean.getUsername(), loginBean.getPassword());
		
		if (userProfileId>0) {
			//issue token
			String userToken=issueToken(userProfileId);
			return new ResponseEntity<String>("{\"token\":\""+userToken+"\"}", HttpStatus.OK);
		}else {
			//if username & password incorrect	401
			return new ResponseEntity<String>("{\"error\": \"Invalid username/password\"}", HttpStatus.UNAUTHORIZED);
		}
	}

    /**
     * Authenticate if user exists for username & password
     *
     * @param username
     * @param password
     * @return userProfileId
     */
    private Integer authenticate(String username, String password){
    	Integer userProfileId=userService.isAuthencatedUser(username,password);
    	return userProfileId;
    }

    /**
     * Issue accessToken of user
     *
     * @param userProfileId
     * @return accessToken
     */
    private String issueToken(Integer userProfileId) {
    	return userService.updateTokenForUser(userProfileId);
    }
	
	/**
	 * Gets the user profile for given accessToken
	 *
	 * @param userName
	 * @param authorizationHeaderToken
	 * @return the userProfile or errors if records notExists
	 */
	@GetMapping("/users/{username}")
	public ResponseEntity<Object> getUserProfile(@PathVariable("username") String userName,@RequestHeader(value="Authorization") String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return new ResponseEntity<Object>("Please add token header eg :- Authorization Bearer [YOUR_TOKEN]", HttpStatus.UNAUTHORIZED);
        }else {

			try {
				String accessToken=authorizationHeader.split("Bearer ")[1];
				Object response=userService.getUserProfile(userName,accessToken);
				if (response instanceof UserBean) {
					UserBean userBean= (UserBean)response;
					UserProfileResponseBean userProfileResponseBean=new UserProfileResponseBean();
					userProfileResponseBean.setUsername(userBean.getUsername());
					userProfileResponseBean.setFullName(userBean.getFullName());
					userProfileResponseBean.setPhoneNo(userBean.getPhoneNo());
					//User found	200
					//{"Username”: “abc123”,“Fullname”: “John Doe”,“PhoneNumber”: “+1234567890”}
					return new ResponseEntity<Object>(userProfileResponseBean, HttpStatus.OK);	
				}else {
					ErrorResponseBean errorBean=(ErrorResponseBean)response;
					if (errorBean.getMessage().equals("Invalid Token")) {
						//if incorrect token	401
						//{"message": "Invalid token"}
						return new ResponseEntity<Object>(errorBean, HttpStatus.UNAUTHORIZED);
					}else if (errorBean.getMessage().equals("No Users Found")) {
						//no users found		404
						//{"message": "No users found"}
						return new ResponseEntity<Object>(errorBean, HttpStatus.NOT_FOUND);
					}else{
						return new ResponseEntity<Object>(errorBean, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
			}catch (Exception e) {
				return new ResponseEntity<Object>("Please add token header eg :- Authorization Bearer [YOUR_TOKEN]", HttpStatus.UNAUTHORIZED);
			}
        }
	}
	
	/**
	 * Insert Dummy users (testing purpose)
	 *
	 * @param UserBean
	 * @param UriBuilder for return news inserted record's location
	 * @return the newly added user's profile
	 */
	@PostMapping("/user/add")
	public ResponseEntity<Void> addDummyUsers(@RequestBody UserBean userBean,UriComponentsBuilder builder) {
        boolean flag = userService.addUsers(userBean);
        
        if (flag == false) {
        	return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/users/{username}").buildAndExpand(userBean.getUsername()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
        
	}	
}