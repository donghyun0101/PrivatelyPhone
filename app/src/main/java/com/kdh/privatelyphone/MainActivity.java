package com.kdh.privatelyphone;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private Intent Lock_Service;

    private Switch service_off_btn, service_manner_btn;
    private Button delete_btn, as_btn;

    private static SharedPreferences off = null, manner = null;
    private SharedPreferences.Editor editor_off, editor_manner;

    private ComponentName comp;
    private DevicePolicyManager mDPM;

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_main);

        off = MainActivity.this.getSharedPreferences("kdh_lock_service", 0);
        manner = MainActivity.this.getSharedPreferences("kdh_manner_service", 0);
        editor_off = off.edit();
        editor_manner = manner.edit();

        Lock_Service = new Intent(this, LockService.class);

        service_off_btn = (Switch) findViewById(R.id.service_off_btn);
        service_manner_btn = (Switch) findViewById(R.id.service_manner_btn);

        tv = (TextView)findViewById(R.id.tv);

        service_off_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                if (isChecked == true)
                {
                    startService(Lock_Service);
                    service_manner_btn.setEnabled(true);
                    tv.setTextColor(Color.parseColor("#ffffff"));
                }

                else
                {
                    stopService(Lock_Service);
                    service_manner_btn.setChecked(false);
                    service_manner_btn.setEnabled(false);
                    tv.setTextColor(Color.parseColor("#d2d2d2"));
                }
            }
        });

        service_manner_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                if (isChecked == true)
                {
                    editor_manner.putInt("kdh_manner_service", 1);
                }
                else
                {
                    editor_manner.putInt("kdh_manner_service", 0);
                }
                editor_manner.commit();
            }
        });

        if(off.getInt("kdh_lock_service", 0) == 1)
        {
            service_off_btn.setChecked(true);
            tv.setTextColor(Color.parseColor("#ffffff"));
        }

        else
        {
            service_off_btn.setChecked(false);
            tv.setTextColor(Color.parseColor("#d2d2d2"));
        }

        if(manner.getInt("kdh_manner_service", 0) == 1)
            service_manner_btn.setChecked(true);
        else
            service_manner_btn.setChecked(false);

        delete_btn = (Button)findViewById(R.id.delete_app);
        as_btn = (Button)findViewById(R.id.add_system_please);

        delete_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                comp = new ComponentName(MainActivity.this, ScreenLockDeviceAdminReceiver.class);
                mDPM = (DevicePolicyManager)MainActivity.this.getSystemService(Context.DEVICE_POLICY_SERVICE);
                mDPM.removeActiveAdmin(comp);
                finish();
            }
        });

        as_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Uri uri = Uri.parse("https://goto.kakao.com/@lionex");
                Intent it  = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(it);
                Toast.makeText(MainActivity.this, "친구추가 후 추가하면 좋을 기능을 건의 해 주세요!", Toast.LENGTH_LONG).show();
            }
        });
    }
}