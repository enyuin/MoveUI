package com.example.moveui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.moveui.data.model.AccessToken;
import com.example.moveui.data.model.AcknowledgeData;
import com.example.moveui.data.model.AmrStatus;
import com.example.moveui.data.model.EmptyRequest;
import com.example.moveui.data.model.GetTaskCtrl;
import com.example.moveui.data.model.GoalCtrl;
import com.example.moveui.data.model.RecoveryPoint;
import com.example.moveui.data.model.StationReleaseCtrl;
import com.example.moveui.data.model.TaskCtrl;
import com.example.moveui.data.model.UserCtrl;
import com.example.moveui.data.remote.ApiService;
import com.example.moveui.data.remote.ApiUtils;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    List<Integer> goalList = new ArrayList<Integer>();
    private AppBarConfiguration appBarConfiguration;
    private static final String TAG = "MainActivity";
    private static int st1_id = 2;
    private static int st2_id = 3;
    private static int st3_id = 7;
    private static int st4_id = 9;
    private ApiService mAPIService;
    String access_token = null;
    Button buttonSt1, buttonSt2, buttonSt3, buttonSt4, buttonAck, buttonRecover;
    com.google.android.material.floatingactionbutton.FloatingActionButton floatingActionButton;
    TextView tw;
    Date current_date, previous_date;
    Toast toastmsg;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        current_date = new Date();
        previous_date = new Date();

        buttonSt1 = findViewById(R.id.button_st1);
        buttonSt2 = findViewById(R.id.button_st2);
        buttonSt3 = findViewById(R.id.button_st3);
        buttonSt4 = findViewById(R.id.button_st4);

        buttonAck = findViewById(R.id.button_acknowledge);
        buttonRecover = findViewById(R.id.button_recover);
        floatingActionButton = findViewById(R.id.fab);
        tw = findViewById(R.id.textview_first);

        buttonAck.setVisibility(View.INVISIBLE);
        mAPIService = ApiUtils.getAPIService();
        showRecoveryPage();
        showFab();


        buttonSt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (goalList.contains(st1_id)) {
                    goalList.remove(goalList.indexOf(st1_id));
                    buttonSt1.setSelected(false);
                }
                else {
                    goalList.add(st1_id);
                    buttonSt1.setSelected(true);
                }
                showFab();
            }
        });

        buttonSt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (goalList.contains(st2_id)) {
                    goalList.remove(goalList.indexOf(st2_id));
                    buttonSt2.setSelected(false);
                }
                else {
                    goalList.add(st2_id);
                    buttonSt2.setSelected(true);
                }
                showFab();
            }
        });

        buttonSt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (goalList.contains(st3_id)) {
                    goalList.remove(goalList.indexOf(st3_id));
                    buttonSt3.setSelected(false);
                }
                else {
                    goalList.add(st3_id);
                    buttonSt3.setSelected(true);
                }
                showFab();
            }
        });

        buttonSt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (goalList.contains(st4_id)) {
                    goalList.remove(goalList.indexOf(st4_id));
                    buttonSt4.setSelected(false);
                }
                else {
                    goalList.add(st4_id);
                    buttonSt4.setSelected(true);
                }
                showFab();
            }
        });

        buttonAck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                current_date = new Date();
                long difference = current_date.getTime() - previous_date.getTime();
                Log.i(TAG, "Time difference " + difference);
                if (difference > 5000){
                    getStationRelease();
                }
                else {
                    if (toastmsg!= null) {
                        toastmsg.cancel();
                    }
                    toastmsg = Toast.makeText(getApplicationContext(), "Do not spam the acknowledge button", Toast.LENGTH_SHORT);
                    toastmsg.show();
                }
            }
        });

        buttonRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (access_token == null)
                {
                    Log.i(TAG, "reobtain access token");
                    AuthUser(2);
                }
                else {
                    sendRecovery();
                }
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(goalList);
                previous_date = new Date();
                if (access_token == null)
                {
                    Log.i(TAG, "reobtain access token");
                    AuthUser(1);
                }
                else {
                    getAvailableTask();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void AuthUser(int mode){
        UserCtrl user = new UserCtrl("sesto", "100%sesto");
        mAPIService.getAuthUser(user).enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "why success");
                    access_token = response.body().toString();
                    if (mode == 0){
                        getAmrStatus();
                    }
                    if (mode == 1){
                        getAvailableTask();
                    }
                    if (mode == 2){
                        sendRecovery();
                    }
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to authenticate the user " + t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createGoalTask(TaskCtrl selected){
        Log.i(TAG, "generated2 " + selected);
        Log.i(TAG, access_token);
        mAPIService.createTaskRequest(selected, access_token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i(TAG, "hehe2 " + response);
                if (response.isSuccessful()) {
                    Log.i(TAG, "hehe");
                    switchPage();
                    Toast.makeText(getApplicationContext(), "Received " + response.body(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "what happen " + t);
                Toast.makeText(getApplicationContext(), "Unable to submit post to create goal API " + t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void acknowledgeTask(){
        mAPIService.acknowledgeRequest(EmptyRequest.INSTANCE, access_token).enqueue(new Callback<AcknowledgeData>() {
            @Override
            public void onResponse(Call<AcknowledgeData> call, Response<AcknowledgeData> response) {
                if (response.isSuccessful()) {
                    goalList.remove(0);
                    previous_date = new Date();
                    if (goalList.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Acknowledged! Task finished!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Acknowledged! Moving to next station.", Toast.LENGTH_SHORT).show();
                    }
                    switchPage();
                    Log.i(TAG, "Acknowledged request has submitted" + response.body());
                }
            }

            @Override
            public void onFailure(Call<AcknowledgeData> call, Throwable t) {
                Log.e(TAG, "Unable to submit acknowledge request due to " + t);
            }
        });
    }

    private void getStationRelease(){
        mAPIService.getStationRelease(access_token).enqueue(new Callback<List<StationReleaseCtrl>>() {
            @Override
            public void onResponse(Call<List<StationReleaseCtrl>> call, Response<List<StationReleaseCtrl>> response) {
                if (response.isSuccessful()) {
                    List<StationReleaseCtrl> release = response.body();
                    Log.i(TAG, "hehe3 " + release.get(0).getStatus());
                    String operation = "operational";
                    if (Objects.equals(release.get(0).getStatus(), operation)){
                        acknowledgeTask();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "AMR have not reached the goal yet, so ack is not allowed.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<StationReleaseCtrl>> call, Throwable t) {
                Log.e(TAG, "getStation Release Error " + t);
                Toast.makeText(getApplicationContext(), "Unable to submit getStationRelease API " + t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getAvailableTask(){
        mAPIService.getTask(access_token).enqueue(new Callback<List<GetTaskCtrl>>() {
            @Override
            public void onResponse(Call<List<GetTaskCtrl>> call, Response<List<GetTaskCtrl>> response) {
                if (response.isSuccessful()) {
                    List<GetTaskCtrl> task_saved = response.body();
                    if (task_saved != null && !task_saved.isEmpty()){
                        Log.i(TAG, "hehe4 " + task_saved.get(0).getId());
                        cancelTask(task_saved.get(0).getId());
                    }
                    else {
                        Log.i(TAG, "hehe5");
//                                            After getting the access token, start to create task
                        List<GoalCtrl> goalGenerateList = new ArrayList<>();
                        for (int i = 0; i < goalList.size(); i++){
                            GoalCtrl goal = new GoalCtrl(i, goalList.get(i), 7);
                            goalGenerateList.add(goal);
                        }
                        TaskCtrl task = new TaskCtrl(goalList.size(), goalGenerateList, 100);
                        Log.i(TAG, "generated " + goalList.size());
                        createGoalTask(task);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GetTaskCtrl>> call, Throwable t) {
                Log.e(TAG, "what happen " + t);
                Toast.makeText(getApplicationContext(), "Unable to submit getTask API " + t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cancelTask(int task_id){
        mAPIService.cancelTaskRequest(task_id, access_token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "hehe3 " + response.body());
                    Toast.makeText(getApplicationContext(), response.body(), Toast.LENGTH_LONG).show();
                    List<GoalCtrl> goalGenerateList = new ArrayList<>();
                    for (int i = 0; i < goalList.size(); i++){
                        GoalCtrl goal = new GoalCtrl(i, goalList.get(i), 7);
                        goalGenerateList.add(goal);
                    }
                    TaskCtrl task = new TaskCtrl(goalList.size(), goalGenerateList, 100);
                    Log.i(TAG, "generated " + goalList.size());
                    createGoalTask(task);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Cancel Task Error " + t);
                Toast.makeText(getApplicationContext(), "Unable to submit cancelTask API " + t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getAmrStatus(){
        mAPIService.getAMRStatus(access_token).enqueue(new Callback<List<AmrStatus>>() {
            @Override
            public void onResponse(Call<List<AmrStatus>> call, Response<List<AmrStatus>> response) {
                if (response.isSuccessful()) {
                    List<AmrStatus> amr_status = response.body();
                    Log.i(TAG, "hehe3 " + amr_status.get(0).getNavigation_state());
                    String recovery = "recovery";
                    String offline = "offline";
                    String welcome_kit = "welcome_kit";
                    String initialisation = "initialisation";
                    if (Objects.equals(amr_status.get(0).getNavigation_state(), recovery)){
                        Log.i(TAG, "waiting for recovery");
                    }
                    else if (Objects.equals(amr_status.get(0).getNavigation_state(), offline)){
                        Log.i(TAG, "waiting for recovery");
                    }
                    else if (Objects.equals(amr_status.get(0).getNavigation_state(), welcome_kit)){
                        Log.i(TAG, "waiting for recovery");
                    }
                    else if (Objects.equals(amr_status.get(0).getNavigation_state(), initialisation)){
                        Log.i(TAG, "waiting for recovery");
                    }
                    else {
                        switchPage();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<AmrStatus>> call, Throwable t) {
                Log.e(TAG, "Get AMR Status Error " + t);
                Toast.makeText(getApplicationContext(), "Unable to submit getAMRStatus API " + t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendRecovery(){
        RecoveryPoint rp = new RecoveryPoint(1, 1);
        mAPIService.postRecoverRequest(rp, access_token).enqueue(new Callback<EmptyRequest>() {
            @Override
            public void onResponse(Call<EmptyRequest> call, Response<EmptyRequest> response) {
                if (response.isSuccessful()) {
//                    Log.i(TAG, response.body());
//                    Toast.makeText(getApplicationContext(), response.body(), Toast.LENGTH_LONG).show();
                    switchPage();
                }
            }

            @Override
            public void onFailure(Call<EmptyRequest> call, Throwable t) {
                Log.e(TAG, "Post Recovery Error " + t);
                Toast.makeText(getApplicationContext(), "Unable to submit post recovery API " + t, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showFab(){
        if (goalList.isEmpty()){
            floatingActionButton.setVisibility(View.INVISIBLE);
        }
        else{
            floatingActionButton.setVisibility(View.VISIBLE);
        }
    }

    public void showRecoveryPage(){
        buttonRecover.setVisibility(View.VISIBLE);
        buttonAck.setVisibility(View.INVISIBLE);
        buttonSt1.setVisibility(View.INVISIBLE);
        buttonSt2.setVisibility(View.INVISIBLE);
        buttonSt3.setVisibility(View.INVISIBLE);
        buttonSt4.setVisibility(View.INVISIBLE);
        buttonSt1.setSelected(false);
        buttonSt2.setSelected(false);
        buttonSt3.setSelected(false);
        buttonSt4.setSelected(false);
        floatingActionButton.setVisibility(View.INVISIBLE);
        tw.setText("Press the recover button to start!");
        goalList.clear();
        AuthUser(0);
    }

    public void switchPage(){
        if (goalList.isEmpty()){
            buttonRecover.setVisibility(View.INVISIBLE);
            buttonAck.setVisibility(View.INVISIBLE);
            buttonSt1.setVisibility(View.VISIBLE);
            buttonSt2.setVisibility(View.VISIBLE);
            buttonSt3.setVisibility(View.VISIBLE);
            buttonSt4.setVisibility(View.VISIBLE);
            buttonSt1.setSelected(false);
            buttonSt2.setSelected(false);
            buttonSt3.setSelected(false);
            buttonSt4.setSelected(false);
            tw.setText("Please select which stations AMR is going to");
        }
        else{
            buttonRecover.setVisibility(View.INVISIBLE);
            buttonAck.setVisibility(View.VISIBLE);
            buttonSt1.setVisibility(View.INVISIBLE);
            buttonSt2.setVisibility(View.INVISIBLE);
            buttonSt3.setVisibility(View.INVISIBLE);
            buttonSt4.setVisibility(View.INVISIBLE);
            floatingActionButton.setVisibility(View.INVISIBLE);
            if (goalList.get(0) == st1_id){
                tw.setText("Moving to ST 1 ...");
            }
            if (goalList.get(0) == st2_id){
                tw.setText("Moving to ST 2 ...");
            }
            if (goalList.get(0) == st3_id){
                tw.setText("Moving to ST 3 ...");
            }
            if (goalList.get(0) == st4_id){
                tw.setText("Moving to ST 4 ...");
            }

        }
    }
}