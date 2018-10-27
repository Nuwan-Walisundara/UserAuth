package com.user.model;

import java.util.Date;

public class TokenBean {
	private Integer tokenId;
	private Integer userProfileId;
	private String userToken;
	private Date createdTime;
	private long validityPeriod;

	public Integer getTokenId() {
		return tokenId;
	}

	public void setTokenId(Integer tokenId) {
		this.tokenId = tokenId;
	}

	public Integer getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(Integer userProfileId) {
		this.userProfileId = userProfileId;
	}

	public String getUserToken() {
		return userToken;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public long getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(long validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

}
