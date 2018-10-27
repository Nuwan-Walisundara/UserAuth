package com.user.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.user.model.ErrorResponseBean;
import com.user.model.LoginInfoBean;
import com.user.model.TokenBean;
import com.user.model.UserProfileBean;
import com.user.model.UserTokenResponseBean;

/**
 * The Class UserDAO represent DAO layer.
 */
@Transactional
@Repository
public class UserDAO implements IUserDAO {

	/** use The SpringJdbcTemplate. */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	// insert dummy users into table
	@Override
	public Integer addUserProfile(UserProfileBean userProfileBean) {
		// Add user
		String sql = "INSERT INTO user_profile (username, full_name, phone_no) values (?, ?, ?)";
		jdbcTemplate.update(sql, userProfileBean.getUsername(), userProfileBean.getFullName(),userProfileBean.getPhoneNo());

		sql = "SELECT user_profile_id FROM user_profile WHERE username=?";
		Integer userId = jdbcTemplate.queryForObject(sql, Integer.class, userProfileBean.getUsername());

		return userId;
	}

	// update Token for user
	public Integer updateTokenForUser(TokenBean tokenBean) {
		// Add token for user
		String sql = "insert into user_token (access_token,user_profile_id,created_time,validity_period) values(?,?,?,?)";
		jdbcTemplate.update(sql, tokenBean.getUserToken(), tokenBean.getUserProfileId(), tokenBean.getCreatedTime(),tokenBean.getValidityPeriod());

		sql = "SELECT token_id FROM user_token WHERE binary access_token=? AND user_profile_id=?";
		Integer token_id = jdbcTemplate.queryForObject(sql, Integer.class, tokenBean.getUserToken(),
				tokenBean.getUserProfileId());

		return token_id;

	}

	public void addUserCredentials(LoginInfoBean loginInfoBean) {
		// Add credentials for user
		String sql = "insert into user_auth (token_id,username,password,salt_value) values(?,?,?,?)";
		jdbcTemplate.update(sql, loginInfoBean.getTokenId(), loginInfoBean.getUsername(), loginInfoBean.getPassword(),loginInfoBean.getSaltValue());
	}

	// check whether users exists for given username
	public boolean userExists(String username) {
		String sql = "SELECT count(*) FROM user_profile WHERE username = ?";
		int count = jdbcTemplate.queryForObject(sql, Integer.class, username);

		if (count == 0) {
			return false;
		} else {
			return true;
		}
	}

	// check whether user exists for username & password pair
	public Integer isAuthencatedUser(String username, String password) {
		try {
			String sql = "SELECT user_profile_id FROM user_profile WHERE username=? and BINARY password = ?";
			Integer userId = jdbcTemplate.queryForObject(sql, Integer.class, username, password);
			return userId;
		} catch (EmptyResultDataAccessException e) {
			return 0;
		}
	}

	// check whether token exists for given userProfileId
	public String tokenExists(Integer userProfileId) {
		String sql = "select access_token from user_token where user_profile_id=?;";

		try {
			String access_token = jdbcTemplate.queryForObject(sql, String.class, userProfileId);
			return access_token;
		} catch (EmptyResultDataAccessException e) {
			return "N";
		}
	}

	// check whether any record exists for given username & token pair
	public UserProfileBean getUserProfile(String userName, String accessToken) {
			String sql = "select up.username,up.full_name,up.phone_no from user_token ut,user_profile up where ut.user_profile_id=up.user_profile_id and up.username=? and binary ut.access_token=?;";
			RowMapper<UserProfileBean> rowMapper = new BeanPropertyRowMapper<UserProfileBean>(UserProfileBean.class);
			UserProfileBean userProfileBean = jdbcTemplate.queryForObject(sql, rowMapper, userName, accessToken);
			return userProfileBean;
	}

	@Override
	public Object getUserAuthInfo(String username) {
		try {
			String sql = "SELECT token_id,password,salt_value FROM user_auth where username=?";
			RowMapper<LoginInfoBean> rowMapper = new BeanPropertyRowMapper<LoginInfoBean>(LoginInfoBean.class);
			LoginInfoBean userBean = jdbcTemplate.queryForObject(sql, rowMapper, username);
			return userBean;
		} catch (EmptyResultDataAccessException e) {
			ErrorResponseBean errorBean = new ErrorResponseBean();
			errorBean.setMessage("NOUSER");
			return errorBean;
		}
	}

	@Override
	public String issueToken(Integer tokenId) {
		String sql = "SELECT access_token FROM user_token where token_id=?";
		return jdbcTemplate.queryForObject(sql, String.class, tokenId);
	}

	@Override
	public UserTokenResponseBean getTokenByUsername(String userName) {
		try {
			String sql = "select ut.access_token,ua.username from user_token ut,user_auth ua where ut.token_id=ua.token_id and ua.username = ?";
			RowMapper<UserTokenResponseBean> rowMapper = new BeanPropertyRowMapper<UserTokenResponseBean>(UserTokenResponseBean.class);
			UserTokenResponseBean userTokenResponse= jdbcTemplate.queryForObject(sql, rowMapper, userName);
			return userTokenResponse;			
		}catch (EmptyResultDataAccessException e) {
			return null;
		}	

	}

}