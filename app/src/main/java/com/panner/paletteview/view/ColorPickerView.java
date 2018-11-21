package com.panner.paletteview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import com.panner.paletteview.R;

/**
 * 取色盘（RGB)
 *
 * @author panzhijie
 * @version 2018-11-7 11:00
 */
public class ColorPickerView extends View {

    private Context mContext;
    private Paint mPickerViewPaint;//色盘画笔
    private Paint mTouchViewPaint;//拖动圆画笔
    private Bitmap mPickerView;
    //拖动圆的属性
    private int mTouchWidth;
    private int mTouchColor;
    private Bitmap mTouchBitmap;
    //色盘大小
    private int mPickerViewWidth;
    private int mTouchResId;

    private int mCenterX;
    private int mCenterY;
    private Rect mPickerViewRect;
    private float[] mPickerHsv = {0f, 1f, 1f};
    private int mTouchCircleY;
    private int mTouchCircleX;

    public ColorPickerView(Context context) {
        this(context, null);
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
        initAttr(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ColorPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 初始化操作，画笔
     */
    public void init() {
        //色盘画笔
        mPickerViewPaint = new Paint();
        mPickerViewPaint.setAntiAlias(true);//消除锯齿
        mPickerViewPaint.setDither(true);//防抖动

        //拖动圆的画笔
        mTouchViewPaint = new Paint();
        mTouchViewPaint.setAntiAlias(true);
        mTouchViewPaint.setDither(true);
        mTouchViewPaint.setStrokeWidth(5);//设置拖动圆的大小
        mTouchViewPaint.setColor(Color.parseColor("#FFFFFFFF"));
    }

    /**
     * 初始化属性
     *
     * @param attrs
     */
    private void initAttr(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.ColorPickerView);
        mTouchWidth = typedArray.getInt(R.styleable.ColorPickerView_indicator_width, 5);
        mTouchColor = typedArray.getColor(R.styleable.ColorPickerView_indicator_color,
                mContext.getResources().getColor(R.color.colorAccent));
        mPickerViewWidth = typedArray.getInt(R.styleable.ColorPickerView_picker_width, 800);
        mTouchResId = typedArray.getResourceId(R.styleable.ColorPickerView_indicator_icon, 0);
//        if(mTouchResId==0) {
//            //未指定指示器的图标采用默认的绘制一个小圆形
//            indictorRadius = (float) (mTouchWidth * 0.5);
//        }else {
//            // 将背景图片大小设置为属性设置的直径
//            mTouchBitmap = BitmapFactory.decodeResource(getResources(), mTouchResId );
//            mTouchBitmap= Bitmap.createScaledBitmap(mTouchBitmap,mTouchBitmap.getWidth(),mTouchBitmap.getHeight(),true);
//            mTouchWidth=mTouchBitmap.getHeight();
//        }

        mTouchCircleX = mTouchCircleY = mPickerViewWidth / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mPickerView, mPickerViewRect.left, mPickerViewRect.top, null);
        canvas.drawCircle(mTouchCircleX, mTouchCircleY, 30, mTouchViewPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = mPickerViewWidth / 2;
        mCenterY = mPickerViewWidth / 2;

        mPickerViewRect = new Rect(0, 0, mCenterX + mTouchWidth, mCenterY + mTouchWidth);
        mPickerView = createPickerView();

    }

    private Bitmap createPickerView() {
        Bitmap bitmap = Bitmap.createBitmap(mPickerViewWidth, mPickerViewWidth, Bitmap.Config.ARGB_8888);
        int colorCount = 12;
        int colorAngleStep = 360 / 12;
        int colors[] = new int[colorCount];
        float hsv[] = new float[]{0f, 1f, 1f};
        for (int i = 0; i < colors.length; i++) {
            hsv[0] = (i * colorAngleStep + 180) % 360;
            colors[i] = Color.HSVToColor(hsv);
        }

        SweepGradient sweepGradient = new SweepGradient(mCenterX, mCenterY, colors, null);
        RadialGradient radialGradient = new RadialGradient(mCenterX, mCenterY, mPickerViewWidth / 2,
                0xFFFFFFFF, 0x00FFFFFF, Shader.TileMode.CLAMP);
        ComposeShader composeShader = new ComposeShader(sweepGradient, radialGradient, PorterDuff.Mode.SRC_OVER);
        mPickerViewPaint.setShader(composeShader);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(mPickerViewWidth / 2, mPickerViewWidth / 2, mPickerViewWidth / 2, mPickerViewPaint);
        return bitmap;
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
                    int cy = y - mCenterY;
                    double d = Math.sqrt(cx * cx + cy * cy);
                    if (d <= mPickerViewWidth / 2) {
                        mPickerHsv[0] = (float) (Math.toDegrees(Math.atan2(cy, cx)) + 180f);
                        mPickerHsv[1] = Math.max(0f, Math.min(1f, (float) (d / mPickerViewWidth / 2)));
                        if (mPickerViewListener != null) {
                            mTouchCircleY = y;
                            mTouchCircleX = x;
                            mPickerViewListener.onColorPicker(getColor());
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

    public int getColor() {
        return Color.HSVToColor(mPickerHsv);
    }

    /**
     * 设置拖动圆的大小
     *
     * @param touchWidth
     */
    public void setTouchWidth(int touchWidth) {
        this.mTouchWidth = touchWidth;
    }

    /**
     * 设置拖动圆的颜色
     *
     * @param color
     */
    public void setTouchColor(int color) {
        mTouchViewPaint.setColor(color);
    }

    /**
     * 设置色盘大小
     *
     * @param radius
     */
    public void setPickerViewWidth(int radius) {
        this.mPickerViewWidth = radius;
    }

    private PickerViewListener mPickerViewListener;

    public void setOnColorPickListener(PickerViewListener listener) {
        this.mPickerViewListener = listener;
    }

    public interface PickerViewListener {
        void onColorPicker(int color);
    }
}
