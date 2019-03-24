# Android hatch-drawable library
###### Helps to simply add hatch to you app (only for square views)

Written in Kotlin with @JvmOverloads constructor

```
    @JvmOverloads constructor(
        private val lineWidth: Float = 1f,
        private val lineSpacing: Float = 4f,
        private val lineColor: Int = Color.BLACK,
        degree: Float = 225f
    ) 
```

Only thing you need to do - set correct vales to constructor and set this drawable as background, foreground or either
something for your View. 

```
    val drawable = HatchDrawable(3, 6, Color.BLACK, 30)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        hatch.background = drawable
    else {
        @Suppress("DEPRECATION")
        hatch.setBackgroundDrawable(drawable)
    }
```

Demo app attached. Some screenshots below:

![1](https://github.com/iamthevoid/android-hatch-drawable/blob/master/screenshot/1.png)  ![2](https://github.com/iamthevoid/android-hatch-drawable/blob/master/screenshot/2.png)  ![3](https://github.com/iamthevoid/android-hatch-drawable/blob/master/screenshot/3.png)