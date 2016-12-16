package com.kdh.privatelyphone;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by KDH on 2016-12-15.
 */

public class SplashActivity extends AppCompatActivity
{
    DevicePolicyManager mDPM;
    ComponentName comp;

    private Button device_set_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_splash);

        comp = new ComponentName(SplashActivity.this, ScreenLockDeviceAdminReceiver.class);
        mDPM = (DevicePolicyManager) SplashActivity.this.getSystemService(Context.DEVICE_POLICY_SERVICE);

        if (mDPM.isAdminActive(comp))
        {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        device_set_btn = (Button)findViewById(R.id.device_set);
        device_set_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, comp);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "화면을 끄는 것 이외에 그 어떤 보안관련기능을 사용하지 않으니 안심하고 허용하셔도 됩니다.");
                startActivityForResult(intent, 101);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 101)
            if (resultCode == RESULT_OK)
            {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
            else
            {
                Toast.makeText(this, "허용하시지 않으면 기능이 정상작동하지 않습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
    }
}
