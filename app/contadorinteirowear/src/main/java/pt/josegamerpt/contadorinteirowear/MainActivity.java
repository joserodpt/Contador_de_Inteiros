package pt.josegamerpt.contadorinteirowear;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

import androidx.core.view.InputDeviceCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewConfigurationCompat;

import com.github.florent37.viewanimator.ViewAnimator;
import com.thekhaeng.pushdownanim.PushDownAnim;

import pt.josegamerpt.contadorinteirowear.databinding.ActivityMainBinding;


public class MainActivity extends Activity {

    public static Boolean limit = false;
    public static Boolean vibration = true;
    public static int limitMax;
    public static SharedPreferences sharedpref;
    public static SharedPreferences.Editor sharedprededitor;
    static TextView limitText;
    private static Activity main;
    public ActivityMainBinding binding;
    TextView display;
    int counter = 0;
    //asked
    private Boolean asked = false;
    private int animDelay = 25;

    public static void setLimit(boolean b, int parseInt) {
        limit = b;
        limitMax = parseInt;
        if (b) {
            limitText.setVisibility(View.VISIBLE);
            limitText.setText(limitMax + main.getResources().getString(R.string.limit));
        } else {
            limitText.setVisibility(View.INVISIBLE);
        }
        sharedprededitor.putBoolean("limit", b);
        sharedprededitor.putInt("limitint", parseInt);
        sharedprededitor.apply();
    }

    public static void registerVib(boolean i) {
        vibration = i;
        sharedprededitor.putBoolean("vibrate", i);
        sharedprededitor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        main = this;

        sharedpref = getApplicationContext().getSharedPreferences("wholeCounter", 0);
        sharedprededitor = sharedpref.edit();

        limitText = binding.limitText;
        display = binding.numberDisplay;
        display.setText(counter + "");
        display.setOnLongClickListener(v -> {
            counter = 0;
            display.setText(counter + "");
            registerNumber();
            AppUtils.vibrate(MainActivity.this, 300);
            return false;
        });

        vibration = sharedpref.getBoolean("vibrate", true);

        if (sharedpref.getInt("counter", 0) != 0) {
            //ask for restore
            counter = sharedpref.getInt("counter", 0);
            display.setText(counter + "");
            setLimit(sharedpref.getBoolean("limit", false), sharedpref.getInt("limitint", 0));
            asked = true;
        }

        PushDownAnim.setPushDownAnimTo(binding.settingIcon)
                .setScale(PushDownAnim.MODE_SCALE, 0.89f).setOnClickListener(v -> MainActivity.this.startActivity(new Intent(MainActivity.this, Settings.class)));

        PushDownAnim.setPushDownAnimTo(binding.add)
                .setScale(PushDownAnim.MODE_SCALE, 0.89f).setOnClickListener(v -> increment());

        PushDownAnim.setPushDownAnimTo(binding.sub)
                .setScale(PushDownAnim.MODE_SCALE, 0.89f).setOnClickListener(v -> decrease());

        binding.numberDisplay.requestFocus();
        binding.numberDisplay.setOnGenericMotionListener((v, ev) -> {
            if (ev.getAction() == MotionEvent.ACTION_SCROLL &&
                    ev.isFromSource(InputDeviceCompat.SOURCE_ROTARY_ENCODER)
            ) {
                float delta = -ev.getAxisValue(MotionEventCompat.AXIS_SCROLL) *
                        ViewConfigurationCompat.getScaledVerticalScrollFactor(
                                ViewConfiguration.get(getApplicationContext()), getApplicationContext()
                        );

                int round = Math.round(delta);
                if (round > 0) {
                    increment();
                } else {
                    decrease();
                }
                return true;
            }
            return false;
        });


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt("countInt", counter);
        savedInstanceState.putBoolean("asked", asked);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        counter = savedInstanceState.getInt("countInt");
        asked = savedInstanceState.getBoolean("asked");

        display.setText(counter + "");

        if (limit) {
            limitText.setVisibility(View.VISIBLE);
            limitText.setText(limitMax + main.getResources().getString(R.string.limit));
        }
    }

    public Boolean isInLimitUp() {
        return (counter + 1) > limitMax && limit;
    }

    public Boolean isInLimitDown() {
        return (counter > limitMax) && limit;
    }

    public void increment() {
        if (isInLimitUp()) {
            ViewAnimator.animate(display)
                    .translationX(-50, 0)
                    .duration(animDelay)
                    .thenAnimate(display)
                    .translationX(+50, 0)
                    .duration(animDelay)
                    .thenAnimate(display)
                    .translationX(-50, 0)
                    .duration(animDelay)
                    .thenAnimate(display)
                    .translationX(+50, 0)
                    .duration(animDelay)
                    .start();
            AppUtils.vibrate(this, 700);
            AppUtils.setColor(this, Color.RED);
        } else {
            AppUtils.setColor(this, Color.BLACK);
            counter++;
            display.setText(counter + "");
            ViewAnimator.animate(display)
                    .translationY(+50, 0)
                    .duration(animDelay)
                    .thenAnimate(display)
                    .translationY(-50, 0)
                    .duration(animDelay)
                    .start();
            registerNumber();
            AppUtils.vibrate(this, 50);
        }
    }

    private void registerNumber() {
        sharedprededitor.putInt("counter", counter);
        sharedprededitor.apply();
    }

    public void decrease() {
        if (!isInLimitDown()) {
            if (counter == 0) {
                ViewAnimator.animate(display)
                        .translationX(-50, 0)
                        .duration(animDelay)
                        .thenAnimate(display)
                        .translationX(+50, 0)
                        .duration(animDelay)
                        .thenAnimate(display)
                        .translationX(-50, 0)
                        .duration(animDelay)
                        .thenAnimate(display)
                        .translationX(+50, 0)
                        .duration(animDelay)
                        .start();
                AppUtils.vibrate(this, 700);
                AppUtils.setColor(this, Color.RED);
            } else {
                AppUtils.setColor(this, Color.BLACK);
                counter--;
                display.setText(counter + "");
                ViewAnimator.animate(display)
                        .translationY(-50, 0)
                        .duration(animDelay)
                        .thenAnimate(display)
                        .translationY(+50, 0)
                        .duration(animDelay)
                        .start();
                registerNumber();
                AppUtils.vibrate(this, 50);
            }
        } else {
            AppUtils.setColor(this, Color.BLACK);
            counter--;
            display.setText(counter + "");
            ViewAnimator.animate(display)
                    .translationY(-50, 0)
                    .duration(animDelay)
                    .thenAnimate(display)
                    .translationY(+50, 0)
                    .duration(animDelay)
                    .start();
            AppUtils.vibrate(this, 50);
        }
    }
}