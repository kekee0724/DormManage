package org.kekee.model;

import lombok.Data;

/**
 * @author cocoa
 */
@Data
public class DormManager {

    private int dormManagerId;
    private String userName;
    private String password;
    private int dormBuildId;
    private String dormBuildName;
    private String name;
    private String sex;
    private String tel;

    public DormManager() {
    }

    public DormManager(String userName, String password,
                       String name, String sex, String tel, int dormBuildId) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.tel = tel;
        this.dormBuildId = dormBuildId;
    }

    public DormManager(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
