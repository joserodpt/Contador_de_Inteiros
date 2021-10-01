package pt.josegamerpt.contadorinteirowear;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class AppUtils {

    public static void vibrate(Context c, int miliseconds) {
        if (MainActivity.vibration)
            ((Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(miliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    public static void setColor(MainActivity mainActivity, int a) {
        mainActivity.binding.backg.setBackgroundColor(a);
    }
}
