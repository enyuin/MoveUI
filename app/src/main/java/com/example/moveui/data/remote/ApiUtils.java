package com.example.moveui.data.remote;

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "http://192.60.86.14/";

    public static ApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }
}
