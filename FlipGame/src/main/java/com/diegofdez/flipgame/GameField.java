package com.diegofdez.flipgame;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class GameField extends Activity {

    // Array de imagenes con colores
    private static final int[] colors = new int[] {
            R.drawable.ic_1c,
            R.drawable.ic_2c,
            R.drawable.ic_3c,
            R.drawable.ic_4c,
            R.drawable.ic_5c,
            R.drawable.ic_6c
    };

    // array de imagenes con numeros
    private static final int[] numbers = new int[] {
            R.drawable.ic_1n,
            R.drawable.ic_2n,
            R.drawable.ic_3n,
            R.drawable.ic_4n,
            R.drawable.ic_5n,
            R.drawable.ic_6n
    };

    // Apuntar el array preferido
    private int[] pictures = null;

    // Numero maximo de celdas (por defecto 3)
    private int topTileX = 3;
    private int topTileY = 3;

    // Maximo de elementos (colores o numeros) a utilizar
    private int topElements = 2;

    // Configuracion
    private boolean hasSound = false;
    private boolean hasVibration = false;

    // Array de identificadores de las celdas
    private int ids[][] = null;
    // Array de valores de las celdas
    private int values[][] = null;

    // Contador de pulsaciones
    private int numberOfClicks = 0;

    // Reproductor de sonido para cuando el jugador pulse una celda
    private MediaPlayer mp = null;
    // Controlador de vibracion para cuando el jugador pulse una celda
    private Vibrator vibratorService = null;

    // Texto para mostrar en pantalla el numero de pulsaciones
    private TextView tvNumberOfClicks = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_field);

        vibratorService = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        mp = MediaPlayer.create(this, R.raw.touch);

        tvNumberOfClicks = (TextView) findViewById(R.id.clicksTxt);

        // Obtener configuracion
        Bundle extras = getIntent().getExtras();
        topTileX = extras.getInt("xtiles") + 3;
        topTileY = extras.getInt("ytiles") + 3;
        topElements = extras.getInt("numcolors") + 2;
        if ("C".equals(extras.getString("tile"))) {
            pictures = colors;
        }
        else {
            pictures = numbers;
        }
        hasSound = extras.getBoolean("hasSound");
        hasVibration = extras.getBoolean("hasVibration");

        // Preparar el tablero
        LinearLayout ll = (LinearLayout) findViewById(R.id.fieldLandscape);
        ll.removeAllViews();

        // Calcular tamaño de celdas
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (dm.widthPixels -50)/ topTileX;
        int height = (dm.heightPixels -100) / topTileY;

        // Incializar arrays que almacenan ids y valores de las celdas
        ids = new int[topTileX][topTileY];
        values = new int[topTileX][topTileY];

        Random r = new Random(System.currentTimeMillis());
        int tilePictureToShow;

        int ident = 0;

        for (int i=0; i< topTileY; i++) {
            LinearLayout l2 = new LinearLayout(this);
            l2.setOrientation(LinearLayout.HORIZONTAL);
            for (int j=0; j < topTileX; j++) {
                tilePictureToShow = r.nextInt(topElements);
                values[j][i] = tilePictureToShow;

                TileView tv = new TileView(this, j, i, topElements, tilePictureToShow, pictures[tilePictureToShow]);
                ident++;
                tv.setId(ident);
                ids[j][i] = ident;
                tv.setHeight(height);
                tv.setWidth(width);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hasClick(((TileView)v).x, ((TileView)v).y);
                    }
                });
                l2.addView(tv);
            }
            ll.addView(l2);
        }

        Chronometer t = (Chronometer) findViewById(R.id.Chronometer);
        t.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_field, menu);
        return true;
    }

    private void hasClick(int x, int y) {
        // Responder al tacto
        if (hasVibration) vibratorService.vibrate(100);
        if (hasSound) mp.start();

        // Cambiar las celdas que corresponda
        changeView(x,y);
        // Esquinas
        if (x==0 && y==0) {
            changeView(0,1);
            changeView(1,0);
            changeView(1,1);
        }
        else if (x==0 && y==topTileY-1) {
            changeView(0,topTileY-2);
            changeView(1,topTileY-1);
            changeView(1,topTileY-2);
        }
        else if (x==topTileX-1 && y==0) {
            changeView(topTileX-1,1);
            changeView(topTileX-2,0);
            changeView(topTileX-2,1);
        }
        else if (x==topTileX-1 && y==topTileY-1) {
            changeView(topTileX-1,topTileY-2);
            changeView(topTileX-2,topTileY-1);
            changeView(topTileX-2,topTileY-2);
        }
        // Lados
        else if (x==0) {
            changeView(0,y-1);
            changeView(0,y+1);
            changeView(1,y);
        }
        else if (y==0) {
            changeView(x-1,0);
            changeView(x+1,0);
            changeView(x,1);
        }
        else if (x==topTileX-1) {
            changeView(topTileX-1,y-1);
            changeView(topTileX-1,y+1);
            changeView(topTileX-2,y);
        }
        else if (y==topTileY-1) {
            changeView(x-1,topTileY-1);
            changeView(x+1,topTileY-1);
            changeView(x,topTileY-2);
        }
        // Resto de celdas
        else {
            changeView(x,y-1);
            changeView(x,y+1);
            changeView(x-1,y);
            changeView(x+1,y);
        }

        // Actualizar contadores
        numberOfClicks++;
        tvNumberOfClicks.setText(getString(R.string.score_clicks) + numberOfClicks);

        // Comprobar fin de partida
        checkIfFinished();
    }

    private void changeView(int x, int y) {
        ///lalala
        TileView tt = (TileView) findViewById(ids[x][y]);
        int newIndex = tt.getNewIndex();
        values[x][y] = newIndex;
        tt.setBackgroundResource(pictures[newIndex]);
        tt.invalidate();
    }

    private void checkIfFinished() {
        int targetValue = values[0][0];

        // Comprobar si alguna celda es diferente de la primera,
        // y por tanto aun no se ha acabado el juego
        for (int i=0; i<topTileY; i++) {
            for (int j=0; j<topTileX; j++) {
                if (values[j][i] != targetValue) return;
            }
        }

        // Si llegamos aquí es que todas las celdas son iguales => se acabó el juego
        Intent resultIntent = new Intent((String) null);
        resultIntent.putExtra("clicks", numberOfClicks);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
