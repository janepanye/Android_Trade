package com.coco3g.caopantx.bean;

import java.util.ArrayList;

/**
 * Created by lisen on 16/9/2.
 */
public class ProDetailDataBean extends BaseData {

    public Pro response;

    public class Pro {

        public String title;

        public String tid;

        public String prono;

        public String catid;

        public String nums;

        public String wpno;

        public String amount;

        public String type;

        public String zhibo;

        public String rate;

        public Order orderset;

        public class Order {

            public String status;

            public String zhiying;

            public String zhisun;

            public String setid;

            public String nums;

        }

        public ArrayList<ChiCang> zhang;

        public ArrayList<ChiCang> die;

        public String order_nums;

        public class ChiCang {

            public String  price;

            public String nums;

            public String rate;

        }

        public String  detail_url;

        public String fx_agree;

    }
}
