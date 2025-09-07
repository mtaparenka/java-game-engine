package com.mtaparenka.engine.render;

import com.mtaparenka.engine.ShaderContext;
import com.mtaparenka.engine.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL46.*;

public class Base2DRenderer {
    public int vao, vbo, ebo;
    public Texture texture;
    public Vector2f position;
    public Vector4f color;
    public int maxElementsCount;
    public int renderCount;

    public Base2DRenderer(int vao, int vbo, int ebo, Texture texture, Vector2f position, Vector4f color, int maxElementsCount) {
        this.vao = vao;
        this.vbo = vbo;
        this.ebo = ebo;
        this.texture = texture;
        this.position = position;
        this.color = color;
        this.maxElementsCount = maxElementsCount;
        this.renderCount = maxElementsCount;

        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, maxElementsCount * 16L * Float.BYTES, GL_DYNAMIC_DRAW); // 8 xy and 8 uv = 16
        IntBuffer indiciesBuffer = BufferUtils.createIntBuffer(maxElementsCount * 6);

        for (int i = 0; i < maxElementsCount; i++) {
            int indexOffset = i * 4;

            indiciesBuffer.put(new int[] {
                    indexOffset, indexOffset + 1, indexOffset + 3,        // first triangle
                    indexOffset + 1, indexOffset + 2, indexOffset + 3     // second triangle
            });
        }

        indiciesBuffer.flip();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indiciesBuffer, GL_STATIC_DRAW);

        int stride = 4 * Float.BYTES;
        int texPointer = 2 * Float.BYTES;

        glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, stride, texPointer);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }

    public void bufferSubData(FloatBuffer verticiesBuffer) {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, verticiesBuffer);
    }

    public void bufferSubData(float[] verticiesBuffer) {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, verticiesBuffer);
    }

    public void draw(double dt) {
        texture.bind();
        glBindVertexArray(vao);
        ShaderContext.get().setUniform4fv("spriteColor", color);

        glDrawElements(GL_TRIANGLES, renderCount * 6, GL_UNSIGNED_INT, 0);
        texture.unbind();
    }

    public void dispose() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
    }
}
