

Easy Slide Show 4 Android<br>
Still in <b>progress</b>

A small library ( it is actually one file ) that you can basically copy and paste it in your project.
It uses <b>picasso</b> to download images from urls and cache them.
It handles right to left and left to right gestures by default with changing to next or previous image respectively.
It uses dots to indicate which image you are using.


Better explained with a ScreenShot
<a href="http://tinypic.com?ref=15zfm8j" target="_blank"><img src="http://i61.tinypic.com/15zfm8j.png" border="0" alt="Image and video hosting by TinyPic"></a>


How-to
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!!!!First of all you need picasso in your project!!!!-> you can get picasso here< href="https://github.com/square/picasso">picasso</a>
Then :

1) Add it in your layout 
```java
    <gr.spiritinlife.EasySlideShow.SlideShowView
        android:id="@+id/slideShow"
        android:layout_width="match_parent"
        android:clickable="true"
        android:layout_height="170dp"
        android:layout_marginBottom="7dp"
        android:minHeight="200dp"/>
```
        
2) Get it from code
```java
    slideShow = (SlideShowView) view.findViewById(R.id.slideShow);
```

3) Start  it passing it an array of urls
```java
    slideShow.start(urls);
```


To-Do <br>
1) Add examples <br>
2) Add a generic way to make animation between imageview trasnistion and remove the current one which depeneds on canvas <br>
3) Give a public function for changing the MAX_IMAGES which is now 3 ( it can be changed by code if you like ) <br>
4) Make it possible to add custom colors on dots without changing the library. <br>
