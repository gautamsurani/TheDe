package thedezine.android.utils;

/**
 * Created by theonetech28 on 4/27/2016.
 */
public class Constant {

    public static String BASE_URL = "http://www.thedezine.in/android/index.php?view="; // - Production

    public static String ABOUT = "about";

    public static String HOME_PAGE = "home&appToken=";

    public static String DEVICE_ID = "&appDevice=";

    public static String DEVICE_EMAIL = "&appEmail=";

    public static String SERVICE_DETAILS = "&appEmail=";

    public static String CONTACT_FORM = "contact_form&name=";

    public static String CONTACT = "contact";

    public static String PORTFOLIO = "portfolio&pagecode=";

    public static String TEAM = "team&pagecode=";

    public static String CLIENT = "clients&pagecode=";

    public static String SERVICES = "services";

    public static String INQUIRY = "contact_enq&service=";

    public static String SERVICES_DETAILS = "services_detail&service_id=";

    public static String LOGIN_URL = "login&mobile_no=";
    public static String LOGIN_URL_TWO = "login2&mobile_no=";
    public static String LOGIN_URL_RESEND = "resend_otp2&phone=";
    public static String REGISTER_URL = "register&name=";

    public static String FACEBOOKLOGIN_URL = "facebook_login&email=";

    public static String FORGOTPASSWORD_URL = "forgot_password&name=";

    public static String HOME_URL = "home";

    public static String BESTNEW_URL = "new_product&page=new";

    public static String CATEGORY_URL = "category";

    public static String SUB_CATEGORY_URL = "subcategory&catID=";

    public static String PRODUCT_LIST_URL = "product&catID=";

    public static String PRODUCT_DETAIL_URL = "product_detail&productID=";

    public static String SEARCH_URL = "search";

    public static String RELATED_PRODUCT_URL = "related_product&productID=";

    public static String REFER_URL = "share_msg";

    public static String OFFER_URL = "offer_list";

    public static String HELP_URL = "help";

    public static String WISHLIST_ADD_URL = "wish_list&page=wish_data_id&userID=";

    public static String WISHLIST_URL = "wish_list&page=wish_proucts_list&userID=";

    public static String STORELIST_URL = "address";

    public static String UPDATEPROFILE_URL = "change_info&page=update_info&userID=";

    public static String STOT_URL = "http://www.nationalhandloomcorp.com/demo/nhc/index.php?view=wish_list&page=wish_data_id&userID=6&products={%22productID%22:[{%22p_id%22:%2249%22},{%22p_id%22:%2250%22}]}";
    // For Storing Values in Preferences

    public static String PREFS_NAME = "UserBasicDetails";
    public static String USER_ID = "UserId";
    public static String USER_NAME = "username";
    public static String USER_MOBILE = "usermobile";
    public static String USER_IMAGE = "userimage";
    public static String USER_EMAIl = "useremail";
    public static String USER_WISHLIST = "WishList";
    public static String USER_CITYNAME = "cityname";

    // For GCM
    public static final String SENT_TOKEN_TO_SERVER = "SENT_TOKEN_TO_SERVER";
    public static final String REG_SUCCESS = "REG_SUCCESS";
    public static final String REGISTRATION_COMPLETE = "REGISTRATION_COMPLETE";
    public static final String REG_ID = "gcmtoken";

    public static final String DROID_LOGOd =
     "M104.98945617675781,128.30074882507324 L103.48570251464844,344.8421154022217"+
     "M103.48570251464844,127.54887199401855 L213.2601318359375,36.57143211364746"+
     "M213.2601318359375,36.57143211364746 L314.7638854980469,124.54134941101074"+
     "M314.7638854980469,123.03759574890137 L311.7563781738281,340.33082389831543"+
     "M101.98194885253906,342.58646965026855 L312.5082702636719,340.33082389831543";

    public static  final String mnb="M437.9,164.7h-1.3c-16.6,0 -30.2,13.6 -30.2,30.1v131.1c0,16.6 13.6,30.1 30.2,30.1h1.3c16.6,0 30.1,-13.6 30.1,-30.1V194.8C468,178.3 454.5,164.7 437.9,164.7z";

    public static  final String circle="M 80.889194,41.80541 C 80.889194,63.45198 63.341174,81 41.694594,81 20.048024,81 2.5,63.45198 2.5,41.80541 2.5,20.158827 20.048024,2.61081 41.694594,2.61081 c 21.64658,0 39.1946,17.548017 39.1946,39.1946 z";

