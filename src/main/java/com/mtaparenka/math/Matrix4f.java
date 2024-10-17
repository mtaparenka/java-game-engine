package com.mtaparenka.math;

import java.nio.FloatBuffer;

public class Matrix4f {
    private final float[] matrix = new float[16];

    public Matrix4f(float value) {
        //  0   1   2   3
        //  4   5   6   7
        //  8   9   10  11
        //  12  13  14  15
        matrix[0] = value;
        matrix[1] = 0f;
        matrix[2] = 0f;
        matrix[3] = 0f;
        matrix[4] = 0f;
        matrix[5] = value;
        matrix[6] = 0f;
        matrix[7] = 0f;
        matrix[8] = 0f;
        matrix[9] = 0f;
        matrix[10] = value;
        matrix[11] = 0f;
        matrix[12] = 0f;
        matrix[13] = 0f;
        matrix[14] = 0f;
        matrix[15] = value;
    }

    public static Matrix4f identity() {
        return new Matrix4f(1f);
    }


    public void get(FloatBuffer fb) {
        fb.put(matrix).position(0);
    }
}
