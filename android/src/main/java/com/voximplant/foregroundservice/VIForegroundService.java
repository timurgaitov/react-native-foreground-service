/*
 * Copyright (c) 2011-2019, Zingaya, Inc. All rights reserved.
 */

package com.voximplant.foregroundservice;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.IBinder;

import static com.voximplant.foregroundservice.Constants.NOTIFICATION_CONFIG;

public class VIForegroundService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action != null) {
            if (action.equals(Constants.ACTION_FOREGROUND_SERVICE_START)) {
                if (intent.getExtras() != null && intent.getExtras().containsKey(NOTIFICATION_CONFIG)) {
                    Bundle notificationConfig = intent.getExtras().getBundle(NOTIFICATION_CONFIG);
                    if (notificationConfig != null && notificationConfig.containsKey("id")) {
                        Notification notification = NotificationHelper.getInstance(getApplicationContext())
                                .buildNotification(getApplicationContext(), notificationConfig);

                        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.Q) {
                            startForeground((int)notificationConfig.getDouble("id"), notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE);
                        } else {
                            startForeground((int)notificationConfig.getDouble("id"), notification);
                        }
                    }
                }
            } else if (action.equals(Constants.ACTION_FOREGROUND_SERVICE_STOP)) {
                stopSelf();
            }
        }
        return START_NOT_STICKY;

    }
}