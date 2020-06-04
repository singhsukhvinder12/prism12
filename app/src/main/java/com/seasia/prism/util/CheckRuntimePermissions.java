package com.seasia.prism.util;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class CheckRuntimePermissions {

    private CheckRuntimePermissions() {
    }

    /**
     * method check if permission is granted or not if not then it will show dialog
     *
     * @param context    of the calling class
     * @param permission to check if granted or not
     * @return
     */
    public static int checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission);
    }

    /**
     * method to check if all multiple/single permission is granted or not
     *
     * @param appCompatActivity       calling activity
     * @param permissionsToGrant      permission list in string array
     * @param permissionRequestCode to identify every permission check
     * @return if granted or not
     */
    public static boolean checkMashMallowPermissions(AppCompatActivity appCompatActivity, String[] permissionsToGrant, int permissionRequestCode) {
        ArrayList<String> permissions = new ArrayList<>();
        for (String permission : permissionsToGrant) {
            if (checkPermission(appCompatActivity, permission) != 0) {
                permissions.add(permission);
            }
        }
        //if all permission is granted
        if (permissions.isEmpty()) {
            return true;
        }
        ActivityCompat.requestPermissions(appCompatActivity, permissions.toArray(new String[permissions.size()]), permissionRequestCode);
        return false;
    }
}
