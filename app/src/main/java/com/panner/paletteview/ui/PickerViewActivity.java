package com.panner.paletteview.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.panner.paletteview.R;
import com.panner.paletteview.listener.PickerViewListener;
import com.panner.paletteview.view.ColorPickerView;

public class PickerViewActivity extends AppCompatActivity {
    private ColorPickerView mPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_view);
        mPickerView = findViewById(R.id.picker_view);
        mPickerView.setOnColorPickListener(new PickerViewListener() {
            @Override
            public void onPickerColor(int color) {

            }

        });
    }
}
