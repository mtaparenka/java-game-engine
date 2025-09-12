package com.mtaparenka.engine.render;

import com.mtaparenka.engine.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL46.*;

public class SpriteRenderer {
    private Base2DRenderer renderer;
    public Vector2f position;
    public float width;
    public float height;

    public SpriteRenderer(String texturePath, Vector4f color, Vector2f position, float width, float height) {
        this.position = position;
        this.width = width;
        this.height = height;

        int vbo = glGenBuffers();
        int ebo = glGenBuffers();
        int vao = glGenVertexArrays();

        renderer = new Base2DRenderer(vao, vbo, ebo, new Texture(texturePath), position, color, 1);

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        update();
    }

    public static SpriteRenderer plainShape(Vector4f color, Vector2f position, float width, float height) {
        return new SpriteRenderer("/assets/sprites/white.png", color, position, width, height);
    }

    public void update() {
        float[] verticies = new float[]{ // top and bot can actually be reversed depending on matrix orientation
                //position                          //tex coords
                position.x + width,  position.y + height,     1.0f, 1.0f, // top right
                position.x + width,  position.y,              1.0f, 0.0f, // bottom right
                position.x,          position.y,              0.0f, 0.0f, // bottom left
                position.x,          position.y + height,     0.0f, 1.0f, // top left
        };

        renderer.renderCount = verticies.length / 16;
        renderer.bufferSubData(verticies);
    }

    public void draw(double dt) {
        renderer.draw(dt);
    }

    public void dispose() {
        renderer.dispose();
    }
}
