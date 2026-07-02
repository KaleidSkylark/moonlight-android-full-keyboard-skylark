package com.limelight;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class KeyInterceptorService extends AccessibilityService {

    public static volatile boolean isServiceRunning = false;
    private boolean isInterceptorEnabled = false;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isInterceptorEnabled = prefs.getBoolean("checkbox_keyboard_interceptor", false);
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if ("checkbox_keyboard_interceptor".equals(key)) {
                    isInterceptorEnabled = sharedPreferences.getBoolean(key, false);
                }
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        isServiceRunning = true;
    }

    @Override
    public void onDestroy() {
        isServiceRunning = false;
        if (listener != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.unregisterOnSharedPreferenceChangeListener(listener);
        }
        super.onDestroy();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // We do not need to handle accessibility events
    }

    @Override
    public void onInterrupt() {
        // We do not need to handle interrupts
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        if (!isInterceptorEnabled) {
            return super.onKeyEvent(event);
        }

        Game game = Game.activeInstance;
        if (game != null && game.isInputGrabbed()) {
            int keyCode = event.getKeyCode();
            
            // Exclude power, volume, and system navigation keys (Home, Back)
            if (keyCode == KeyEvent.KEYCODE_POWER ||
                keyCode == KeyEvent.KEYCODE_VOLUME_UP ||
                keyCode == KeyEvent.KEYCODE_VOLUME_DOWN ||
                keyCode == KeyEvent.KEYCODE_VOLUME_MUTE ||
                keyCode == KeyEvent.KEYCODE_HOME ||
                keyCode == KeyEvent.KEYCODE_BACK) {
                return super.onKeyEvent(event);
            }

            // Forward the event to the active Game activity
            game.handleAccessibilityKeyEvent(event);
            return true; // Consume the event so Android OS does not handle it
        }
        return super.onKeyEvent(event);
    }
}
