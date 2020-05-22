package org.kekee.model;

import lombok.Data;

/**
 * @author cocoa
 */
@Data
public class DormBuild {
	private int dormBuildId;
	private String dormBuildName;
	private String detail;

	public DormBuild() {
	}
	
	public DormBuild(String dormBuildName, String detail) {
		this.dormBuildName = dormBuildName;
		this.detail = detail;
	}
}
