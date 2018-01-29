package com.fieldez.android.domain.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public abstract class BaseModel implements Serializable{
	@DatabaseField(generatedId=true,allowGeneratedIdInsert=true)
	private int id;
	@DatabaseField(canBeNull=false) private boolean dirty;
	@DatabaseField(dataType=DataType.DATE_STRING) private Date createdDate;
	@DatabaseField(dataType=DataType.DATE_STRING) private Date syncDateTime;
	@DatabaseField private int serverId;
	@DatabaseField private int version;
	@DatabaseField private boolean posting;

	private String userId;
	private String userPassword;
	private User user;
	
	public abstract Map<String, String> toKeyValuePair();

	public BaseModel() {}
	
	public BaseModel(User user) {
		setUserId(user.getExternalId());
		setUserPassword(user.getPassword());
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		setUserId(user.getExternalId());
		setUserPassword(user.getPassword());
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public boolean isDirty() {
		return dirty;
	}
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
	public Date getSyncDateTime() {
		return syncDateTime;
	}

	public void setSyncDateTime(Date syncDateTime) {
		this.syncDateTime = syncDateTime;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public boolean isPosting() {
		return posting;
	}

	public void setPosting(boolean posting) {
		this.posting = posting;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		BaseModel other = (BaseModel) obj;
		if (id != other.id)
			return false;
		return true;
	}

	
}
