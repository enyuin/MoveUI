package com.example.moveui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.moveui.data.model.AccessToken;
import com.example.moveui.data.model.AcknowledgeData;
import com.example.moveui.data.model.AmrStatus;
import com.example.moveui.data.model.EmptyRequest;
import com.example.moveui.data.model.GetTaskCtrl;
import com.example.moveui.data.model.GoToCtrl;
import com.example.moveui.data.model.GoalCtrl;
import com.example.moveui.data.model.ObstacleStatus;
import com.example.moveui.data.model.RecoveryPoint;
import com.example.moveui.data.model.StationReleaseCtrl;
import com.example.moveui.data.model.TaskCtrl;
import com.example.moveui.data.model.UserCtrl;
import com.example.moveui.data.remote.ApiService;
import com.example.moveui.data.remote.ApiUtils;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    private static int st1_id = 14;
    private static int st2_id = 24;
    private static int st3_id = 32;
    private static int st4_id = 37;
    private ApiService mAPIService;
    String access_token = null;
    String navigation_state = null;
    Button buttonSt1, buttonSt2, buttonSt3, buttonSt4, buttonAck, buttonRecover, buttonBack, buttonReset;
    ImageView batteryImage;
    com.google.android.material.floatingactionbutton.FloatingActionButton floatingActionButton;
    TextView tw, tw_status;
    Date current_date, previous_date, previous_status_request_time;
    Toast toastmsg;
    int status_request_count = 0;
    int battery_level = 100;
    int previous_page = 0; //0 stands for recovery, 1 stands for task page
    int curr_waypoint_id = -1;
    boolean reset_pressed = false;
    boolean is_estopped_ok = true;
    boolean acknowledge_handler_triggered = false;

    Handler status_handler, recovery_handler, obstacle_handler, acknowledge_handler;
    Runnable status_runnable, recovery_runnable, obstacle_runnable, acknowledge_runnable;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        current_date = new Date();
        previous_date = new Date();
        previous_status_request_time = new Date();

        buttonSt1 = findViewById(R.id.button_st1);
        buttonSt2 = findViewById(R.id.button_st2);
        buttonSt3 = findViewById(R.id.button_st3);
        buttonSt4 = findViewById(R.id.button_st4);

        buttonAck = findViewById(R.id.button_acknowledge);
        buttonRecover = findViewById(R.id.button_recover);
        buttonReset = findViewById(R.id.button_reset);
        buttonBack = findViewById(R.id.button_back);

        floatingActionButton = findViewById(R.id.fab);
        tw = findViewById(R.id.textview_first);
        tw_status = findViewById(R.id.textview_state);

        batteryImage = findViewById(R.id.image_battery);

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
                buttonRecover.setEnabled(false);
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonReset.setEnabled(false);
                buttonBack.setEnabled(false);
                Log.i(TAG, "Resetting");
                if (access_token == null)
                {
                    Log.i(TAG, "reobtain access token for reset");
                    AuthUser(3);
                }
                else {
                    reset();
                }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Back to task page");
                status_handler.removeCallbacks(status_runnable);
                if (previous_page == 0){
                    showRecoveryPage();
                }
                if (previous_page == 1){
                    switchPage();
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

        tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Date current_request_time = new Date();
                long difference = current_request_time.getTime() - previous_status_request_time.getTime();
                Log.i(TAG, "Request time difference " + difference);
                if (difference > 10000){
                    status_request_count = 0;
                    previous_status_request_time = new Date();
                }
                else {
                    status_request_count += 1;
                    if (status_request_count == 7){
                        if (previous_page == 0)
                        {
                            recovery_handler.removeCallbacks(recovery_runnable);
                        }
                        if (acknowledge_handler_triggered){
                            acknowledge_handler.removeCallbacks(acknowledge_runnable);
                        }
                        showStatusPage();
                        status_request_count = 0;
                        previous_status_request_time = new Date();
                    }
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
                    if (mode == 3){
                        reset();
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
        mAPIService.acknowledgeRequest(EmptyRequest.INSTANCE, access_token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    goalList.remove(0);
                    previous_date = new Date();
                    if (goalList.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Acknowledged! Task finished!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Acknowledged! Moving to next station.", Toast.LENGTH_SHORT).show();
                    }
                    Log.i(TAG,"SUCCESS4");
                    acknowledge_handler.removeCallbacks(acknowledge_runnable);
                    switchPage();
                    Log.i(TAG, "Acknowledged request has submitted" + response.body());
                }
                Log.i(TAG,"SUCCESS WHY");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
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
                    Log.i(TAG, "retrieve task as " + task_saved);
                    String cancelled = "cancelled";
                    String completed = "completed";
                    if (task_saved != null && !task_saved.isEmpty() && !Objects.equals(task_saved.get(0).getState(), cancelled) && !Objects.equals(task_saved.get(0).getState(), completed)){
                        Log.i(TAG, "hehe4 " + task_saved.get(0).getId());
                        Log.i(TAG, "task state " + task_saved.get(0).getState());
                        cancelTask(task_saved.get(0).getId());
                    }
                    else {
                        if (previous_page < 2 && !reset_pressed)
                        {
                            Log.i(TAG, "hehe5");
                            List<GoalCtrl> goalGenerateList = new ArrayList<>();
                            for (int i = 0; i < goalList.size(); i++){
                                int id_count = 1 + 2 * i;
                                GoToCtrl goto_tree = new GoToCtrl("2."+ id_count, "ST" + goalList.get(i).toString());
                                String goal_id = "";
                                if (i == 0){
                                    goal_id = "6b5856d4-660e-4d0d-b8c9-996cbc00e846";
                                }
                                if (i == 1){
                                    goal_id = "b4e4803e-b9b3-4d3b-a009-5763534930e0";
                                }
                                if (i == 2){
                                    goal_id = "79c8c96e-296d-4d0c-b43f-6670a88b3edd";
                                }
                                if (i == 3){
                                    goal_id = "736e21ee-d5a9-4350-a3d4-0ff56f78fa31";
                                }
                                GoalCtrl goal = new GoalCtrl(goal_id, goto_tree);
                                goalGenerateList.add(goal);
                            }
                            TaskCtrl task = new TaskCtrl(goalList.size(), goalGenerateList, 100);
                            Log.i(TAG, "generated " + goalList.size());
                            createGoalTask(task);
                        }
                        else {
                            reset_pressed = false;
                            buttonReset.setEnabled(true);
                            buttonBack.setEnabled(true);
                            goalList.clear();
                            Toast.makeText(getApplicationContext(), "Reset successfully", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else {
                    if (!(previous_page < 2)){
                        reset_pressed = false;
                        buttonReset.setEnabled(true);
                        buttonBack.setEnabled(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GetTaskCtrl>> call, Throwable t) {
                Log.e(TAG, "what happen " + t);
                reset_pressed = false;
                buttonReset.setEnabled(true);
                buttonBack.setEnabled(true);
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
                    if (previous_page < 2 && !reset_pressed)
                    {
                        List<GoalCtrl> goalGenerateList = new ArrayList<>();
                        for (int i = 0; i < goalList.size(); i++){
                            int id_count = 1 + 2 * i;
                            GoToCtrl goto_tree = new GoToCtrl("2."+ id_count, "ST" + goalList.get(i).toString());
                            String goal_id = "";
                            if (i == 0){
                                goal_id = "6b5856d4-660e-4d0d-b8c9-996cbc00e846";
                            }
                            if (i == 1){
                                goal_id = "b4e4803e-b9b3-4d3b-a009-5763534930e0";
                            }
                            if (i == 2){
                                goal_id = "79c8c96e-296d-4d0c-b43f-6670a88b3edd";
                            }
                            if (i == 3){
                                goal_id = "736e21ee-d5a9-4350-a3d4-0ff56f78fa31";
                            }
                            GoalCtrl goal = new GoalCtrl(goal_id, goto_tree);
                            goalGenerateList.add(goal);
                        }
                        TaskCtrl task = new TaskCtrl(goalList.size(), goalGenerateList, 100);
                        Log.i(TAG, "generated " + goalList.size());
                        createGoalTask(task);
                    }
                    else {
                        reset_pressed = false;
                        buttonReset.setEnabled(true);
                        buttonBack.setEnabled(true);
                        goalList.clear();
                        Toast.makeText(getApplicationContext(), "Reset successfully", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    reset_pressed = false;
                    buttonReset.setEnabled(true);
                    buttonBack.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Cancel Task Error " + t);
                reset_pressed = false;
                buttonReset.setEnabled(true);
                buttonBack.setEnabled(true);
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
                    navigation_state = amr_status.get(0).getNavigation_state();
                    battery_level = amr_status.get(0).getBattery_level();
                    is_estopped_ok = amr_status.get(0).getSystem_status().isIs_estop_ok();
                    curr_waypoint_id = amr_status.get(0).getCurr_waypoint_id();
                    Log.i(TAG, "Received "+ is_estopped_ok);
                    if (previous_page == 0)
                    {
                        String recovery = "recovery";
                        String offline = "offline";
                        String welcome_kit = "welcome_kit";
                        String welcome_kit_error = "wk_error";
                        String initialisation = "initialisation";
                        String estopped = "estopped";
                        if (Objects.equals(amr_status.get(0).getNavigation_state(), recovery)){
                            Log.i(TAG, "waiting for recovery");
                        }
                        else if (Objects.equals(amr_status.get(0).getNavigation_state(), estopped)){
                            Log.i(TAG, "waiting for estopped");
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
                        else if (Objects.equals(amr_status.get(0).getNavigation_state(), welcome_kit_error)){
                            Log.i(TAG, "Error, need reboot");
                        }
                        else {
                            switchPage();
                        }
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
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // This method will be executed once the timer is over
                            buttonRecover.setEnabled(true);
                            getAmrStatus();
                            Log.d(TAG,"resend1");
                            //                    switchPage();

                        }
                    },10000);// set time as per your requirement
                }
                else {
                    buttonRecover.setEnabled(true);
                    Log.e(TAG, "Fail to recover");
                }
            }

            @Override
            public void onFailure(Call<EmptyRequest> call, Throwable t) {
                buttonRecover.setEnabled(true);
                Log.e(TAG, "Post Recovery Error " + t);
                Toast.makeText(getApplicationContext(), "Unable to submit post recovery API " + t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void reset(){
        //clear obstacle first
        if (reset_pressed == true){
            Log.i(TAG, "reset has been pressed");
            return;
        }
        reset_pressed = true;
        mAPIService.getObstacles(access_token).enqueue(new Callback<ObstacleStatus>() {
            @Override
            public void onResponse(Call<ObstacleStatus> call, Response<ObstacleStatus> response) {
                if (response.isSuccessful()) {
                    ObstacleStatus obstacle_retrieved = response.body();
                    List<Integer> obstacle_environment = obstacle_retrieved.getEnvironment();
                    Log.i(TAG, "value" + obstacle_environment);

                    if (obstacle_environment != null && !obstacle_environment.isEmpty()){
                        Log.i(TAG, "size " + obstacle_environment.get(0));
                        for (int i=0; i<obstacle_environment.size(); i ++)
                        {
                            clearObstacle(obstacle_environment.get(i));
                        }
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                getAvailableTask();
                                Log.i(TAG,"Waited for clearing to happen with size of " + obstacle_environment.size());
                            }
                        }, 1000L * obstacle_environment.size());// set time as per your requirement
                    }
                    else {
                        getAvailableTask();
                    }
                }
                else {
                    reset_pressed = false;
                    buttonReset.setEnabled(true);
                    buttonBack.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<ObstacleStatus> call, Throwable t) {
                Log.e(TAG, "what happen " + t);
                reset_pressed = false;
                buttonReset.setEnabled(true);
                buttonBack.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Unable to submit getObstacle API " + t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void clearObstacle(int id){
        //clear obstacle first
        mAPIService.deleteObstacle(id, access_token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.body(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "what happen " + t);
                Toast.makeText(getApplicationContext(), "Unable to submit getObstacle API " + t, Toast.LENGTH_LONG).show();
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
        previous_page = 0;
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
        tw.setVisibility(View.VISIBLE);
        tw_status.setVisibility(View.INVISIBLE);
        batteryImage.setVisibility(View.INVISIBLE);
        buttonBack.setVisibility(View.INVISIBLE);
        buttonReset.setVisibility(View.INVISIBLE);
        goalList.clear();

        recovery_runnable = new Runnable() {
            @Override
            public void run() {
                if (access_token == null)
                {
                    AuthUser(4);
                }
                else {
                    getAmrStatus();
                    if (navigation_state == "estopped" || !is_estopped_ok) {
                        tw.setText("E-STOP!!! Reset E-STOP to cont!");
                        tw.setTextColor(Color.RED);
                        buttonRecover.setEnabled(false);
                    }
                    else {
                        tw.setTextColor(Color.BLACK);
                        tw.setText("Press the recover button to start!");
                        buttonRecover.setEnabled(true);
                    }
                }
                recovery_handler.postDelayed(recovery_runnable, 1000);
            }
        };

        recovery_handler = new Handler();
        recovery_handler.postDelayed(recovery_runnable, 0);
    }

    public void showStatusPage(){
        buttonRecover.setVisibility(View.INVISIBLE);
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
        tw.setVisibility(View.INVISIBLE);
        tw_status.setVisibility(View.VISIBLE);
        batteryImage.setVisibility(View.VISIBLE);
        buttonBack.setVisibility(View.VISIBLE);
        buttonReset.setVisibility(View.VISIBLE);

        status_runnable = new Runnable() {
            @Override
            public void run() {
                getAmrStatus();
                Log.i(TAG, "battery level " + battery_level);
                if (battery_level > 75)
                {
                    batteryImage.setImageResource(R.drawable.full_battery);
                }
                if (battery_level > 50 && battery_level <= 75)
                {
                    batteryImage.setImageResource(R.drawable.battery);
                }
                if (battery_level > 25 && battery_level <= 50)
                {
                    batteryImage.setImageResource(R.drawable.half_battery);
                }
                if (battery_level > 0 && battery_level <= 25)
                {
                    batteryImage.setImageResource(R.drawable.low_battery);
                }
                tw_status.setText("Battery Level: " +battery_level +"%\nState:" + navigation_state +"\nGoal:" + goalList);
                status_handler.postDelayed(status_runnable, 1000);
            }
        };

        status_handler = new Handler();
        status_handler.postDelayed(status_runnable, 0);

    }

    public void showAcknowledgePage(){
        buttonRecover.setVisibility(View.INVISIBLE);
        buttonAck.setVisibility(View.VISIBLE);
        buttonAck.setEnabled(false);
        buttonSt1.setVisibility(View.INVISIBLE);
        buttonSt2.setVisibility(View.INVISIBLE);
        buttonSt3.setVisibility(View.INVISIBLE);
        buttonSt4.setVisibility(View.INVISIBLE);
        floatingActionButton.setVisibility(View.INVISIBLE);
        tw.setVisibility(View.VISIBLE);
        tw_status.setVisibility(View.INVISIBLE);
        batteryImage.setVisibility(View.INVISIBLE);
        buttonBack.setVisibility(View.INVISIBLE);
        buttonReset.setVisibility(View.INVISIBLE);
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

        acknowledge_runnable = new Runnable() {
            @Override
            public void run() {
                getAmrStatus();
                String station = "station";
                Log.i(TAG, "acknowledge runnable is running");
                if (Objects.equals(navigation_state, station)){
                    if (curr_waypoint_id == goalList.get(0)){
                        Log.i(TAG, "AMR reached station");
                        Runnable station_runnable = new Runnable() {
                            @Override
                            public void run() {
                                buttonAck.setEnabled(true);
                            }
                        };

                        Handler station_handler = new Handler();
                        station_handler.postDelayed(station_runnable, 5000);
                    }
                }
                else {
                    buttonAck.setEnabled(false);
                }
                acknowledge_handler.postDelayed(acknowledge_runnable, 1000);
            }
        };

        acknowledge_handler = new Handler();
        acknowledge_handler.postDelayed(acknowledge_runnable, 0);
        acknowledge_handler_triggered = true;

    }

    public void switchPage(){
        recovery_handler.removeCallbacks(recovery_runnable);
        previous_page = 1;
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
            tw.setVisibility(View.VISIBLE);
            tw_status.setVisibility(View.INVISIBLE);
            batteryImage.setVisibility(View.INVISIBLE);
            buttonBack.setVisibility(View.INVISIBLE);
            buttonReset.setVisibility(View.INVISIBLE);
        }
        else{
            showAcknowledgePage();
        }
    }
}