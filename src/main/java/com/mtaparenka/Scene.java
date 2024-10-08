package com.mtaparenka;

import static org.lwjgl.opengl.GL46.*;

public class Scene {
    private int vbo;
    private int ebo;
    private int vao;

    private float[] verticies = new float[] {
            0.5f,  0.5f, 0.0f,  // top right
            0.5f, -0.5f, 0.0f,  // bottom right
            -0.5f, -0.5f, 0.0f,  // bottom left
            -0.5f,  0.5f, 0.0f   // top left
    };

    private int[] indicies = new int[] {
            0, 1, 3,   // first triangle
            1, 2, 3    // second triangle
    };

    private ShaderProgram shaderProgram;

    public Scene() {
        shaderProgram = new ShaderProgram("assets/shaders/default_vertex.glsl", "assets/shaders/default_fragment.glsl");
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

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);
        glEnableVertexAttribArray(0);
    }

    public void update(float dt) {
        shaderProgram.bind();
        glBindVertexArray(vao);
        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
    }

    public void dispose() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
        shaderProgram.dispose();
    }
}
