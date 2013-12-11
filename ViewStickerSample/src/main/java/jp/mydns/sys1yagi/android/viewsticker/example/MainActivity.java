package jp.mydns.sys1yagi.android.viewsticker.example;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import jp.mydns.sys1yagi.android.viewsticker.ViewSticker;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewSticker sticker = ViewSticker.starch(this, R.id.scroll_view, R.id.container);

        sticker.stick(findViewById(R.id.description_header));
        sticker.stick(findViewById(R.id.physical_features_header));
    }
}
