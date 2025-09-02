package com.mtaparenka;

import com.mtaparenka.engine.font.BitmapFont;
import com.mtaparenka.engine.font.Glyph;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL46.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class TextRenderer {
    private int vao, vbo, ebo;
    private float x, y;

    private ShaderProgram shaderProgram;
    private BitmapFont font;
    private Vector4f color;
    private int renderCount;

    public TextRenderer(ShaderProgram shaderProgram, BitmapFont font, Vector4f color, String text, float x, float y) {
        this.shaderProgram = shaderProgram;
        this.font = font;
        this.color = color;
        this.x = x;
        this.y = y;

        //fix single channeling
        font.atlasTexture.bind();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_SWIZZLE_R, GL_ONE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_SWIZZLE_G, GL_ONE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_SWIZZLE_B, GL_ONE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_SWIZZLE_A, GL_RED);
        font.atlasTexture.unbind();

        buildBuffer(text);
    }

    public void buildBuffer(String text) {
        FloatBuffer verticiesBuffer = BufferUtils.createFloatBuffer(16 * text.length());
        IntBuffer indiciesBuffer = BufferUtils.createIntBuffer(6 * text.length());
        float advancedX = x;

        for (int i = 0; i < text.length(); i++) {
            Glyph g = font.glyphs.get(text.codePointAt(i));

            float x0 = advancedX + g.xOffset();
            float y0 = y + g.yOffset();
            float x1 = x0 + g.width();
            float y1 = y0 + g.height();
            float u0 = (float) (g.x()) / font.scaleW;
            float v0 = (float) (g.y()) / font.scaleH;
            float u1 = (float) (g.x() + g.width()) / font.scaleW;
            float v1 = (float) (g.y() + g.height()) / font.scaleH;

            verticiesBuffer.put(new float[] {
                    x1, y1,       u1, v1, // top right
                    x1, y0,       u1, v0, // bottom right
                    x0, y0,       u0, v0, // bottom left
                    x0, y1,       u0, v1  // top left
            });

            int indexOffset = i * 4;

            indiciesBuffer.put(new int[] {
                    indexOffset, indexOffset + 1, indexOffset + 3,   // first triangle
                    indexOffset + 1, indexOffset + 2, indexOffset + 3    // second triangle
            });

            advancedX += g.xAdvance();
        }

        verticiesBuffer.flip();
        indiciesBuffer.flip();

        renderCount = indiciesBuffer.limit();

        vbo = glGenBuffers();
        ebo = glGenBuffers();
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, verticiesBuffer, GL_DYNAMIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indiciesBuffer, GL_DYNAMIC_DRAW);

        int stride = 4 * Float.BYTES;
        int texPointer = 2 * Float.BYTES;

        glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, stride, texPointer);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }

    public void draw(double dt) {
        font.atlasTexture.bind();
        glBindVertexArray(vao);
        shaderProgram.setUniform4fv("spriteColor", color);

        glDrawElements(GL_TRIANGLES, renderCount, GL_UNSIGNED_INT, 0);
        font.atlasTexture.unbind();
    }
}
