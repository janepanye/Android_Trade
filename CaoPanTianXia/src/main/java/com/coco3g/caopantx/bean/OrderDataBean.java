package com.coco3g.caopantx.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lisen on 2016/10/25.
 */

public class OrderDataBean extends BaseData {

    public OrderData data;

    public class OrderData implements Serializable {

        public ArrayList<ProDetailDataBean.Pro.ChiCang> zhang;

        public ArrayList<ProDetailDataBean.Pro.ChiCang> die;

        public String order_nums;

        public String amount;
//        public class ZhangDie implements Serializable {
//
//            public String price;
//
//            public String nums;
//
//            public String rate;
//
//        }
    }
}
