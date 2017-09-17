package com.adlabs.apps.ohok.remote;

/**
 * Created by Anup Dey on 16-Sep-17.
 */

public class ApiUtils {

    //"https://api.stackexchange.com/2.2/"
    public static final String BASE_URL = "http://www.adlabsbd.com/userup/";

    public static SOService getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(SOService.class);
    }


}
