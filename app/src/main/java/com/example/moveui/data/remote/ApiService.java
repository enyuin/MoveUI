package com.example.moveui.data.remote;
import com.example.moveui.data.model.AccessToken;
import com.example.moveui.data.model.AcknowledgeData;
import com.example.moveui.data.model.AmrStatus;
import com.example.moveui.data.model.EmptyRequest;
import com.example.moveui.data.model.GetTaskCtrl;
import com.example.moveui.data.model.RecoveryPoint;
import com.example.moveui.data.model.StationReleaseCtrl;
import com.example.moveui.data.model.TaskCtrl;
import com.example.moveui.data.model.UserCtrl;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("public/auth")
        // @FormUr1Encoded
    Call<AccessToken> getAuthUser(@Body UserCtrl body);

    @POST("public/ensemble/task")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    Call<String> createTaskRequest(@Body TaskCtrl body, @Header("Authorization") String access_token);

    @GET("public/ensemble/task")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    Call<List<GetTaskCtrl>> getTask(@Header("Authorization") String access_token);

    @POST("public/ensemble/task/cancel")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    Call<String> cancelTaskRequest(@Query("id") int id, @Header("Authorization") String access_token);

    @POST("public/amrs/recovery")
    @Headers("Content-Type: application/json")
    Call<EmptyRequest> postRecoverRequest(@Body RecoveryPoint body, @Header("Authorization") String access_token);

    @POST("payload/user_input")
    @Headers("Content-Type: application/json")
    Call<AcknowledgeData> acknowledgeRequest(@Body EmptyRequest body, @Header("Authorization") String access_token);

    @GET("public/amrs/release/station")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    Call<List<StationReleaseCtrl>> getStationRelease(@Header("Authorization") String access_token);

    @GET("public/amrs/statuses")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    Call<List<AmrStatus>> getAMRStatus(@Header("Authorization") String access_token);

}
