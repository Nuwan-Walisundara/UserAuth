package com.user.dao;

import com.user.model.UserDto;

import org.springframework.dao.EmptyResultDataAccessException;

import com.user.model.TokenBean;
import com.user.model.UserProfileBean;
import com.user.model.UserTokenResponseBean;

/**
 * The Interface IUserDAO.
 */
public interface IUserDAO {

	/**
	 * Adds the users.
	 *
	 * @param userBean
	 * @return userBean
	 */
	public Integer addUserProfile(UserProfileBean userProfileBean);

	public void addUserCredentials(UserDto loginInfoBean);

	/**
	 * check if User exists.
	 *
	 * @param username
	 * @return true, if successful
	 */
	public boolean userExists(String username);

	/**
	 * Checks if authencatedUser.
	 *
	 * @param username
	 * @param password
	 * @return the userProfileId
	 */
	public Integer isAuthencatedUser(String username, String password);

	/**
	 * Update token for user.
	 *
	 * @param userProfileId
	 * @param userToken
	 */
	public Integer updateTokenForUser(TokenBean tokenBean);

	/**
	 * check if Token exists.
	 *
	 * @param userProfileId
	 */
	public String tokenExists(Integer userProfileId);

	/**
	 * Gets the user profile.
	 *
	 * @param userName
	 * @param accessToken
	 * @return the userProfile
	 */
	public UserProfileBean getUserProfile(String userName, String accessToken);

	public String issueToken(Integer tokenId);

	public UserTokenResponseBean getTokenByUsername(String userName);


	public UserDto getUser(final String username, final String password) throws EmptyResultDataAccessException;

}