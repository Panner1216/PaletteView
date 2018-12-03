package com.panner.paletteview.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.panner.paletteview.R;
import com.panner.paletteview.listener.PickerViewListener;
import com.panner.paletteview.view.DoubleColorPickerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoubleColorPickerActivity extends AppCompatActivity {
    @BindView(R.id.double_color_picker_view)
    DoubleColorPickerView colorPickerView;
    @BindView(R.id.picker_view_r)
    TextView mRed;
    @BindView(R.id.picker_view_g)
    TextView mGreen;
    @BindView(R.id.picker_view_b)
    TextView mBlue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_color_picker);
        ButterKnife.bind(this);
        colorPickerView.setOnColorPickerListener(new PickerViewListener() {
            @Override
            public void onPickerColor(int color) {
                ((View)colorPickerView.getParent()).setBackgroundColor(color);
                int red = (color & 0xff0000) >> 16;
                int green = (color & 0x00ff00) >> 8;
                int blue = (color & 0x0000ff);

                mRed.setText(String.valueOf(red));
                mGreen.setText(String.valueOf(green));
                mBlue.setText(String.valueOf(blue));
            }
        });
    }
}
