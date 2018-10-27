package com.user.util;

import com.user.status.UserTokenStatus;

public interface IValidateHeaderToken {
	
	public UserTokenStatus validateAuthToken(String userName, String accessToken);

}