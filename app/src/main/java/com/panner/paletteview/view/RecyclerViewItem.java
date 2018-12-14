package com.panner.paletteview.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.panner.paletteview.R;

import java.util.ArrayList;


/**
 * @author panzhijie
 * @version 2018-12-14-13:41
 */

public class RecyclerViewItem extends View {
    private Context mContext;
    private ArrayList<Bitmap> mBitmaps;
    private Paint mBgPaint;
    private int widthSize;
    private int heightSize;
    private Paint mWhiteBg;

    public RecyclerViewItem(Context context) {
        this(context, null);
    }

    public RecyclerViewItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mBitmaps = new ArrayList<>();
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RecyclerViewItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mBitmaps = new ArrayList<>();
        init();
    }

    public void init() {
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setDither(true);
        mBgPaint.setColor(getResources().getColor(R.color.color_21));

        mWhiteBg = new Paint();
        mWhiteBg.setAntiAlias(true);
        mWhiteBg.setDither(true);
        mWhiteBg.setColor(getResources().getColor(R.color.color_ff));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(widthSize / 2, widthSize / 2, widthSize / 2, mBgPaint);
        // 绘制图片
        for (int i = 0; i < mBitmaps.size(); i++) {
            if (mBitmaps.size() == 1) {
                Rect src = new Rect(0, 0, mBitmaps.get(i).getWidth(), mBitmaps.get(i).getHeight());
                canvas.drawCircle(widthSize / 2, widthSize / 2, widthSize / 2, mWhiteBg);
                canvas.drawBitmap(mBitmaps.get(i), null, src, null);
            } else if (mBitmaps.size() <= 4) {
                Rect src;
                if (i > 1) {
                    src = new Rect(mBitmaps.get(i).getWidth() / 2 * (i - 2) + dp2px(mContext, 10), mBitmaps.get(i).getHeight() + dp2px(mContext, 10),
                            mBitmaps.get(i).getWidth() * (i - 1) + dp2px(mContext, 10), mBitmaps.get(i).getHeight() * 2 + dp2px(mContext, 10));
                    canvas.drawCircle(mBitmaps.get(i).getWidth() / 2 * (i - 2) + dp2px(mContext, 10) + mBitmaps.get(i).getWidth() / 2,
                            mBitmaps.get(i).getHeight() + dp2px(mContext, 10) + mBitmaps.get(i).getHeight() / 2,
                            mBitmaps.get(i).getHeight() / 2 - dp2px(mContext, 2), mWhiteBg);
                } else {
                    src = new Rect(mBitmaps.get(i).getWidth() * i + dp2px(mContext, 10), dp2px(mContext, 10),
                            mBitmaps.get(i).getWidth() * (i + 1) + dp2px(mContext, 10), mBitmaps.get(i).getHeight() + dp2px(mContext, 10));
                    canvas.drawCircle(mBitmaps.get(i).getWidth() * i + dp2px(mContext, 10) + mBitmaps.get(i).getWidth() / 2,
                            dp2px(mContext, 10) + mBitmaps.get(i).getHeight() / 2,
                            mBitmaps.get(i).getHeight() / 2-dp2px(mContext,2), mWhiteBg);
                }
                canvas.drawBitmap(mBitmaps.get(i), null, src, null);
            } else if (mBitmaps.size() <= 6) {
                Rect src;
                if (i > 2) {
                    src = new Rect(mBitmaps.get(i).getWidth() * (i - 3) + dp2px(mContext, 5), mBitmaps.get(i).getHeight() + dp2px(mContext, 20),
                            mBitmaps.get(i).getWidth() * (i - 2) + dp2px(mContext, 5), mBitmaps.get(i).getHeight() * 2 + dp2px(mContext, 20));
                    canvas.drawCircle(mBitmaps.get(i).getWidth() * (i - 3) + dp2px(mContext, 5) + mBitmaps.get(i).getWidth() / 2,
                            mBitmaps.get(i).getHeight() + dp2px(mContext, 20) + mBitmaps.get(i).getHeight() / 2,
                            mBitmaps.get(i).getHeight() / 2-dp2px(mContext,2), mWhiteBg);
                } else {
                    src = new Rect(mBitmaps.get(i).getWidth() * i + dp2px(mContext, 5), dp2px(mContext, 20),
                            mBitmaps.get(i).getWidth() * (i + 1) + dp2px(mContext, 5), mBitmaps.get(i).getHeight() + dp2px(mContext, 20));
                    canvas.drawCircle(mBitmaps.get(i).getWidth() * i + dp2px(mContext, 5) + mBitmaps.get(i).getWidth() / 2,
                            dp2px(mContext, 20) + mBitmaps.get(i).getHeight() / 2,
                            mBitmaps.get(i).getHeight() / 2-dp2px(mContext,2), mWhiteBg);
                }
                canvas.drawBitmap(mBitmaps.get(i), null, src, null);
            } else {
                Rect src = new Rect(mBitmaps.get(i).getWidth() * i, 0, mBitmaps.get(i).getWidth() * (i + 1), mBitmaps.get(i).getHeight());
                canvas.drawBitmap(mBitmaps.get(i), null, src, null);
            }
        }

    }

    private int mChildWidth = 30;
    private int mChildHeight = 30;

    public void setBitmaps(ArrayList<String> list) {
        int size = list.size();
        if (size == 1) {
            mChildWidth = 100;
            mChildHeight = 100;
        } else if (size <= 4) {
            mChildWidth = 40;
            mChildHeight = 40;
        } else {
            mChildWidth = 30;
            mChildHeight = 30;
        }
        for (int i = 0; i < size; i++) {
            Bitmap bitmap;
            if ("rgb".equals(list.get(i))) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.rgb);
            } else if ("single".equals(list.get(i))) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.single);
            } else if ("yw".equals(list.get(i))) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.double_light);
            } else {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.rgb);
            }
            // 获得图片的宽高
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            // 设置想要的大小
            int newWidth = dp2px(mContext, mChildWidth);
            int newHeight = dp2px(mContext, mChildHeight);
            // 计算缩放比例
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // 得到新的图片
            Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
                    true);
            mBitmaps.add(newbm);
        }
        invalidate();
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
