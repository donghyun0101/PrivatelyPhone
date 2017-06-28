package com.kdh.privatelyphone;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by KDH on 2016-12-15.
 */

public class LockService extends Service implements SensorEventListener
{
    private static SharedPreferences off = null, manner = null;
    private SharedPreferences.Editor editor_off, editor_manner;

    private SensorManager mSensorManager;
    private Sensor mProximity;
    private AudioManager am;
    private DevicePolicyManager mDPM;
    private ComponentName comp;

    @Override
    public void onCreate()
    {
        off = LockService.this.getSharedPreferences("kdh_lock_service", 0);
        manner = LockService.this.getSharedPreferences("kdh_manner_service", 0);
        editor_off = off.edit();
        editor_manner = manner.edit();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // TODO Auto-generated method stub
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);

        editor_off.putInt("kdh_lock_service", 1);
        editor_off.commit();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        mSensorManager.unregisterListener(this);
        editor_off.putInt("kdh_lock_service", 0);
        editor_manner.putInt("kdh_manner_service", 0);
        editor_off.commit();
        editor_manner.commit();
        super.onDestroy();
    }

    public final void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }

    @Override
    public final void onSensorChanged(SensorEvent event)
    {
        float distance = event.values[0];
        Log.i("Distance", "" + distance);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();

        if (isScreenOn && distance <= 0)
        {
            comp = new ComponentName(LockService.this, ScreenLockDeviceAdminReceiver.class);

            mDPM = (DevicePolicyManager) LockService.this.getSystemService(Context.DEVICE_POLICY_SERVICE);
            mDPM.lockNow();

            if(manner.getInt("kdh_manner_service", 0) == 1)
            {
                am = (AudioManager) LockService.this.getSystemService(Context.AUDIO_SERVICE);
                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            }
        }
    }
}