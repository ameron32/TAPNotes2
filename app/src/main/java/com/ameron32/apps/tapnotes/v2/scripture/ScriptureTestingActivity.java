package com.ameron32.apps.tapnotes.v2.scripture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ameron32.apps.tapnotes.v2.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * PRIMARY TESTING ACTIVITY for MICAH
 */
public class ScriptureTestingActivity extends AppCompatActivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.hello)
    TextView mHelloTextView;


    private Bible mBible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_scripture);
        ButterKnife.inject(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBible = null;
        BibleBuilder bb = new BibleBuilder();
//        bb.buildDefaultBible(this);
        try {
            mBible = bb.getBible(this);
        } catch (BibleResourceNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            mHelloTextView.setText(Html.fromHtml(mBible.getVerses(0, 0, 0,1,2,3,4,5,6,7,8,9,10,11)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scripture_testing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
