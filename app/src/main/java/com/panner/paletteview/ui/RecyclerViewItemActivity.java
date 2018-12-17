package com.panner.paletteview.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.panner.paletteview.R;
import com.panner.paletteview.view.RecyclerViewItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewItemActivity extends AppCompatActivity {
    @BindView(R.id.view)
    RecyclerViewItem mView;
    @BindView(R.id.view1)
    RecyclerViewItem mView1;
    @BindView(R.id.view2)
    RecyclerViewItem mView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_item);
        ButterKnife.bind(this);
        ArrayList<String> strings = new ArrayList<String>() {{
            add("rgb");
            add("single");
            add("single");
            add("single");
            add("double");
            add("double");
        }};
        mView.setBitmaps(strings);
        ArrayList<String> strings1 = new ArrayList<String>() {{
            add("rgb");
            add("single");
            add("single");
            add("single");
        }};
        mView1.setBitmaps(strings1);
        ArrayList<String> strings2 = new ArrayList<String>() {{
            add("rgb");
        }};
        mView2.setBitmaps(strings2);
    }
}
