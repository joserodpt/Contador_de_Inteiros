package pt.josegamerpt.contadordepessoas;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.florent37.viewanimator.ViewAnimator;
import com.thekhaeng.pushdownanim.PushDownAnim;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static pt.josegamerpt.contadordepessoas.AppUtils.setColor;

public class MainActivity extends AppCompatActivity {

    //limit
    public static Boolean limit = false;
    public static int limitMax;
    public static SharedPreferences sharedpref;
    public static SharedPreferences.Editor sharedprededitor;
    static TextView limitText;
    private static Activity main;
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
            limitText.setText(main.getResources().getString(R.string.limit) + limitMax);
        } else {
            limitText.setVisibility(View.INVISIBLE);
        }
        sharedprededitor.putBoolean("limit", b);
        sharedprededitor.putInt("limitint", parseInt);
        sharedprededitor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main = this;

        sharedpref = getApplicationContext().getSharedPreferences("wholeCounter", 0);
        sharedprededitor = sharedpref.edit();

        checkDark(getResources().getConfiguration());

        limitText = main.findViewById(R.id.limitText);
        display = findViewById(R.id.numberDisplay);
        display.setText(counter + "");
        display.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                counter = 0;
                display.setText(counter + "");
                AppUtils.vibrate(MainActivity.this, 300);
                return false;
            }
        });


        PushDownAnim.setPushDownAnimTo(findViewById(R.id.settingIcon))
                .setScale(PushDownAnim.MODE_SCALE, 0.89f).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, Settings.class));
            }
        });

        PushDownAnim.setPushDownAnimTo(findViewById(R.id.add))
                .setScale(PushDownAnim.MODE_SCALE, 0.89f).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increment();
            }
        });

        PushDownAnim.setPushDownAnimTo(findViewById(R.id.sub))
                .setScale(PushDownAnim.MODE_SCALE, 0.89f).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrease();
            }
        });


        //check last session

        if (!asked) {
            if (sharedpref.getInt("counter", 0) != 0) {
                //ask for restore
                final SweetAlertDialog ask = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
                ask.setTitleText(getApplicationContext().getString(R.string.restore_session));
                ask.setCancelable(false);
                ask.setCancelButton(getApplicationContext().getString(android.R.string.cancel), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sharedprededitor.putInt("counter", 0);
                        sharedprededitor.apply();
                        asked = true;
                        ask.dismissWithAnimation();
                    }
                });
                ask.setConfirmButton(getApplicationContext().getString(android.R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        counter = sharedpref.getInt("counter", 0);
                        display.setText(counter + "");
                        setLimit(sharedpref.getBoolean("limit", false), sharedpref.getInt("limitint", 0));
                        asked = true;
                        ask.dismissWithAnimation();
                    }
                });
                ask.show();

            }
        }
    }

    public void checkDark(Configuration config) {
        int currentNightMode = config.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                //day
                setColor(this, Color.WHITE);
                ((TextView) findViewById(R.id.limitText)).setTextColor(Color.BLACK);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                //night
                setColor(this, Color.BLACK);
                ((TextView) findViewById(R.id.limitText)).setTextColor(Color.WHITE);
                break;
        }
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
            limitText.setText(main.getResources().getString(R.string.limit) + limitMax);
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
            checkDark(getResources().getConfiguration());
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
                checkDark(getResources().getConfiguration());
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
            checkDark(getResources().getConfiguration());
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                increment();
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                decrease();
                break;
        }

        return true;
    }
}