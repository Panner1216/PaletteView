package com.panner.paletteview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
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

public class RectanglePickerView extends View {

    private Paint mPickerPaint;//背景图片画笔
    private Paint mTouchViewPaint;
    private float mTouchViewRadius;
    private int mTouchViewColor;

    private float mPickerViewWidth;
    private int mPickerViewHeight;//去色盘的宽高
    private Bitmap mPickerView;
    private int mCenterX;
    private int mCenterY;
    private float[] mPickerHsv = {0f, 1f, 1f};
    private int mTouchCircleY;
    private int mTouchCircleX;
    private Paint mBigTouchCircle;

    public RectanglePickerView(Context context) {
        this(context, null);
    }

    public RectanglePickerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectanglePickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RectanglePickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        mPickerViewWidth = typedArray.getDimension(R.styleable.RectanglePickerView_picker_width_rect, AppUtils.getScreenWidth(context));
        mPickerViewHeight = (int) typedArray.getDimension(R.styleable.RectanglePickerView_picker_height_rect, 600);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mPickerView, 0, 0, null);
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
        mCenterX = mTouchCircleX = (int) (mPickerViewWidth / 2);
        mCenterY = mTouchCircleY = mPickerViewHeight / 2;
        mPickerView = createPickerViewBitmap();
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
                    int cx = x - mCenterX;

                    if (cx <= mPickerViewWidth && y <= mPickerViewHeight) {
                        mPickerHsv[0] = 360f - 360f / mPickerViewWidth * x;
                        mPickerHsv[1] = y / (float) mPickerViewHeight;

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
     * 将hsv转换为color
     *
     * @return
     */
    public int getColor() {
        return Color.HSVToColor(mPickerHsv);
    }

    /**
     * 创建背景图片
     * 将宽度分为13份，每30度为一份，渐变渲染
     * 将hsv转为rgb渲染图片，h->色彩  s->深浅，0-1之间的值，越小越白  v->明暗，图片的亮暗，0-1之间，越小越暗
     *
     * @return
     */
    public Bitmap createPickerViewBitmap() {
        Bitmap bitmap = Bitmap.createBitmap((int) mPickerViewWidth, mPickerViewHeight, Bitmap.Config.ARGB_8888);
        int colorCount = 6;
        int colorAngleStep = 360 / 6;
        int colors[] = new int[colorCount + 1];
        float hsv[] = new float[]{0f, 1f, 1f};
        for (int i = 0; i < colors.length; i++) {
            hsv[0] = 360 - (i * colorAngleStep) % 360;
            colors[i] = Color.HSVToColor(hsv);
        }
        LinearGradient linearGradient = new LinearGradient(0, 0, mPickerViewWidth, 0,
                colors, null, Shader.TileMode.CLAMP);
        LinearGradient gradient = new LinearGradient(0, 0, 0, mPickerViewHeight,
                new int[]{Color.WHITE, Color.TRANSPARENT}, null, Shader.TileMode.CLAMP);
        ComposeShader composeShader = new ComposeShader(linearGradient, gradient, PorterDuff.Mode.SRC_OVER);
        mPickerPaint.setShader(composeShader);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, mPickerViewWidth, mPickerViewHeight, mPickerPaint);
        return bitmap;
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
}
