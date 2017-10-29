package com.coco3g.caopantx.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lisen on 16/9/4.
 */
public class TransListDataBean extends BaseData {

    public ArrayList<TransData> response;

    public ArrayList<TransData> data;

    public class TransData implements Serializable {

        public String title;

        public String tid;

        public String prono;

        public String catid;

        public String nums;

        public ArrayList<TransListChiCang> zhang;

        public ArrayList<TransListChiCang> die;

        public String order_nums;

        public class TransListChiCang {

            public String price;

            public String nums;

            public String rate;

        }

        /**
         * socket
         */
        public String lastprice;

        public String perprice;

        public float tprice;

        public String zhangdie;

        public String buyPrice;

        public String buyNums;

        public String salePrice;

        public String saleNums;
    }
}
