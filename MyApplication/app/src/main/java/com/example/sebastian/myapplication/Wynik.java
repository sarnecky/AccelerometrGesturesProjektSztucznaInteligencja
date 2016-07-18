package com.example.sebastian.myapplication;

/**
 * Created by Sebastian on 2016-03-21.
 */
public class Wynik {
    public float[] values = new float[3];

    public Wynik(float tab[])
    {
        for(int i=0;i<3;i++)
            values[i]=tab[i];
    }
}
