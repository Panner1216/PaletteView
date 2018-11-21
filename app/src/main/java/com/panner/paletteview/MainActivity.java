package com.panner.paletteview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.panner.paletteview.view.ColorPickerView;

public class MainActivity extends AppCompatActivity {

    private ColorPickerView mPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPickerView = findViewById(R.id.picker_view);
        mPickerView.setOnColorPickListener(new ColorPickerView.PickerViewListener() {
            @Override
            public void onColorPicker(int color) {

            }
        });
    }
}
