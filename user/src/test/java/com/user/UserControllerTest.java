package com.user;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.user.model.LoginBean;
import com.user.model.UserProfileResponseBean;

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
	    String url = "http://localhost:8033/user";

	    LoginBean loginBean =new LoginBean();
	    loginBean.setUsername("Vikum");
	    loginBean.setPassword("V@123");
        
	    HttpEntity<LoginBean> requestEntity = new HttpEntity<LoginBean>(loginBean,headers);
        ResponseEntity<String> response=restTemplate.postForEntity(url, requestEntity, String.class);
        
        System.out.println("loginUser : RESPONSE STATUS CODE : "+response.getStatusCodeValue());
        System.out.println("loginUser : RESPONSE PAYLOAD"+ response.getBody());
    }
	
	//Test case for getUserProfile function
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getUserProfile() {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.set("Authorization", "Bearer b915d94c-8c6c-4c90-bcc1-c7b3628870fb");

    	RestTemplate restTemplate = new RestTemplate();
	    String url = "http://localhost:8033/users/Vikum";

	    HttpEntity requestEntity = new HttpEntity (headers);
        ResponseEntity<UserProfileResponseBean> response=restTemplate.exchange(url,HttpMethod.GET,requestEntity,UserProfileResponseBean.class);
        
        System.out.println("getUserProfile : RESPONSE STATUS CODE : "+response.getStatusCodeValue());
        System.out.println("getUserProfile : RESPONSE PAYLOAD"+ "fullname : "+response.getBody().getFullName()+" phoneNo : "+response.getBody().getPhoneNo());
    }
}
