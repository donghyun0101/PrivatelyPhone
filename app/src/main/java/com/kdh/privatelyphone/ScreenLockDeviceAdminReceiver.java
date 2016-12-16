package com.kdh.privatelyphone;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by KDH on 2016-12-15.
 */

public class ScreenLockDeviceAdminReceiver extends DeviceAdminReceiver
{
    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Toast.makeText(context, "정상적으로 설정되었습니다.", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Toast.makeText(context, "정상적으로 해제되었습니다.\n어플을 삭제하실 수 있습니다.", Toast.LENGTH_LONG).show();
    }

}
