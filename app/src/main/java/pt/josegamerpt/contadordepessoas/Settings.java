package pt.josegamerpt.contadordepessoas;

import static pt.josegamerpt.contadordepessoas.AppUtils.setColor;
import static pt.josegamerpt.contadordepessoas.R.id;
import static pt.josegamerpt.contadordepessoas.R.layout;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rm.rmswitch.RMSwitch;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_settings);

        final TextView ti = this.findViewById(id.limitinput);
        final Button b = this.findViewById(id.save);
        final RMSwitch rm = this.findViewById(R.id.switchvib);
        rm.addSwitchObserver((switchView, isChecked) -> MainActivity.registerVib(isChecked));
        rm.setChecked(MainActivity.vibration);

        if (MainActivity.limit) {
            ti.setText(MainActivity.limitMax + "");
        }

        checkDark(getResources().getConfiguration());

        b.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(ti.getText())) {
                MainActivity.setLimit(true, Integer.parseInt(ti.getText() + ""));
            } else {
                MainActivity.setLimit(false, 0);
            }
            finish();
        });

    }

    public void checkDark(Configuration config) {
        int currentNightMode = config.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                //day
                setColor(this, Color.WHITE);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                //night
                setColor(this, Color.BLACK);
                break;
        }
    }
}