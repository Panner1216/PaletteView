package com.panner.paletteview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import com.panner.paletteview.R;
import com.panner.paletteview.listener.PickerViewListener;
import com.panner.paletteview.utils.AppUtils;

/**
 * @author panzhijie
 * @version 2018-11-23-09:43
 */

public class DoubleColorPickerView extends View {

    private Paint mPickerPaint;//背景图片画笔
    private Paint mTouchViewPaint;
    private float mTouchViewRadius;
    private int mTouchViewColor;

    private int mPickerViewWidth;
    private int mPickerViewHeight;//取色盘的宽高
    private int mTouchCircleY;
    private int mTouchCircleX;
    private Paint mBigTouchCircle;
    private int mStartColor = 0xFFFAAC24, mEndColor = 0XFFFFFFFF;

    public DoubleColorPickerView(Context context) {
        this(context, null);
    }

    public DoubleColorPickerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleColorPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DoubleColorPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * 初始化画笔
     */
    public void init() {
        mPickerPaint = new Paint();
        mPickerPaint.setAntiAlias(true);//消除锯齿
        mPickerPaint.setDither(true);//防抖动
        mPickerPaint.setStrokeWidth(2);//描边

        mTouchViewPaint = new Paint();
        mTouchViewPaint.setAntiAlias(true);
        mTouchViewPaint.setDither(true);
        mTouchViewPaint.setStrokeWidth(5);
        mTouchViewPaint.setColor(mTouchViewColor);
        //拖动圆的边框
        mBigTouchCircle = new Paint();
        mBigTouchCircle.setAntiAlias(true);
        mBigTouchCircle.setDither(true);
        mBigTouchCircle.setColor(getResources().getColor(R.color.color_cc));
    }


    /**
     * 初始化属性值
     *
     * @param context
     * @param attrs
     */
    public void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RectanglePickerView);
        mTouchViewRadius = typedArray.getDimension(R.styleable.RectanglePickerView_indicator_width_rect, 25);
        mTouchViewColor = typedArray.getColor(R.styleable.RectanglePickerView_indicator_color_rect,
                context.getResources().getColor(R.color.color_ff));
        mPickerViewWidth = (int) typedArray.getDimension(R.styleable.RectanglePickerView_picker_width_rect, AppUtils.getScreenWidth(context));
        mPickerViewHeight = (int) typedArray.getDimension(R.styleable.RectanglePickerView_picker_height_rect, 600);
    }

    /**
     * 画色盘的时候传入的颜色数组为想要的两种变化颜色
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LinearGradient linearGradient = new LinearGradient(0, 0, mPickerViewWidth, 0, new int[]{mStartColor, mEndColor}, null, LinearGradient.TileMode.CLAMP);
        mPickerPaint.setShader(linearGradient);
        canvas.drawRect(0, 0, mPickerViewWidth, mPickerViewHeight, mPickerPaint);
        canvas.drawCircle(mTouchCircleX, mTouchCircleY, mTouchViewRadius + 4, mBigTouchCircle);
        canvas.drawCircle(mTouchCircleX, mTouchCircleY, mTouchViewRadius, mTouchViewPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int minimumWidth = getSuggestedMinimumWidth();
        final int minimumHeight = getSuggestedMinimumHeight();
        int width = measureWidth(minimumWidth, widthMeasureSpec);
        int height = measureHeight(minimumHeight, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int defaultWidth, int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.AT_MOST:
                defaultWidth = (int) (mPickerViewWidth + getPaddingLeft() + getPaddingRight());
                break;
            case MeasureSpec.EXACTLY:
                defaultWidth = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                defaultWidth = Math.max(defaultWidth, specSize);
        }
        return defaultWidth;
    }

    private int measureHeight(int defaultHeight, int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.AT_MOST:
                defaultHeight = mPickerViewHeight + getPaddingTop() + getPaddingBottom();
                break;
            case MeasureSpec.EXACTLY:
                defaultHeight = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                defaultHeight = Math.max(defaultHeight, specSize);
                break;
        }
        return defaultHeight;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTouchCircleX = mPickerViewWidth / 2;
        mTouchCircleY = mPickerViewHeight / 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ViewParent parent = getParent();
        if (parent != null) {
            //父控件不拦截事件，全部交给子控件处理
            parent.requestDisallowInterceptTouchEvent(true);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    //处理越界问题
                    if (y > mPickerViewHeight) {
                        y = mPickerViewHeight;
                    }
                    if (x > mPickerViewWidth) {
                        x = mPickerViewWidth;
                    }
                    if (x <= mPickerViewWidth && y <= mPickerViewHeight && x >= 0 && y >= 0) {

                        if (mPickerViewListener != null) {
                            mTouchCircleY = y;
                            mTouchCircleX = x;
                            mPickerViewListener.onPickerColor(getColor());
                            mTouchViewColor = getColor();
                            mTouchViewPaint.setColor(mTouchViewColor);
                            postInvalidate();
                        }
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
        }
        return true;

    }

    private PickerViewListener mPickerViewListener;

    /**
     * 获取颜色的回调
     *
     * @param listener
     */
    public void setOnColorPickerListener(PickerViewListener listener) {
        this.mPickerViewListener = listener;
    }

    /**
     * 渐变开始的颜色值
     *
     * @param color 传入十六进制的颜色值如0X000000
     */
    public void setStartColor(int color) {
        this.mStartColor = color;

    }

    /**
     * 渐变结束的颜色值
     *
     * @param color 传入十六进制的颜色值如0XFFFFFF
     */
    public void setEndColor(int color) {
        this.mEndColor = color;

    }

    /**
     * 将hsv转换为color
     *
     * @return
     */
    public int getColor() {
        return Color.parseColor("#FFFF" + addZeroForNumLeft(Integer.toHexString((int) (mTouchCircleX * (255f / mPickerViewWidth))), 2));
    }

    /**
     * 设置色盘宽度
     *
     * @param width
     */
    public void setWidth(int width) {
        this.mPickerViewWidth = width;
    }

    /**
     * 设置色盘高度
     *
     * @param height
     */
    public void setHeight(int height) {
        this.mPickerViewHeight = height;
    }

    /**
     * 设置拖拽圆的颜色
     *
     * @param color
     */
    public void setTouchViewColor(@ColorInt int color) {
        this.mTouchViewColor = color;
    }

    /**
     * 设置拖拽圆的半径
     *
     * @param radius
     */
    public void setTouchViewRadius(int radius) {
        this.mTouchViewRadius = radius;
    }

    /**
     * 将传入的字符串转换为指定位数，不足的左补零
     *
     * @param str
     * @param strLength
     * @return
     */
    public static String addZeroForNumLeft(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);// 左补0
                str = sb.toString();
                strLen = str.length();
            }
        }

        return str;
    }
}
