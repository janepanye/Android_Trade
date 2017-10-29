package com.coco3g.caopantx.bean;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lisen on 16/9/5.
 */
public class ProChildListDataBean extends BaseData {

    public ArrayList<ProBaseData> response;


    public class ProBaseData implements Serializable {

        public String thumb_code;

        public String title;

        public String description;

        public String timedesc;

        public String baozhengjin;

        public String catid;

        public String tid;

        public String code_color;

        public String status;

        public String img;
    }
}
