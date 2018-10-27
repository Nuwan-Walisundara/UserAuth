package com.user.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.dao.IUserDAO;
import com.user.model.LoginInfoBean;
import com.user.model.TokenBean;
import com.user.model.UserProfileBean;
import com.user.model.UserRequestBean;
import com.user.model.UserTokenResponseBean;

/**
 * The Class UserService.
 */
@Service
public class UserService implements IUserService {

	/** Autowire user DAO. */
	@Autowired
	IUserDAO userDAO;

	
	@Override
	public boolean addUsers(UserRequestBean userRequestBean) {
		if (userDAO.userExists(userRequestBean.getUsername())) {
			return false;
		} else {

			UserProfileBean userProfileBean=new UserProfileBean();
			userProfileBean.setUsername(userRequestBean.getUsername());
			userProfileBean.setPhoneNo(userRequestBean.getPhoneNo());
			userProfileBean.setFullName(userRequestBean.getFullName());
			
			Integer userProfileId=userDAO.addUserProfile(userProfileBean);
			
			TokenBean tokenBean=new TokenBean();
			tokenBean.setUserProfileId(userProfileId);
			tokenBean.setUserToken(UUID.randomUUID().toString());
			tokenBean.setCreatedTime(new java.sql.Date(System.currentTimeMillis()));
			tokenBean.setValidityPeriod(3600000);
			Integer tokenId=userDAO.updateTokenForUser(tokenBean);
			
			LoginInfoBean loginInfoBean=new LoginInfoBean();
			loginInfoBean.setUsername(userRequestBean.getUsername());
			loginInfoBean.setPassword(userRequestBean.getPassword());
			loginInfoBean.setSaltValue(userRequestBean.getSaltValue());
			loginInfoBean.setTokenId(tokenId);
			userDAO.addUserCredentials(loginInfoBean);
			
			return true;
		}
	}

	
	@Override
	public Integer isAuthencatedUser(String username, String password) {
		return userDAO.isAuthencatedUser(username,password);
	}

	

	@Override
	public UserProfileBean getUserProfile(String userName,String accessToken) {
		return userDAO.getUserProfile(userName,accessToken);
	}


	@Override
	public Object getUserAuthInfo(String username) {
		return userDAO.getUserAuthInfo(username);
	}


	@Override
	public String issueToken(Integer tokenId) {
		return userDAO.issueToken(tokenId);
	}

	public UserTokenResponseBean getTokenByUsername(String userName) {
		return userDAO.getTokenByUsername(userName);
	}


}