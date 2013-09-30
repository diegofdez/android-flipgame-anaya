package com.diegofdez.flipgame;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class HowTo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.howto);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.how_to, menu);
        return true;
    }
    
}
