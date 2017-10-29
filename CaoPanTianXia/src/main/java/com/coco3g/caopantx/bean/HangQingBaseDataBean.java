package com.coco3g.caopantx.bean;

import java.io.Serializable;

/**
 * Created by lisen on 16/9/2.
 */
public class HangQingBaseDataBean extends BaseData implements Serializable {

    public HangQing data;

    public class HangQing implements Serializable {

        public String lastprice;

        public String perprice;

        public String prono;

        public String zhangdie;

        public String buyPrice;

        public String buyNums;

        public String saleNums;

        public String salePrice;

        public String Preprice;

    }

}
