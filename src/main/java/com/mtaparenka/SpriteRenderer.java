package com.mtaparenka;

import org.joml.Vector4f;

import static org.lwjgl.opengl.GL46.*;

public class SpriteRenderer {
    private float[] verticies;

    private int[] indicies = new int[]{
            0, 1, 3,   // first triangle
            1, 2, 3    // second triangle
    };

    private final Texture texture;
    private final Vector4f color;

    private int vbo;
    private int ebo;
    private int vao;

    public SpriteRenderer(String texturePath, Vector4f color, float x, float y, float width, float height) {
        verticies = new float[]{ // top and bot can actually be reversed depending on matrix orientation
                //position                          //tex coords
                x + width,  y + height,  0.0f,      1.0f, 1.0f, // top right
                x + width,  y,           0.0f,      1.0f, 0.0f, // bottom right
                x,          y,           0.0f,      0.0f, 0.0f, // bottom left
                x,          y + height,  0.0f,      0.0f, 1.0f, // top left
        };

        texture = new Texture(texturePath);
        this.color = color;

        createVertexArrayObject();
    }

    public static SpriteRenderer plainShape(Vector4f color, float x, float y, float width, float height) {
        return new SpriteRenderer("assets/sprites/white.png", color, x, y, width, height);
    }

    private void createVertexArrayObject() {
        vbo = glGenBuffers();
        ebo = glGenBuffers();
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, verticies, GL_DYNAMIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicies, GL_DYNAMIC_DRAW);

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
        ShaderContext.get().setUniform4fv("spriteColor", color);

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        texture.unbind();
    }

    public void dispose() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
    }
}
