package com.user.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.user.model.UserTokenResponseBean;
import com.user.service.IUserService;
import com.user.status.UserTokenStatus;

@Component
public class ValidateHeaderTokenUtil implements IValidateHeaderToken{
	
	@Autowired
	private IUserService userService;
	
	@Override
	public UserTokenStatus validateAuthToken(String userName, String accessToken) {
		UserTokenResponseBean responseBean = userService.getTokenByUsername(userName);
		if (responseBean!=null) {
			if (responseBean.getAccessToken().equals(accessToken)) {
				return UserTokenStatus.SUCCESS;
			}else {
				return UserTokenStatus.INVALIDTOKEN;	
			}			
		}else {
			return UserTokenStatus.NOUSERFOUND;
		}
	}
	
}
