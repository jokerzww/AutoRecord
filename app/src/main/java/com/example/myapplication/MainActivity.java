package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myapplication.AutoRecord.AutoRecord;
import com.example.myapplication.AutoRecord.HttpUtil;
import com.example.myapplication.AutoRecord.TimeUtil;
import com.example.myapplication.JobService.MyJobService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAIN_ACTIVITY";
    private static final String CHANNEL_ID = "AUTO_RECORD";
    private EditText mETAccount;
    private EditText mETPassword;
    private Button mBtnRecord;
    private TextView mTVShow;
    private ComponentName mServiceComponent;
    private TimePicker mTimePicker;
    private Switch mSwitch;
    private SharedPreferences sharedPreferences;
    public final static int mJobIdFirst=0;
    public final static int mJobIdPeriodic=1;
    private boolean isTiming;
    private String mSelectedDate=TimeUtil.getCurrentTime();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init(){
        mETAccount=findViewById(R.id.user);
        mETPassword=findViewById(R.id.password);
        mBtnRecord=findViewById(R.id.recordButton);
        mTVShow=findViewById(R.id.textShow);
        mTimePicker=findViewById(R.id.timePicker);
        mSwitch=findViewById(R.id.switch1);
        sharedPreferences= getSharedPreferences("USER_INFO",MODE_PRIVATE);
        String defaultUser=sharedPreferences.getString("user","");
        String defaultPassword=sharedPreferences.getString("psw","");
        mETAccount.setText(defaultUser);
        mETPassword.setText(defaultPassword);
        mBtnRecord.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                final String user=mETAccount.getText().toString().trim();
                final String psw=mETPassword.getText().toString().trim();
                mTVShow.setText("网络有点慢，请稍等呢....." +
                        "" +
                        "");
                AutoRecord.autoRecord(user, psw, new HttpUtil.CallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        mTVShow.setText((String)data);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("user",user);
                        editor.putString("psw",psw);
                        editor.commit();
                        successNotification();
                        Log.d(TAG, "JobSuccess");

                    }

                    @Override
                    public void onFail(String errorInfo, String code) {
                        mTVShow.setText(errorInfo);
                        failNotification(errorInfo);
                        Log.d(TAG, "JobFail "+errorInfo);
                    }
                });

            }
        });
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {  //获取当前选择的时间
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                mSelectedDate=df.format(new Date());
                mSelectedDate=mSelectedDate+" "+hourOfDay+":"+minute+":"+"00";

            }
        });

        mSwitch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if(!mSwitch.isChecked()){
                    Log.d(TAG, "switch"+"close job");
                    closeJob();
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putBoolean("IsTiming",false);
                    editor.commit();
                }else {
                    try {
                        Log.d(TAG, "onClick: "+TimeUtil.calLastedTime(TimeUtil.sdf.parse(mSelectedDate)));
                        openJob(TimeUtil.calLastedTime(TimeUtil.sdf.parse(mSelectedDate)));
                        Toast.makeText(getApplicationContext(),"定时打卡设置成功！",Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("SelectedDate",mSelectedDate);
                        editor.putBoolean("IsTiming",true);
                        editor.commit();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mTimePicker.setIs24HourView(true);
        mSelectedDate=sharedPreferences.getString("SelectedDate",mSelectedDate);

        isTiming=sharedPreferences.getBoolean("IsTiming",false);
        if(isTiming){
            mSwitch.setChecked(true);
            try {
                Date date=TimeUtil.sdf.parse(mSelectedDate);
                openJob(TimeUtil.calLastedTime(date));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mTimePicker.setHour(date.getHours());
                    mTimePicker.setMinute(date.getMinutes());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            mSwitch.setChecked(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBtnRecord.callOnClick();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openJob(long time){
        Log.d(TAG, "openJob: "+time);
        JobScheduler mJobScheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
        mJobScheduler.cancelAll();
        //根据JobService创建一个ComponentName对象
        mServiceComponent = new ComponentName(this, MyJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(mJobIdFirst, mServiceComponent);
        builder.setMinimumLatency(time);//设置延迟调度时间
        builder.setOverrideDeadline(time+1000);//设置该Job截至时间，在截至时间前肯定会执行该Job
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);//需要网络
        mJobScheduler.schedule(builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void closeJob(){
        JobScheduler mJobScheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
        mJobScheduler.cancelAll();
    }

    /**
     * 简单通知
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void successNotification() {
        Log.d(TAG, "successNotification: ");
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //8.0 以后需要加上channelId 才能正常显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelName = "默认通知";
            manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH));
        }

        Notification.Builder mBuilder = new Notification.Builder(this,CHANNEL_ID);

        mBuilder.setSmallIcon(R.drawable.ic_user)
                .setContentTitle("打卡通知").setContentText("AutoRecord Success!");

        Intent resultIntent = new Intent(this, MainActivity.class);
        // 新开一个Activity 栈

        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        manager.notify(0, mBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public  void failNotification(String errorInfo) {
        Log.d(TAG, "successNotification: ");
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //8.0 以后需要加上channelId 才能正常显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelName = "默认通知";
            manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH));
        }

        Notification.Builder mBuilder = new Notification.Builder(this,CHANNEL_ID);

        mBuilder.setSmallIcon(R.drawable.ic_user)
                .setContentTitle("打卡通知").setContentText(TimeUtil.getCurrentTime()+" " +errorInfo+",need to be record yourself!!!!");

        Intent resultIntent = new Intent(this, MainActivity.class);
        // 新开一个Activity 栈

        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        manager.notify(0, mBuilder.build());
    }
}