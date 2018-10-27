package com.user.service;

import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.user.dao.IUserDAO;
import com.user.model.TokenBean;
import com.user.model.UserAuthBean;
import com.user.model.UserDto;
import com.user.model.UserProfileBean;
import com.user.model.UserRequestBean;
import com.user.model.UserTokenResponseBean;
import com.user.util.PasswordUtils;

/**
 * The Class UserService.
 */
@Service
public class UserService implements IUserService {

	/** Autowire user DAO. */
	@Autowired
	IUserDAO userDAO;
	@Autowired
	PasswordUtils passwordUtil;

	@Override
	public boolean addUsers(UserRequestBean userRequestBean) {
		if (userDAO.userExists(userRequestBean.getUsername())) {
			return false;
		} else {

			UserProfileBean userProfileBean = new UserProfileBean();
			userProfileBean.setUsername(userRequestBean.getUsername());
			userProfileBean.setPhoneNo(userRequestBean.getPhoneNo());
			userProfileBean.setFullName(userRequestBean.getFullName());

			Integer userProfileId = userDAO.addUserProfile(userProfileBean);

			TokenBean tokenBean = new TokenBean();
			tokenBean.setUserProfileId(userProfileId);
			tokenBean.setUserToken(UUID.randomUUID().toString());
			tokenBean.setCreatedTime(new java.sql.Date(System.currentTimeMillis()));
			tokenBean.setValidityPeriod(3600000);
			Integer tokenId = userDAO.updateTokenForUser(tokenBean);

			UserDto loginInfoBean = new UserDto();
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
		return userDAO.isAuthencatedUser(username, password);
	}

	@Override
	public UserProfileBean getUserProfile(String userName, String accessToken) {
		return userDAO.getUserProfile(userName, accessToken);
	}

	public UserDto getUserAuthInfo(String username, String password) throws UserNotFoundException {
		try {
			// TODO:Validate username,password and throw respective exceptions

			return userDAO.getUser(username, password);
		} catch (EmptyResultDataAccessException e) {
			// TODO:addd logs
			throw new UserNotFoundException();
		}
	}

	@Override
	public String issueToken(Integer tokenId) {
		return userDAO.issueToken(tokenId);
	}

	public UserTokenResponseBean getTokenByUsername(String userName) {
		return userDAO.getTokenByUsername(userName);
	}

	@Override
	public boolean isValidUser(UserAuthBean userRequestBean) {
		try {
			// Decode the password
			String password = new String(Base64.getDecoder().decode(userRequestBean.getPassword().trim()));
			// Decode the username
			String userName = new String(Base64.getDecoder().decode(userRequestBean.getUsername().trim()));

			getUserAuthInfo(userName, password);
			return true;
		} catch (UserNotFoundException e) {
			return false;
		}

	}

}