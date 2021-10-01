package pt.josegamerpt.contadordepessoas;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class AppUtils {

    public static void setColor(Activity c, int co)
    {
        AppUtils.setWindowFlag(c, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        if (co == Color.WHITE) {
            c.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        c.getWindow().setStatusBarColor(co);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            c.getWindow().setNavigationBarColor(co);
        }
        c.findViewById(R.id.main).setBackgroundColor(co);
    }

    public static void vibrate(Context c, int miliseconds) {
        if (MainActivity.vibration) {
            Vibrator v = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(miliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(miliseconds);
            }
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


}
