package com.diegofdez.flipgame;

import android.content.Context;
import android.widget.Button;

public class TileView extends Button {

    // coordenadas
    public int x = 0;
    public int y = 0;
    // trama a mostrar
    private int index = 0;
    // maximo de tramas
    private int topElements = 0;

    public TileView(Context context, int x, int y, int topElements, int index, int background) {
        super(context);

        this.x = x;
        this.y = y;
        this.topElements = topElements;
        this.index = index;
        this.setBackgroundResource(background);
    }

    public int getNewIndex() {
        index++;

        // ¿Reiniciar tramas?
        if (index == topElements) index = 0;
        return index;
    }
}
