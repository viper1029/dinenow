package com.dinenowinc.dinenow.model;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="version_api")
public class Version extends BaseEntity {
	

	private String versionName;

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	@Override
	public String toString() {
		return "Version [versionName=" + versionName + "]";
	}
	

	
}
