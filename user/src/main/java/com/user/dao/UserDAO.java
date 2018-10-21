package com.user.dao;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.user.model.ErrorResponseBean;
import com.user.model.UserBean;

/**
 * The Class UserDAO represent DAO layer.
 */
@Transactional
@Repository
public class UserDAO implements IUserDAO {
	
	/** use The SpringJdbcTemplate. */
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	//insert dummy users into table
	@Override
	public UserBean addUsers(UserBean userBean) {
		
		//Add user
		String sql = "INSERT INTO user_profile (username, password, full_name, phone_no) values (?, ?, ?, ?)";
		jdbcTemplate.update(sql, userBean.getUsername(), userBean.getPassword(), userBean.getFullName(),userBean.getPhoneNo());
		
		sql = "SELECT user_profile_id FROM user_profile WHERE username=? and BINARY password = ?";
		int userId = jdbcTemplate.queryForObject(sql, Integer.class,userBean.getUsername() ,userBean.getPassword());
		
		userBean.setUserId(userId);
		
		return userBean;
	}
	
	//check whether users exists for given username
	public boolean userExists(String username) {
		String sql = "SELECT count(*) FROM user_profile WHERE username = ?";
		int count = jdbcTemplate.queryForObject(sql, Integer.class, username);
		
		if(count == 0) {
    		return false;
		} else {
			return true;
		}
	}

	//check whether user exists for username & password pair
	public Integer isAuthencatedUser(String username, String password) {
		try {
			String sql = "SELECT user_profile_id FROM user_profile WHERE username=? and BINARY password = ?";
			Integer userId = jdbcTemplate.queryForObject(sql, Integer.class,username ,password);
			return userId;
		} catch (EmptyResultDataAccessException e) {
			return 0;
		}					
	}
	
	//check whether token exists for given userProfileId
	public String tokenExists(Integer userProfileId) {
		String sql = "select access_token from user_token where user_profile_id=?;";
		
		try {
			String access_token = jdbcTemplate.queryForObject(sql, String.class,userProfileId);
			return access_token;  
		} catch (EmptyResultDataAccessException e) {
			return "N";
		}		
	}	
	
	//update Token for user
	public String updateTokenForUser(Integer userProfileId,String userToken) {
		//Add token for user
		Date nowDateTime = new java.sql.Date(System.currentTimeMillis());
		String sql = "insert into user_token (access_token,user_profile_id,created_time,validity_period) values(?,?,?,?);";
		jdbcTemplate.update(sql, userToken, userProfileId,nowDateTime,3600000);
		return userToken;
		
	}

	//check whether any record exists for given username & token pair
	public Object getUserProfile(String userName,String accessToken) {
		try {
			String sql="select up.* from user_profile up,user_token ut  where up.user_profile_id=ut.user_profile_id and binary ut.access_token=? and up.username=?";
			RowMapper<UserBean> rowMapper = new BeanPropertyRowMapper<UserBean>(UserBean.class);
			UserBean userBean = jdbcTemplate.queryForObject(sql, rowMapper, accessToken,userName);
				return userBean;
		} catch (EmptyResultDataAccessException e) {
			ErrorResponseBean errorBean=new ErrorResponseBean();
			errorBean.setMessage("Invalid Token");
				return errorBean;
		}	
	}

}