package com.limelight;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class KeyInterceptorService extends AccessibilityService {

    public static volatile boolean isServiceRunning = false;
    public static volatile KeyInterceptorService instance = null;
    private SharedPreferences prefs;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        isServiceRunning = true;
        instance = this;

        // Disable key filtering by default on start unless a game session is active
        Game game = Game.activeInstance;
        updateKeyFiltering(game != null && game.isSessionActive());
    }

    @Override
    public void onDestroy() {
        isServiceRunning = false;
        instance = null;
        super.onDestroy();
    }

    public void updateKeyFiltering(boolean enable) {
        try {
            android.accessibilityservice.AccessibilityServiceInfo info = getServiceInfo();
            if (info != null) {
                if (enable) {
                    info.flags |= android.accessibilityservice.AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS;
                } else {
                    info.flags &= ~android.accessibilityservice.AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS;
                }
                setServiceInfo(info);
            }
        } catch (Exception e) {
            // Safe catch
        }
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
        try {
            if (prefs == null) {
                prefs = PreferenceManager.getDefaultSharedPreferences(this);
            }
            if (!prefs.getBoolean("checkbox_keyboard_interceptor", false)) {
                return super.onKeyEvent(event);
            }

            Game game = Game.activeInstance;
            if (game != null && game.isSessionActive()) {
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
        } catch (Exception e) {
            // Prevent accessibility service from crashing and disabling itself
        }
        return super.onKeyEvent(event);
    }
}
