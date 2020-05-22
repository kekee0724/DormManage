package org.kekee.model;

import lombok.Data;

/**
 * @author cocoa
 */
@Data
public class Admin {
    private int adminId;
    private String userName;
    private String password;
    private String name;
    private String sex;
    private String tel;

    public Admin() {

    }

    public Admin(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
