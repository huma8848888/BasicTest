package com.example.basictest.permission;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * Author silence.
 * Time：2019-12-05.
 * Desc：
 */
public final class PermissionsManager {

    private static final String TAG = "PermissionsManager";

    public static PermissionsManager get(@NonNull FragmentActivity activity) {
        return new PermissionsManager(activity);
    }

    public static PermissionsManager get(@NonNull Fragment fragment) {
        return new PermissionsManager(fragment);
    }

    public void requestPermissions(String[] permissions, IPermissionCallback callback) {
        if (mPermissionsFragment != null) {
            mPermissionsFragment.get().requestPermissions(permissions, callback);
        }
    }

    private Lazy<PermissionsFragment> mPermissionsFragment;

    private PermissionsManager(@NonNull FragmentActivity activity) {
        mPermissionsFragment = getLazySingleton(activity.getSupportFragmentManager());
    }

    private PermissionsManager(@NonNull Fragment fragment) {
        mPermissionsFragment = getLazySingleton(fragment.getChildFragmentManager());
    }

    @NonNull
    private Lazy<PermissionsFragment> getLazySingleton(@NonNull final FragmentManager fragmentManager) {
        return new Lazy<PermissionsFragment>() {

            private PermissionsFragment mPermissionsFragment;

            @Override
            public synchronized PermissionsFragment get() {
                if (mPermissionsFragment == null) {
                    mPermissionsFragment = getPermissionsFragment(fragmentManager);
                }
                return mPermissionsFragment;
            }

        };
    }

    private PermissionsFragment getPermissionsFragment(@NonNull final FragmentManager fragmentManager) {
        PermissionsFragment PermissionsFragment = findPermissionsFragment(fragmentManager);
        boolean isNewInstance = PermissionsFragment == null;
        if (isNewInstance) {
            PermissionsFragment = new PermissionsFragment();
            fragmentManager
                    .beginTransaction()
                    .add(PermissionsFragment, TAG)
                    .commitNow();
        }
        return PermissionsFragment;
    }

    private PermissionsFragment findPermissionsFragment(@NonNull final FragmentManager fragmentManager) {
        return (PermissionsFragment) fragmentManager.findFragmentByTag(TAG);
    }

    private interface Lazy<V> {
        V get();
    }
}
