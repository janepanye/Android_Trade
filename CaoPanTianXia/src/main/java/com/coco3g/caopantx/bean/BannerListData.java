package com.coco3g.caopantx.bean;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class BannerListData extends BaseData implements Serializable {

    public ArrayList<Banner> response;

    public class Banner implements Serializable {

        public String catid;

        public String id;

        public String thumb;

        public String url;

    }

}
