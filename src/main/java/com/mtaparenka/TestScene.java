package com.mtaparenka;

import static org.lwjgl.opengl.GL46.*;

public class TestScene {
    private int vbo;
    private int ebo;
    private int vao;


    private float[] verticies = new float[] {
            //position              //color             //tex coords
            0.5f,  0.5f, 0.0f,      1.0f, 0.0f, 0.0f,   1.0f, 1.0f, // top right
            0.5f, -0.5f, 0.0f,      0.0f, 1.0f, 0.0f,   1.0f, 0.0f, // bottom right
            -0.5f, -0.5f, 0.0f,     0.0f, 0.0f, 1.0f,   0.0f, 0.0f, // bottom left
            -0.5f,  0.5f, 0.0f,     0.0f, 0.0f, 0.0f,   0.0f, 1.0f, // top left
    };

    private int[] indicies = new int[] {
            0, 1, 3,   // first triangle
            1, 2, 3    // second triangle
    };

    private final ShaderProgram shaderProgram;
    private final Texture texture;

    public TestScene() {
        shaderProgram = new ShaderProgram("assets/shaders/default_vertex.glsl", "assets/shaders/default_fragment.glsl");
        texture = new Texture("assets/sprites/adventurer-air-attack1-01.png");
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

        int stride = 8 * Float.BYTES;
        int colorPointer = 3 * Float.BYTES;
        int texPointer = 6 * Float.BYTES;

        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, colorPointer);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, texPointer);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
    }

    public void update(float dt) {
        shaderProgram.bind();
        texture.bind();
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        texture.unbind();
    }

    public void dispose() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
        shaderProgram.dispose();
    }
}
