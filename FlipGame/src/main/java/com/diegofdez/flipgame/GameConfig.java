package com.diegofdez.flipgame;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class GameConfig extends Activity {
    private static final int ACTION_PLAY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.startButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlay();
            }
        });

        SeekBar xTiles = (SeekBar) findViewById(R.id.seekbarX);
        xTiles.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateXTiles(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        updateXTiles(xTiles.getProgress());

        SeekBar yTiles = (SeekBar) findViewById(R.id.seekbarY);
        yTiles.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateYTiles(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        updateYTiles(yTiles.getProgress());

        SeekBar colors = (SeekBar) findViewById(R.id.seekbarColors);
        colors.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateColors(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        updateColors(colors.getProgress());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_player:
                showPlayer();
                return true;
            case R.id.m_howto:
                showHowTo();
                return true;
            case R.id.m_about:
                showAbout();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Mostrar mensaje de fin de juego
        Log.d("Completo", "Juego completo: Enviado: " + requestCode + "Recibido: " + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACTION_PLAY:
                    new AlertDialog.Builder(this).setMessage(
                        getResources().getString(R.string.game_end_1)
                        + data.getIntExtra("clicks", 0)
                        + getResources().getString(R.string.game_end_2));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startPlay()
    {
        Intent i = new Intent(this, GameField.class);

        // Configurar numero de filas columnas y colores
        SeekBar sb = (SeekBar) findViewById(R.id.seekbarX);
        i.putExtra("xtiles", sb.getProgress());
        sb = (SeekBar) findViewById(R.id.seekbarY);
        i.putExtra("ytiles", sb.getProgress());
        sb = (SeekBar) findViewById(R.id.seekbarColors);
        i.putExtra("numcolors", sb.getProgress());

        // Colores o numeros?
        RadioButton r = (RadioButton) findViewById(R.id.radioButtonC);
        i.putExtra("tile", r.isChecked()?"C":"N");
        // Control de sonido
        CheckBox c = (CheckBox) findViewById(R.id.checkboxSound);
        i.putExtra("hasSound", c.isChecked());
        // Control de vibracion
        c = (CheckBox) findViewById(R.id.checkboxVibrate);
        i.putExtra("hasVibration", c.isChecked());

        // Comenzar
        startActivityForResult(i, ACTION_PLAY);
    }

    private void updateXTiles(int progress)
    {
        TextView tv = (TextView) findViewById(R.id.seekbarXtxt);
        tv.setText(getString(R.string.num_elem_x) + (progress + 3));
    }

    private void updateYTiles(int progress)
    {
        TextView tv = (TextView) findViewById(R.id.seekbarYtxt);
        tv.setText(getString(R.string.num_elem_y) + (progress + 3));
    }

    private void updateColors(int progress)
    {
        TextView tv = (TextView) findViewById(R.id.seekbarColorsTxt);
        tv.setText(getString(R.string.num_colors) + (progress + 2));
    }

    private void showAbout() {
        Intent i = new Intent(this, About.class);
        startActivity(i);
    }

    private void showHowTo() {
        Intent i = new Intent(this, HowTo.class);
        startActivity(i);
    }

    private void showPlayer() {
        // lalalalala
    }
}
