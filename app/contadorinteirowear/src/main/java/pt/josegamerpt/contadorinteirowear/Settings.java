package pt.josegamerpt.contadorinteirowear;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.rm.rmswitch.RMSwitch;

import pt.josegamerpt.contadorinteirowear.databinding.ActivitySettingsBinding;

public class Settings extends Activity {

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final TextView ti = binding.limitinput;
        final Button b = binding.save;

        if (MainActivity.limit) {
            ti.setText(MainActivity.limitMax + "");
        }

        binding.switchvib.setChecked(MainActivity.vibration);

        final RMSwitch s = binding.switchvib;
        s.addSwitchObserver((buttonView, isChecked) -> MainActivity.registerVib(isChecked));

        b.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(ti.getText())) {
                MainActivity.setLimit(true, Integer.parseInt(ti.getText() + ""));
            } else {
                MainActivity.setLimit(false, 0);
            }
            finish();
        });

        binding.scroll.requestFocus();

    }
}