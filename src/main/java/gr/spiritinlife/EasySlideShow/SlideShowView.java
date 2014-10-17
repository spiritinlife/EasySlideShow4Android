package gr.spiritinlife.EasySlideShow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by spiritinlife on 17/10/2014.
 */
public class SlideShowView extends RelativeLayout implements TargetLoaded {


    private static final int MAX_IMAGES = 3;
    private Context context;

    private String[] urls;


    //CANVAS
    private static final int radius = 10;
    private static final int selectorsMargin = 5;
    private int CanvasWidth;
    private int CanvasHeight;
    private Paint grayPaint,whitePaint,blackPaint;





    private int curPos = 0;
    private ImageView curImageView;
    private TargetImageView nextImageView;


    private boolean Waiting = true;
    private int counter = 0;

    public SlideShowView(Context context,String[] urls) {
        this(context, null, 0);
    }

    public SlideShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWillNotDraw(false);
        nextImageView = new TargetImageView(this);
        this.context = context;
        this.grayPaint = new Paint();
        this.grayPaint.setColor(Color.GRAY);
        this.grayPaint.setStyle(Paint.Style.FILL);
        this.whitePaint = new Paint();
        this.whitePaint.setColor(Color.WHITE);
        this.whitePaint.setStyle(Paint.Style.FILL);

        //we need to set that, because it will be edgy and ugly otherwise
        this.grayPaint.setAntiAlias(true);
        this.whitePaint.setAntiAlias(true);


        this.blackPaint = new Paint();
        this.blackPaint.setColor(Color.BLACK);

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gdt.onTouchEvent(motionEvent);
            }
        });

    }


    private final GestureDetector gdt = new GestureDetector(new GestureListener());

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private final int SWIPE_MIN_DISTANCE = 70;
        private final int SWIPE_THRESHOLD_VELOCITY = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                // Right to left, your code here
                next();
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) >  SWIPE_THRESHOLD_VELOCITY) {
                // Left to right, your code here
                prev();
                return true;
            }
            return false;
        }
    }


    private void Initialize() {
     /*   for (int i = 3; i < 0; i++) {
            getImageIntoView(i); //cache them in picasso
        }*/
        getImageIntoView(0);
    }


    public void start(String[] urls)
    {
        Picasso.with(context).setLoggingEnabled(true);
        this.urls = urls;
        this.curImageView = newImageViewInstance();
        addView(curImageView);
        Initialize();
    }



    /**
     * Create the ImageView that will be used to show the bitmap.
     *
     * @return A new ImageView instance
     */
    private ImageView newImageViewInstance() {
        ImageView iv = new ImageView(context);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // SlideShowView is a subclass of RelativeLayout. Set the layout parameters accordingly
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        iv.setLayoutParams(lp);

        return iv;
    }

    private void getImageIntoView(int position) {
        Picasso.with(context).load(urls[position]).into(nextImageView);

    }



    protected void next()
    {
        Waiting = true;
        curPos++;
        if (curPos > MAX_IMAGES-1)
            curPos = 0;
        getImageIntoView(curPos);
        invalidate();
    }


    protected void prev()
    {
        Waiting = true;
        curPos--;
        if (curPos < 0)
            curPos = MAX_IMAGES-1;
        getImageIntoView(curPos);
        invalidate();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.CanvasWidth = w;
        this.CanvasHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }


    /**
     * We use dispatchDraw in order to draw like childs
     * or else the dots would be hidden by the imageView
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        //clear canvas
        canvas.drawColor(Color.WHITE);
        //super draws the image view
        super.dispatchDraw(canvas);
        //and we add the dots
        int y = CanvasHeight - 2*radius - selectorsMargin;

        for (int i = MAX_IMAGES-1; i >=0 ; i--) {
            canvas.drawCircle(CanvasWidth / 2 + 2 * (i - 1) * radius + i * selectorsMargin, y, radius, (curPos == i) ? grayPaint : whitePaint); //i think (i-1) works because we dont want to count the dot in the middle
        }


        /**
         * todo a generic way to add animations not from canvas
         */
        if (Waiting)
        {
            canvas.drawLine(0, 7*counter, CanvasWidth, 7*counter, blackPaint);
            canvas.drawLine(0, 6*counter, CanvasWidth, 6*counter, blackPaint);
            canvas.drawLine(0, 5*counter, CanvasWidth, 5*counter, blackPaint);
            counter += 2;
            postInvalidate();
        }
        else
            counter = 0;
    }

    @Override
    public void onTargetLoaded(Bitmap bitmap) {
        curImageView.setImageBitmap(bitmap);
        Waiting = false;
        invalidate();

    }



    public class TargetImageView implements Target {

        TargetLoaded targetLoaded;

        public TargetImageView(TargetLoaded targetLoaded)
        {
            this.targetLoaded = targetLoaded;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            targetLoaded.onTargetLoaded(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };


}

