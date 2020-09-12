package com.example.myapplication.JobService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.myapplication.AutoRecord.AutoRecord;
import com.example.myapplication.AutoRecord.HttpUtil;
import com.example.myapplication.AutoRecord.TimeUtil;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import static com.example.myapplication.MainActivity.mJobIdPeriodic;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {
    private static final String CHANNEL_ID = "AUTO_RECORD";
    private static final String TAG = "MY_JOB_SERVICE";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        Log.d(TAG, "onStartJob: ");
        final SharedPreferences sharedPreferences= getSharedPreferences("USER_INFO",MODE_PRIVATE);
        String defaultUser=sharedPreferences.getString("user","");
        String defaultPassword=sharedPreferences.getString("psw","");
        AutoRecord.autoRecord(defaultUser, defaultPassword, new HttpUtil.CallBack() {
            @Override
            public void onSuccess(Object data) {

                openPeriodicJob();
                Log.d(TAG, "JobSuccess ");
                successNotification();
                jobFinished(jobParameters,false);
            }

            @Override
            public void onFail(String errorInfo, String code) {
                Log.d(TAG, "JobFail "+errorInfo);
                failNotification(errorInfo);
                jobFinished(jobParameters,false);

            }
        });
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openPeriodicJob(){
        JobScheduler mJobScheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
        mJobScheduler.cancel(mJobIdPeriodic);
        ComponentName mServicePeriodicComponent= new ComponentName(this, MyJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(mJobIdPeriodic, mServicePeriodicComponent);
        builder.setMinimumLatency(86400000);//设置延迟调度时间
        builder.setOverrideDeadline(86400000+1000);//设置该Job截至时间，在截至时间前肯定会执行该Job
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);//需要网络
        mJobScheduler.schedule(builder.build());
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
                .setContentTitle("定时打卡通知").setContentText("AutoRecord Success!");

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
                .setContentTitle("定时打卡通知").setContentText(TimeUtil.getCurrentTime()+" " +errorInfo+",need to be record yourself!!!!");

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
