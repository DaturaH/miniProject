package com.miniProject.dao;

import java.util.List;

import org.codehaus.plexus.component.annotations.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;

import com.miniProject.model.UserFavorHouseForm;
import com.miniProject.model.UserHouseForm;

public class UserHouseFormMongoDao {
	
	@Autowired
	private MongoOperations mongoOps;
	



	
	public int deleteByPrimaryKey(String houseId) {
		UserHouseForm temp = new UserHouseForm();
		temp.setHouseId(houseId);
		mongoOps.remove(temp);
		return 0;
	}

	
	public int insert(UserHouseForm record) {
		mongoOps.insert(record);
		
		return 0;
	}

	
	public int insertSelective(UserHouseForm record) {
		mongoOps.insert(record);
		return 0;
	}

	
	public UserHouseForm selectByPrimaryKey(String houseId) {
		mongoOps.findById(houseId, UserHouseForm.class);
		return null;
	}

	
	public int updateByPrimaryKeySelective(UserHouseForm record) {
		
		return 0;
	}

	
	public int updateByPrimaryKey(UserHouseForm record) {
		mongoOps.save(record);
		return 0;
	}

	
	public List<UserHouseForm> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}
	

	

}
