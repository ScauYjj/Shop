package com.chinamobile.shop;

/**
 * Created by yjj on 2017/1/17.
 */

public class Constant {

    public static final String WARE = "ware_id" ;

    public static class API{

        public static final String BASE_URL = "http://112.124.22.238:8081/course_api/";

        public static final String HOME_COMPAGIN_URL = BASE_URL + "campaign/recommend";

        public static final String HOT_WARES_URL = BASE_URL + "wares/hot";

        public static final String BANNER=BASE_URL +"banner/query";

        public static final String WARES_LIST=BASE_URL +"wares/list";

        public static final String CATEGORY_LIST=BASE_URL +"category/list";

        public static final String WARES_DETAIL=BASE_URL +"wares/detail.html";
    }
}
