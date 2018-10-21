package com.user.service;

import com.user.model.UserBean;

/**
 * The Interface IUserService.
 */
public interface IUserService {

	/**
	 * Adds the users.
	 *
	 * @param userBean
	 * @return true, if successful
	 */
	public boolean addUsers(UserBean userBean);

	/**
	 * Checks if is authenticated user.
	 *
	 * @param username
	 * @param password
	 */
	public Integer isAuthencatedUser(String username, String password);

	/**
	 * Update token for user.
	 *
	 * @param userProfileId
	 */
	public String updateTokenForUser(Integer userProfileId);

	/**
	 * Gets the user profile.
	 *
	 * @param userName
	 * @param accessToken
	 * @return userProfile
	 */
	public Object getUserProfile(String userName, String accessToken);
	

}
