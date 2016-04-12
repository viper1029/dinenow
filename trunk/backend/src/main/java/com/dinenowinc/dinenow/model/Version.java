package com.dinenowinc.dinenow.model;

import com.dinenowinc.dinenow.model.helpers.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="version")
public class Version extends BaseEntity {
	

	private String version;

	private String platform;

  private String forceUpdate;

  private String updateUrl;

	public String getVersion() {
		return version;
	}

	public void setVersion(String versionName) {
		this.version = versionName;
	}

	@Override
	public String toString() {
		return version;
	}

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(final String platform) {
    this.platform = platform;
  }

  public String getForceUpdate() {
    return forceUpdate;
  }

  public void setForceUpdate(final String forceUpdate) {
    this.forceUpdate = forceUpdate;
  }

  public String getUpdateUrl() {
    return updateUrl;
  }

  public void setUpdateUrl(final String updateUrl) {
    this.updateUrl = updateUrl;
  }
}
