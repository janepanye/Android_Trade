package com.coco3g.caopantx.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class VersionUpdateDataBean extends BaseData {

    public ReturnData response;

    public class ReturnData implements Serializable {

        public String number;

        public String version;

        public String code;

        public String updatelog;

        public String appurl;

    }

}
