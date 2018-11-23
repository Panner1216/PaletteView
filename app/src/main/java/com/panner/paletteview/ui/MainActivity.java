package com.panner.paletteview.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.panner.paletteview.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.open_picker_view)
    void openPickerView() {
        startActivity(new Intent(this, PickerViewActivity.class));
    }

    @OnClick(R.id.open_rectangle_picker)
    void openRectangleView() {
        startActivity(new Intent(this, RectanglePickerActivity.class));
    }
}
