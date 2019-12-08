package com.example.basictest.permission;

import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Random;


public final class PermissionsFragment extends Fragment {

    private SparseArray<IPermissionCallback> mCallbacks = new SparseArray<>();
    private Random mCodeGenerator = new Random();

    public PermissionsFragment() {
    }

    public static PermissionsFragment newInstance() {
        return new PermissionsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    /**
     * 随机生成唯一的requestCode，最多尝试10次
     */
    private int makeRequestCode() {
        int requestCode;
        int tryCount = 0;
        do {
            requestCode = mCodeGenerator.nextInt(0x0000FFFF);
            tryCount++;
        } while (mCallbacks.indexOfKey(requestCode) >= 0 && tryCount < 10);
        return requestCode;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        IPermissionCallback callback = mCallbacks.get(requestCode);
        mCallbacks.remove(requestCode);

        if (callback == null) {
            return;
        }

        boolean allGranted = true;
        boolean shouldTip = false;
        ArrayList<String> denyPermission = new ArrayList<String>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                denyPermission.add(permissions[i]);
                shouldTip = !shouldShowRequestPermissionRationale(permissions[i]);
            }
        }
        if (allGranted) {
            callback.onGranted();
        } else {
            callback.onDenied(shouldTip, denyPermission.toArray(new String[0]));
        }
    }

    public void requestPermissions(String[] permissions, IPermissionCallback eachPermissionListener) {
        int requestCode = makeRequestCode();
        mCallbacks.put(requestCode, eachPermissionListener);
        requestPermissions(permissions, requestCode);
    }

}
