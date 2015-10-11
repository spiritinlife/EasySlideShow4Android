package gr.spiritinlife.EasySlideShow;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import gr.spiritinlife.EasySlideShow.R;

public class SlideShowView extends ImageView implements TargetLoaded {


  private String[] urls;

  private int curPos = 0;

  private int imagesSize = 0;

  private GestureDetector gdt;

  private TargetImageView nextImageView;

  private Dot[] dots;

  //CANVAS
  private int radius = 10;

  private int dotMargin = 5;

  private int dotYPos = -1;


  private Paint selectedPaint;

  private Paint normalPaint;


  public SlideShowView(Context context) {
    super(context);
    init(context, null);
  }


  public SlideShowView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public SlideShowView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public SlideShowView(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context, attrs);
  }


  private void init(Context context, AttributeSet attrs) {
    gdt = new GestureDetector(context, new GestureListener());
    nextImageView = new TargetImageView(this);
    setClickable(true);

    this.normalPaint = new Paint();
    this.selectedPaint = new Paint();
    this.normalPaint.setStyle(Paint.Style.FILL);
    this.selectedPaint.setStyle(Paint.Style.FILL);

    int dotNormalColor = Color.WHITE;
    int dotSelectedColor = Color.GRAY;

    TypedArray a = context.getTheme().obtainStyledAttributes(
        attrs,
        R.styleable.SlideShow,
        0, 0);

    try {
      dotNormalColor = a.getColor(R.styleable.SlideShow_dotNormal, Color.WHITE);
      dotSelectedColor = a.getColor(R.styleable.SlideShow_dotSelected, Color.GRAY);
      radius = a.getDimensionPixelSize(R.styleable.SlideShow_dotRadius, 10);
      dotMargin = a.getDimensionPixelSize(R.styleable.SlideShow_dotMargin, 5);
    } finally {
      a.recycle();
    }

    this.selectedPaint.setColor(dotSelectedColor);
    this.normalPaint.setColor(dotNormalColor);

    this.setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        return gdt.onTouchEvent(event);
      }
    });
  }

  public void start(String[] urls) {
    this.urls = urls;
    this.imagesSize = this.urls.length;

    // setup dots
    dots = new Dot[imagesSize];
    int dotY = 2 * radius - dotMargin;
    int dotWidth = 2 * radius + dotMargin;
    for (int i = 0; i < dots.length; i++) {
      dots[i] = new Dot((imagesSize / 2 - i) * dotWidth, dotY, radius);
    }

    getImageIntoView(0);
  }


  private void getImageIntoView(int position) {
    Picasso.with(getContext()).load(urls[position]).into(nextImageView);
  }


  protected void next() {
    curPos++;
    if (curPos > imagesSize - 1) {
      curPos = 0;
    }
    getImageIntoView(curPos);
  }


  protected void prev() {
    curPos--;
    if (curPos < 0) {
      curPos = imagesSize - 1;
    }
    getImageIntoView(curPos);
  }

  @Override
  public void onTargetLoaded(Bitmap bitmap) {
    setImageBitmap(bitmap);
    invalidate();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    if (dots == null) {
      return;
    }

    int offsetX = canvas.getWidth() / 2;
    int offsetY = canvas.getHeight();
    for (int i = 0; i < dots.length; i++) {
      dots[i].render(canvas, offsetX, offsetY, (curPos == i) ? selectedPaint : normalPaint);
    }

  }

  private class Dot {

    private int posX, posY, radius;

    public Dot() {

    }

    public Dot(int posX, int posY, int radius) {
      this.posX = posX;
      this.posY = posY;
      this.radius = radius;
    }

    public void render(Canvas canvas, int offsetX, int offsetY, Paint paint) {
      canvas
          .drawCircle(offsetX - posX, offsetY - posY, radius, paint);
    }
  }

  private class GestureListener extends GestureDetector.SimpleOnGestureListener {

    private final int SWIPE_MIN_DISTANCE = 50;

    private final int SWIPE_THRESHOLD_VELOCITY = 70;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
      if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
          && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
        // Right to left, your code here
        next();
        return true;
      } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
          && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
        // Left to right, your code here
        prev();
        return true;
      }
      return false;
    }
  }


  public class TargetImageView implements Target {

    TargetLoaded targetLoaded;

    public TargetImageView(TargetLoaded targetLoaded) {
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
  }


}
