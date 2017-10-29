package com.coco3g.caopantx.bean;

import java.io.Serializable;

/**
 * Created by lisen on 16/9/2.
 */
public class KViewUrlDataBean extends BaseData {

    public Url response;

    public class Url implements Serializable {

        public String minute;

        public String kline;

        public String pankou;

        public String zhibo;

        public String chicang;

        public String jiesuan;

        public String buyzhang;

        public String buydie;

        public String minute_kline5;

    }

}
