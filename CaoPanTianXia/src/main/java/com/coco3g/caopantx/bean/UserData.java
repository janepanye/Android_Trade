package com.coco3g.caopantx.bean;

import java.io.Serializable;

/**
 * Created by MIN on 16/6/16.
 */
public class UserData extends BaseData {
    public UserInfo response;

    public class UserInfo implements Serializable {
        public String userid;
        public String avatar;
        public String nickname;
        public String age;
        public String groupid;
        public String gender;
        public String username;
        public String phone;
        public String amount;
        public String point;
        public String islock;
        public String mamount;
        public String lastlogin;
        public String head;//头像

    }
}

