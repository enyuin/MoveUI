package com.example.moveui.data.remote;
import com.example.moveui.data.model.AccessToken;
import com.example.moveui.data.model.AcknowledgeData;
import com.example.moveui.data.model.AmrStatus;
import com.example.moveui.data.model.EmptyRequest;
import com.example.moveui.data.model.GetTaskCtrl;
import com.example.moveui.data.model.ObstacleStatus;
import com.example.moveui.data.model.RecoveryPoint;
import com.example.moveui.data.model.StationReleaseCtrl;
import com.example.moveui.data.model.TaskCtrl;
import com.example.moveui.data.model.UserCtrl;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("public/auth")
        // @FormUr1Encoded
    Call<AccessToken> getAuthUser(@Body UserCtrl body);

    @POST("public/tasks")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    Call<String> createTaskRequest(@Body TaskCtrl body, @Header("Authorization") String access_token);

    @GET("public/tasks")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    Call<List<GetTaskCtrl>> getTask(@Header("Authorization") String access_token);

    @POST("public/tasks/cancel")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    Call<String> cancelTaskRequest(@Query("id") int id, @Header("Authorization") String access_token);

    @POST("public/amrs/recovery")
    @Headers("Content-Type: application/json")
    Call<EmptyRequest> postRecoverRequest(@Body RecoveryPoint body, @Header("Authorization") String access_token);

    @POST("payload/user_input/acknowledgement")
    @Headers("Content-Type: application/json")
    Call<String> acknowledgeRequest(@Body EmptyRequest body, @Header("Authorization") String access_token);

    @GET("public/amrs/release/station")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    Call<List<StationReleaseCtrl>> getStationRelease(@Header("Authorization") String access_token);

    @GET("public/amrs/statuses")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    Call<List<AmrStatus>> getAMRStatus(@Header("Authorization") String access_token);

    @GET("public/graphs/paths/obstacles")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    Call<ObstacleStatus> getObstacles(@Header("Authorization") String access_token);

    @DELETE("public/graphs/paths/obstacles")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    Call<String> deleteObstacle(@Query("waypoint_id") int id, @Header("Authorization") String access_token);

}
