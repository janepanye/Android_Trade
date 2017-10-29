package com.coco3g.caopantx.bean;

/**
 * Created by lisen on 16/9/6.
 */
public class OrderDetailDataBean extends BaseData {

    public OrderInfo response;

    public class OrderInfo {

        public String pay_code;

        public String acc_no;

        public String id_card;

        public String id_holder;

        public String mobile;

        public String trans_id;

        public String txn_amt;

        public String trade_date;

        public String commodity_name;

        public String page_url;

        public String return_url;
    }
}
