

Easy Slide Show 4 Android<br>
Still in <b>progress</b>

A small library ( it is actually one file ) that you can basically copy and paste it in your project.
It uses <b>picasso</b> to download images from urls and cache them.
It handles right to left and left to right gestures by default with changing to next or previous image respectively.
It uses dots to indicate which image you are using.


Better explained with a ScreenShot
<a href="http://tinypic.com?ref=15zfm8j" target="_blank"><img src="http://i61.tinypic.com/15zfm8j.png" border="0" alt="Image and video hosting by TinyPic"></a>


How-to
<br>
First of all you need picasso in your project <br>
   -> you can get picasso here <a href="https://github.com/square/picasso">picasso</a><br>
Then :<br>

##Changes


1) Add it in your layout 
```java
    <gr.spiritinlife.EasySlideShow.SlideShowView
             android:id="@+id/slideShow"
             android:layout_width="match_parent"
             android:layout_height="170dp"
             android:scaleType="centerCrop"
             slideshow:dotNormal="#626283"
             slideshow:dotSelected="#62aa83"
             slideshow:dotRadius="5dp"
             slideshow:dotMargin="2dp"/>
```
        
2) Get it from code
```java
    slideShow = (SlideShowView) view.findViewById(R.id.slideShow);
```

3) Start  it passing it an array of urls
```java
    slideShow.start(urls);
```


Example activity

```

public class EasySlideShowActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.easyslideshow_activity);

    SlideShowView slideShow  = (SlideShowView) findViewById(R.id.slideShow);
    slideShow.start(new String[] {
        "http://7-themes.com/data_images/out/42/6914793-tropical-beach-images.jpg",
        "http://p1.pichost.me/i/51/1749517.jpg",
        "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcR4AxN9QLjASziCAjb0Imt5uoWhJrNBytYzM4JyQ8JLiHG4fl2W",
        "https://pbs.twimg.com/profile_images/587778453937618944/-z_faB4f.jpg",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR9Oonh8RodI9wTlONYORc7AbYoDRLkxnov5gsPamFPhvq2pAhd"
    });
  }
}


```


And the Example layout

```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
                xmlns:slideshow="http://schemas.android.com/apk/res-auto"
                android:orientation="vertical"
                android:layout_width="match_parent"
                android:layout_height="match_parent">


    <gr.spiritinlife.EasySlideShow.SlideShowView
            android:id="@+id/slideShow"
            android:layout_width="match_parent"
            android:layout_height="170dp"
            android:scaleType="centerCrop"
            slideshow:dotNormal="#626283"
            slideshow:dotSelected="#62aa83"
            slideshow:dotRadius="5dp"
            slideshow:dotMargin="2dp"
            android:layout_centerInParent="true"/>


</RelativeLayout>

```


## Changes

- Remove relativelayout and use just a single imageview
- Various optimization
- Make it more configurable