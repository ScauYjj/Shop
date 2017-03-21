package com.chinamobile.shop;

/**
 * Created by yjj on 2017/1/17.
 */

public class Constant {

    public static final String WARE = "ware_id" ;

    public static final String USER_JSON="user_json";

    public static final String TOKEN="token";

    public  static final String DES_KEY="Cniao5_123456";

    public  static final int REQUEST_CODE=0;

    public static final String MOB_APP_KEY = "1c47116cd1cc8";

    public static final String MOB_APP_SECRET = "0c5155d87fa153576d3f2c7e41e175da";

    public static class API{

        public static final String BASE_URL = "http://112.124.22.238:8081/course_api/";

        public static final String HOME_COMPAGIN_URL = BASE_URL + "campaign/recommend";

        public static final String HOT_WARES_URL = BASE_URL + "wares/hot";

        public static final String BANNER=BASE_URL +"banner/query";

        public static final String WARES_LIST=BASE_URL +"wares/list";

        public static final String CATEGORY_LIST=BASE_URL +"category/list";

        public static final String WARES_DETAIL=BASE_URL +"wares/detail.html";

        public static final String LOGIN=BASE_URL +"auth/login";

        public static final String REG=BASE_URL +"auth/reg";

        public static final String USER_DETAIL=BASE_URL +"user/get?id=1";
    }
}