    public static final String DROID_LOGO = "M 149.22,22.00\n" +
            "           C 148.23,20.07 146.01,16.51 146.73,14.32\n" +
            "             148.08,10.21 152.36,14.11 153.65,16.06\n" +
            "             153.65,16.06 165.00,37.00 165.00,37.00\n" +
            "             194.29,27.24 210.71,27.24 240.00,37.00\n" +
            "             240.00,37.00 251.35,16.06 251.35,16.06\n" +
            "             252.64,14.11 256.92,10.21 258.27,14.32\n" +
            "             258.99,16.51 256.77,20.08 255.78,22.00\n" +
            "             252.53,28.28 248.44,34.36 246.00,41.00\n" +
            "             252.78,43.16 258.78,48.09 263.96,52.85\n" +
            "             281.36,68.83 289.00,86.62 289.00,110.00\n" +
            "             289.00,110.00 115.00,110.00 115.00,110.00\n" +
            "             115.00,110.00 117.66,91.00 117.66,91.00\n" +
            "             120.91,76.60 130.30,62.72 141.04,52.85\n" +
            "             146.22,48.09 152.22,43.16 159.00,41.00\n" +
            "             159.00,41.00 149.22,22.00 149.22,22.00 Z\n" +
            "           M 70.80,56.00\n" +
            "           C 70.80,56.00 97.60,100.00 97.60,100.00\n" +
            "             101.34,106.21 108.32,116.34 110.21,123.00\n" +
            "             113.76,135.52 103.90,147.92 91.00,147.92\n" +
            "             78.74,147.92 74.44,139.06 69.00,130.00\n" +
            "             69.00,130.00 39.80,82.00 39.80,82.00\n" +
            "             35.73,75.29 28.40,66.08 29.20,58.00\n" +
            "             30.26,47.20 38.61,40.47 49.00,39.72\n" +
            "             61.22,40.24 64.96,46.28 70.80,56.00 Z\n" +
            "           M 375.80,58.00\n" +
            "           C 376.60,66.08 369.27,75.29 365.20,82.00\n" +
            "             365.20,82.00 336.00,130.00 336.00,130.00\n" +
            "             330.71,138.82 326.73,147.24 315.00,147.89\n" +
            "             301.74,148.63 291.14,135.87 294.79,123.00\n" +
            "             296.68,116.34 303.66,106.21 307.40,100.00\n" +
            "             307.40,100.00 333.00,58.00 333.00,58.00\n" +
            "             339.02,47.98 342.23,40.92 355.00,39.72\n" +
            "             365.83,40.00 374.69,46.77 375.80,58.00 Z\n" +
            "           M 289.00,116.00\n" +
            "           C 289.00,116.00 289.00,239.00 289.00,239.00\n" +
            "             288.98,249.72 285.92,257.31 275.00,261.10\n" +
            "             265.22,264.50 258.37,259.56 255.02,264.43\n" +
            "             253.78,266.24 254.00,269.84 254.00,272.00\n" +
            "             254.00,272.00 254.00,298.00 254.00,298.00\n" +
            "             254.00,304.85 254.77,310.07 250.36,315.93\n" +
            "             242.35,326.68 226.84,326.49 218.80,315.93\n" +
            "             215.07,311.00 215.01,306.83 215.00,301.00\n" +
            "             215.00,301.00 215.00,262.00 215.00,262.00\n" +
            "             215.00,262.00 190.00,262.00 190.00,262.00\n" +
            "             190.00,262.00 190.00,301.00 190.00,301.00\n" +
            "             189.99,306.83 189.93,311.00 186.20,315.93\n" +
            "             178.16,326.49 162.65,326.68 154.64,315.93\n" +
            "             151.09,311.22 151.01,307.61 151.00,302.00\n" +
            "             151.00,302.00 151.00,272.00 151.00,272.00\n" +
            "             151.00,269.84 151.22,266.24 149.98,264.43\n" +
            "             146.53,259.42 138.97,264.76 129.00,260.86\n" +
            "             118.39,256.72 116.02,248.29 116.00,238.00\n" +
            "             116.00,238.00 116.00,116.00 116.00,116.00\n" +
            "             116.00,116.00 289.00,116.00 289.00,116.00 Z";
}
