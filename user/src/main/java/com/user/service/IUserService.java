package com.user.service;

import com.user.model.UserAuthBean;
import com.user.model.UserDto;
import com.user.model.UserProfileBean;
import com.user.model.UserRequestBean;
import com.user.model.UserTokenResponseBean;

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
	public boolean addUsers(UserRequestBean userRequestBean);

	/**
	 * Checks if is authenticated user.
	 *
	 * @param username
	 * @param password
	 */
	public Integer isAuthencatedUser(String username, String password);

	/**
	 * Gets the user profile.
	 *
	 * @param userName
	 * @param accessToken
	 * @return userProfile
	 */
	public UserProfileBean getUserProfile(String userName, String accessToken);

	public UserDto getUserAuthInfo(String username, String password) throws UserNotFoundException;

	public String issueToken(Integer tokenId);

	public UserTokenResponseBean getTokenByUsername(String userName);

	public boolean isValidUser(UserAuthBean userRequestBean);

}
