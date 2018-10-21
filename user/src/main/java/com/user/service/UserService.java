package com.user.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.dao.IUserDAO;
import com.user.model.ErrorResponseBean;
import com.user.model.UserBean;

/**
 * The Class UserService.
 */
@Service
public class UserService implements IUserService {

	/** Autowire user DAO. */
	@Autowired
	IUserDAO userDAO;

	
	@Override
	public boolean addUsers(UserBean userBean) {
		if (userDAO.userExists(userBean.getUsername())) {
			return false;
		} else {
			userBean=userDAO.addUsers(userBean);
			return true;
		}
	}

	
	@Override
	public Integer isAuthencatedUser(String username, String password) {
		return userDAO.isAuthencatedUser(username,password);
	}

	@Override
	public String updateTokenForUser(Integer userProfileId) {
		String userToken=userDAO.tokenExists(userProfileId);
		if (!userToken.equals("N")) {
			return userToken;
		}else {
			userToken=UUID.randomUUID().toString();
			return userDAO.updateTokenForUser(userProfileId,userToken);
		}
		
	}

	@Override
	public Object getUserProfile(String userName,String accessToken) {
		boolean isUserExist=userDAO.userExists(userName);
		if (isUserExist) {
			return userDAO.getUserProfile(userName,accessToken);
		}else {
			ErrorResponseBean errorBean=new ErrorResponseBean();
			errorBean.setMessage("No Users Found");
			return errorBean;
		}		
	}
}