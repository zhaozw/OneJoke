package com.smarter.onejoke.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.smarter.onejoke.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by panl on 15/5/3.
 */
public class GifView extends ImageView implements View.OnClickListener {
    /**
     *  播放gif动画的关键类
     */
    private Movie movie;
    /**
     * 开始播放按钮图片
     */
    private Bitmap startBtn;
    /**
     * 记录动画开始时间
     */
    private long movieStart;
    /**
     * gif图片宽度
     */
    private int gifWidth;
    /**
     * gif图片高度
     */
    private int gifHegiht;
    /**
     * 图片是否正在播放
     */
    private boolean isPlaying;
    /**
     * 是否自动播放
     */
    private boolean isAutoPlay;

    public GifView(Context context) {
        this(context, null);
    }

    public GifView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * PowerImageView构造函数，在这里完成所有必要的初始化操作。
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public GifView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.GifView,0,0);
        Drawable drawable = getDrawable();
        Bitmap bm = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bm);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());             // 使用Movie类对流进行解码
            movie = Movie.decodeStream(is);
            if (movie != null) {
                // 如果返回值不等于null，就说明这是一个GIF图片，下面获取是否自动播放的属性
                isAutoPlay = a.getBoolean(R.styleable.GifView_auto_play, false);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                gifWidth = bitmap.getWidth();
                gifHegiht = bitmap.getHeight();
                bitmap.recycle();
                if (!isAutoPlay) {
                    // 当不允许自动播放的时候，得到开始播放按钮的图片，并注册点击事件
                    startBtn = BitmapFactory.decodeResource(getResources(),
                            R.drawable.start_play);
                    setOnClickListener(this);
                }
            }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (movie == null) {
            // mMovie等于null，说明是张普通的图片，则直接调用父类的onDraw()方法
            super.onDraw(canvas);
        } else {
            // mMovie不等于null，说明是张GIF图片
            if (isAutoPlay) {
                // 如果允许自动播放，就调用playMovie()方法播放GIF动画
                playMovie(canvas);
                invalidate();
            } else {
                // 不允许自动播放时，判断当前图片是否正在播放
                if (isPlaying) {
                    // 正在播放就继续调用playMovie()方法，一直到动画播放结束为止
                    if (playMovie(canvas)) {
                        isPlaying = false;
                    }
                    invalidate();
                } else {
                    // 还没开始播放就只绘制GIF图片的第一帧，并绘制一个开始按钮
                    movie.setTime(0);
                    movie.draw(canvas, 0, 0);
                    int offsetW = (gifWidth - startBtn.getWidth()) / 2;
                    int offsetH = (gifHegiht - startBtn.getHeight()) / 2;
                    canvas.drawBitmap(startBtn, offsetW, offsetH, null);
                }
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (movie != null) {
            // 如果是GIF图片则重写设定PowerImageView的大小
            setMeasuredDimension(gifWidth, gifHegiht);
        }
    }

    @Override
    public void onClick(View view) {

    }
    /**
     * 开始播放GIF动画，播放完成返回true，未完成返回false。
     *
     * @param canvas
     * @return 播放完成返回true，未完成返回false。
     */
    private boolean playMovie(Canvas canvas) {
        long now = SystemClock.uptimeMillis();
        if (movieStart == 0) {
            movieStart = now;
        }
        int duration = movie.duration();
        if (duration == 0) {
            duration = 1000;
        }
        int relTime = (int) ((now - movieStart) % duration);
        movie.setTime(relTime);
        movie.draw(canvas, 0, 0);
        if ((now - movieStart) >= duration) {
            movieStart = 0;
            return true;
        }
        return false;
    }

}
