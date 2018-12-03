package com.panner.paletteview.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.panner.paletteview.R;
import com.panner.paletteview.listener.PickerViewListener;
import com.panner.paletteview.view.ColorPickerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickerViewActivity extends AppCompatActivity {
    @BindView(R.id.color_value)
    TextView mColorValue;
    @BindView(R.id.picker_view)
    ColorPickerView mPickerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_view);
        ButterKnife.bind(this);
        mPickerView.setOnColorPickListener(new PickerViewListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onPickerColor(int color) {
                ((View) mPickerView.getParent()).setBackgroundColor(color);
                mColorValue.setText(Color.valueOf(color).toString());
            }

        });
    }
}
