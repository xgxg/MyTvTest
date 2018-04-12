package com.test.xg.mytvtest;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author 黄冬榕
 * @date 2017/12/25
 * @description
 * @remark
 */

public class MyWindowManager {

    /**
     * 小悬浮窗View的实例
     */
    private static FloatWindowSmallView smallWindow;

    /**
     * 大悬浮窗View的实例
     */
    private static FloatWindowBigView bigWindow;

    /**
     * 小悬浮窗View的参数
     */
    private static LayoutParams smallWindowParams;

    /**
     * 大悬浮窗View的参数
     */
    private static LayoutParams bigWindowParams;

    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private static WindowManager mWindowManager;

    /**
     * 用于获取手机可用内存
     */
    private static ActivityManager mActivityManager;
    private  Context mCtx;
    private  int mScreenWidth;
    private  int mScreenHeight;
    private static MyWindowManager myWindowManager;


    private MyWindowManager(Context mCtx) {
        this.mCtx = mCtx;
        mWindowManager = (WindowManager) mCtx
                .getSystemService(Context.WINDOW_SERVICE);

        mScreenWidth = mWindowManager.getDefaultDisplay().getWidth();
        mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();
    }

    public static MyWindowManager getFloatBallManager(Context mCtx) {
        if (null == myWindowManager)
            myWindowManager = new MyWindowManager(mCtx);
        return myWindowManager;
    }

    public WindowManager getmWindowManager() {
        return mWindowManager;
    }


    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     *
     */
    public void createSmallWindow() {
        if (smallWindow == null) {
            smallWindow = new FloatWindowSmallView(mCtx);
            if (smallWindowParams == null) {
                smallWindowParams = new LayoutParams();
                smallWindowParams.type = LayoutParams.TYPE_SYSTEM_ERROR;
                smallWindowParams.format = PixelFormat.RGBA_8888;
                smallWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_NOT_FOCUSABLE;
                smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                smallWindowParams.width = FloatWindowSmallView.viewWidth;
                smallWindowParams.height = FloatWindowSmallView.viewHeight;
                smallWindowParams.x = mScreenWidth;
                smallWindowParams.y = mScreenHeight / 2;
            }
            smallWindow.setParams(smallWindowParams);
            mWindowManager.addView(smallWindow, smallWindowParams);
        }
    }

    /**
     * 将小悬浮窗从屏幕上移除。
     *
     */
    public static void removeSmallWindow() {
        if (smallWindow != null) {
            mWindowManager.removeView(smallWindow);
            smallWindow = null;
        }
    }

    /**
     * 创建一个大悬浮窗。位置为屏幕正中间。
     *
     */
    public  void createBigWindow() {
        if (bigWindow == null) {
            bigWindow = new FloatWindowBigView(mCtx);
            if (bigWindowParams == null) {
                bigWindowParams = new LayoutParams();
                bigWindowParams.x = mScreenWidth / 2 - FloatWindowBigView.viewWidth / 2;
                bigWindowParams.y = mScreenHeight / 2 - FloatWindowBigView.viewHeight / 2;
                bigWindowParams.type = LayoutParams.TYPE_SYSTEM_ERROR;
                bigWindowParams.format = PixelFormat.RGBA_8888;
                bigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                bigWindowParams.width = FloatWindowBigView.viewWidth;
                bigWindowParams.height = FloatWindowBigView.viewHeight;
            }
            mWindowManager.addView(bigWindow, bigWindowParams);
        }
    }

    /**
     * 将大悬浮窗从屏幕上移除。
     *
     */
    public static void removeBigWindow() {
        if (bigWindow != null) {
            mWindowManager.removeView(bigWindow);
            bigWindow = null;
        }
    }

    /**
     * 更新小悬浮窗的TextView上的数据，显示内存使用的百分比。
     *
     */
    public void updateUsedPercent() {
        if (smallWindow != null) {
            TextView percentView = (TextView) smallWindow.findViewById(R.id.percent);
            percentView.setText(getUsedPercentValue());
        }
    }

    /**
     * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
     *
     * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
     */
    public static boolean isWindowShowing() {
        return smallWindow != null || bigWindow != null;
    }


    /**
     * 如果ActivityManager还未创建，则创建一个新的ActivityManager返回。否则返回当前已创建的ActivityManager。
     *
     * @param context
     *            可传入应用程序上下文。
     * @return ActivityManager的实例，用于获取手机可用内存。
     */
    private static ActivityManager getActivityManager(Context context) {
        if (mActivityManager == null) {
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        return mActivityManager;
    }

    /**
     * 计算已使用内存的百分比，并返回。
     *
     * @return 已使用内存的百分比，以字符串形式返回。
     */
    public String getUsedPercentValue() {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
            long availableSize = getAvailableMemory(mCtx) / 1024;
            int percent = (int) ((totalMemorySize - availableSize) / (float) totalMemorySize * 100);
            return percent + "%";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "悬浮窗";
    }

    /**
     * 获取当前可用内存，返回数据以字节为单位。
     *
     * @param context
     *            可传入应用程序上下文。
     * @return 当前可用内存。
     */
    private static long getAvailableMemory(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        getActivityManager(context).getMemoryInfo(mi);
        return mi.availMem;
    }

}
