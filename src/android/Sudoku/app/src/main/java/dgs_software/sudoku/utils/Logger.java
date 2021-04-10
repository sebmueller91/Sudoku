package dgs_software.sudoku.utils;

import android.util.Log;

import dgs_software.sudoku.BuildConfig;
import dgs_software.sudoku.config.GlobalConfig;

public class Logger {
    public static void LogDebug(String message, Exception e) {
        if (BuildConfig.LOG_DEBUG_MODE) {
            Log.d(GlobalConfig.LOGTAG, message, e);
        }
    }

    public static void LogDebug(String message) {
        if (BuildConfig.LOG_DEBUG_MODE) {
            Log.d(GlobalConfig.LOGTAG, message);
        }
    }

    public static void LogWarning(String message, Exception e) {
        if (BuildConfig.LOG_DEBUG_MODE) {
            Log.w(GlobalConfig.LOGTAG, message, e);
        }
    }

    public static void LogWarning(String message) {
        if (BuildConfig.LOG_DEBUG_MODE) {
            Log.w(GlobalConfig.LOGTAG, message);
        }
    }

    public static void LogError(String message, Exception e) {
        if (BuildConfig.LOG_DEBUG_MODE) {
            Log.e(GlobalConfig.LOGTAG, message, e);
        }
    }

    public static void LogError(String message) {
        if (BuildConfig.LOG_DEBUG_MODE) {
            Log.e(GlobalConfig.LOGTAG, message);
        }
    }
}
