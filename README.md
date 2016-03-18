ViewSticker
===========

ViewSticker is Android Library

<img src="https://github.com/sys1yagi/ViewSticker/blob/master/moon.gif?raw=true" width="250px"/>

## Usage

### layout.xml

require

* FrameLayout
* ScrollView

```
<FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
  <ScrollView
      android:id="@+id/scroll_view"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      >
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        >
      <TextView
          android:id="@+id/description_header"
          android:layout_width="match_parent"
          android:layout_height="wrap_content"
          />
    </LinearLayout>
  </ScrollView>
</FrameLayout>

```

### Activity or Fragment



```
public class MainActivity extends ActionBarActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

	//initialize ViewSticker
    ViewSticker sticker = ViewSticker
      .starch(this, R.id.scroll_view, R.id.container);

	//prepare sticky
    sticker.stick(findViewById(R.id.description_header));
  }
  
}
```

## License

```
 Copyright 2013 Toshihiro.Yagi

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
```
