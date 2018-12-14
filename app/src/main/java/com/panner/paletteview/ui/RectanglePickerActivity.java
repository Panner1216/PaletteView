package com.panner.paletteview.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.panner.paletteview.R;
import com.panner.paletteview.listener.PickerViewListener;
import com.panner.paletteview.view.RectanglePickerView;

public class RectanglePickerActivity extends AppCompatActivity {

    private RectanglePickerView mPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rectangle_picker);
        mPickerView = findViewById(R.id.rectangle_picker_view);
        mPickerView.setOnColorPickerListener(new PickerViewListener() {
            @Override
            public void onPickerColor(int color) {
                ((View)mPickerView.getParent()).setBackgroundColor(color);
            }
        });

    }
}
