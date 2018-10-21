package com.user.dao;

import com.user.model.UserBean;

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
	public UserBean addUsers(UserBean userBean);

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
	public String updateTokenForUser(Integer userProfileId,String userToken);

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
	public Object getUserProfile(String userName,String accessToken);

}