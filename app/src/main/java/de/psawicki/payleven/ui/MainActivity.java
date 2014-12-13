package de.psawicki.payleven.ui;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import de.psawicki.payleven.R;
import de.psawicki.payleven.app.PaylevenApplication;

public class MainActivity extends Activity {

    private PaylevenApplication paylevenApplication = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        paylevenApplication = (PaylevenApplication) getApplication();
    }

}
