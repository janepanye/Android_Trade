package com.coco3g.caopantx.bean;

import java.io.Serializable;

/**
 * Created by lisen on 2017/6/12.
 */

public class UserInfoDataBean extends BaseData {

    public UserInfoData response;

    public class UserInfoData implements Serializable {
        public UserData.UserInfo userinfo;
    }

//    public class UserInfo implements Serializable {
//        public String userid;
//        public String avatar;
//        public String nickname;
//        public String age;
//        public String groupid;
//        public String gender;
//        public String username;
//        public String phone;
//        public String amount;
//        public String point;
//        public String islock;
//        public String mamount;
//        public String lastlogin;
//    }

}
