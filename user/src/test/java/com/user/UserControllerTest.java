package com.user;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.user.model.UserAuthBean;
import com.user.model.UserProfileBean;

public class UserControllerTest {
	public static void main(String[] args) {
		UserControllerTest controllerTest=new UserControllerTest();
		controllerTest.loginUser();
		controllerTest.getUserProfile();
	}
	
	//Test case for Login function
	public void loginUser() {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	RestTemplate restTemplate = new RestTemplate();
	    String url = "http://localhost:8033/users";

	    //{"username":"Supun","password":"S@123"}
	    UserAuthBean loginBean =new UserAuthBean();
	    loginBean.setUsername("Supun");
	    loginBean.setPassword("S@123");    
	    
	    HttpEntity<UserAuthBean> requestEntity = new HttpEntity<UserAuthBean>(loginBean,headers);
        ResponseEntity<String> response=restTemplate.postForEntity(url, requestEntity, String.class);
        
        System.out.println("loginUser : RESPONSE STATUS CODE : "+response.getStatusCodeValue());
        System.out.println("loginUser : RESPONSE PAYLOAD"+ response.getBody());
    }
	
	//Test case for getUserProfile function
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getUserProfile() {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	//{"token": "73c80f52-e60b-40e8-b93f-aad73de472bc"}
    	headers.set("Authorization", "Bearer 73c80f52-e60b-40e8-b93f-aad73de472bc");

    	RestTemplate restTemplate = new RestTemplate();
	    String url = "http://localhost:8033/users/Supun";

	    HttpEntity requestEntity = new HttpEntity (headers);
        ResponseEntity<UserProfileBean> response=restTemplate.exchange(url,HttpMethod.GET,requestEntity,UserProfileBean.class);
        
        System.out.println("getUserProfile : RESPONSE STATUS CODE : "+response.getStatusCodeValue());
        System.out.println("getUserProfile : RESPONSE PAYLOAD"+ "fullname : "+response.getBody().getFullName()+" phoneNo : "+response.getBody().getPhoneNo());
    }
}
