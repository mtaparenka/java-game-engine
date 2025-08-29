package com.mtaparenka;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.glfw.GLFW.*;

public class Sprite {
    private float[] verticies;

    private int[] indicies = new int[] {
            0, 1, 3,   // first triangle
            1, 2, 3    // second triangle
    };

    private final Texture texture;

    private int vbo;
    private int ebo;
    private int vao;

    public Sprite(String texturePath, float x, float y, float width, float height) {
        verticies = new float[] {
                //position                          //tex coords
                x + width,  y + height,  0.0f,      1.0f, 1.0f, // top right
                x + width,  y,           0.0f,      1.0f, 0.0f, // bottom right
                x,          y,           0.0f,      0.0f, 0.0f, // bottom left
                x,          y + height,  0.0f,      0.0f, 1.0f, // top left
        };

        texture = new Texture(texturePath);

        createVertexArrayObject();
    }

    private void createVertexArrayObject() {
        vbo = glGenBuffers();
        ebo = glGenBuffers();
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, verticies, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicies, GL_STATIC_DRAW);

        int stride = 5 * Float.BYTES;
        int texPointer = 3 * Float.BYTES;

        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, stride, texPointer);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }

    public void draw(double dt) {
        texture.bind();
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        texture.unbind();
    }

    public void dispose() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
    }
}
