package com.hackerkernel.storemanager.extras;

/**
 * a interface to store API Keys
 */
public interface Keys {
    //common Keys
    String KEY_COM_RETURN = "return";
    String KEY_COM_MESSAGE = "message";
    String KEY_COM_USERID = "userId";
    String KEY_COM_DATA = "data";
    String KEY_COM_PRODUCTID = "productId";
    String KEY_DEFAULT = "";


    //Login Keys params
    String PRAM_LOGIN_EMAIL = "email";
    String PRAM_LOGIN_PASSWORD = "password";

    //Signup keys params
    String PRAM_SIGNUP_FN = "fullname";
    String PRAM_SIGNUP_STORENAME = "storename";
    String PRAM_SIGNUP_EMAIL = "email";
    String PRAM_SIGNUP_PHONE = "phone";
    String PRAM_SIGNUP_PASS = "password";

    //add category params
    String PRAM_AC_CATEGORY_NAME = "categoryName";

    //add salesman params
    String PRAM_AS_SALESMAN = "salesman";

    //login response
    String KEY_L_USER = "user",
            KEY_L_ID = "id",
            KEY_L_NAME = "name",
            KEY_L_STORENAME = "storename",
            KEY_L_EMAIL = "email",
            KEY_L_PHONE = "phone",
            KEY_L_PASSWORD = "password",
            KEY_L_REGISTER_AT = "register_at";

    //SimpleList response (category & salesman list)
    String KEY_SL_ID = "id",
            KEY_SL_NAME = "name",
            KEY_SL_USER_ID = "user_id",
            KEY_SL_TIME = "time",
            KEY_SL_COUNT = "count";

    //Add products
    String PRAM_AP_CATEGORYNAME = "categoryName",
            PRAM_AP_CATEGORYID = "categoryId",
            PRAM_AP_USERID = "userId",
            PRAM_AP_IMAGE = "pImage",
            PRAM_AP_NAME = "pName",
            PRAM_AP_CODE = "pCode",
            PRAM_AP_CP = "pCP",
            PRAM_AP_SP = "pSP",
            PRAM_AP_SIZE = "pSize",
            PRAM_AP_QUANTITY = "pQuantity";

    //product list
    String KEY_PL_ID = "productId",
            KEY_PL_CATEGORY_ID = "categoryId",
            KEY_PL_NAME = "name",
            KEY_PL_IMAGE = "image",
            KEY_PL_CODE = "code",
            KEY_PL_TIME = "time";

    //PRAM PRODUCT LIST
    String PRAM_PL_CATEGORYID = "categoryId",
            PRAM_PL_CATEGORYNAME = "categoryName";

    //Keys Product
    String KEY_P_ID = "id",
            KEY_P_CATEGORYID = "categoryId",
            KEY_P_NAME = "name",
            KEY_P_IMAGE = "image",
            KEY_P_CODE = "code",
            KEY_P_SIZE = "size",
            KEY_P_QUANTITY = "quantity",
            KEY_P_TIME = "time",
            KEY_P_CP = "cp",
            KEY_P_SP = "sp";

    //Add sales (Non listed)
    String PRAM_NON_LISTED_CUSTOMER_NAME = "customerName",
            PRAM_NON_LISTED_NAME = "name",
            PRAM_NON_LISTED_SIZE = "size",
            PRAM_NON_LISTED_QUANTITY = "quantity",
            PRAM_NON_LISTED_COSTPRICE = "costprice",
            PRAM_NON_LISTED_SELLINGPRICE = "sellingprice",
            PRAM_NON_LISTED_SALESMAN_ID = "salesmanId",
            PRAM_NON_LISTED_SALESMAN_NAME = "salesmanName";

    //Add sales (Listed)
    String PRAM_LISTED_SALES_TYPE = "salesType";

    //Auto Complete Product search PRAM
    String PRAM_AC_PRODUCTNAME = "productName";

    //Auto Complete Product search KEYS
    String KEY_AC_COUNT = "count",
            KEY_AC_ID = "id",
            KEY_AC_NAME = "name",
            KEY_AC_CODE = "code",
            KEY_AC_CP = "CP",
            KEY_AC_SIZE = "size";
}
