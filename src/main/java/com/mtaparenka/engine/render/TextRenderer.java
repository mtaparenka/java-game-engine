package com.mtaparenka.engine.render;

import com.mtaparenka.engine.font.BitmapFont;
import com.mtaparenka.engine.font.Glyph;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL46.*;

import java.nio.FloatBuffer;

public class TextRenderer {
    private final BitmapFont font;
    private final Base2DRenderer renderer;
    public Vector2f position;

    public TextRenderer(BitmapFont font, Vector4f color, String text, Vector2f position, int maxChars) {
        this.font = font;
        this.position = position;

        //fix single channeling
        font.atlasTexture.bind();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_SWIZZLE_R, GL_ONE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_SWIZZLE_G, GL_ONE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_SWIZZLE_B, GL_ONE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_SWIZZLE_A, GL_RED);
        font.atlasTexture.unbind();

        int vao = glGenVertexArrays();
        int vbo = glGenBuffers();
        int ebo = glGenBuffers();

        renderer = new Base2DRenderer(vao, vbo, ebo, font.atlasTexture, position, color, maxChars);

        setText(text);
    }

    public TextRenderer(BitmapFont font, Vector4f color, String text, Vector2f position) {
        this(font, color, text, position, 256);
    }

    public TextRenderer(BitmapFont font, String text, Vector2f position) {
        this(font, new Vector4f(1f), text, position, 256);
    }

    public void setText(String text) {
        FloatBuffer verticiesBuffer = BufferUtils.createFloatBuffer(16 * text.length());
        float advancedX = position.x;
        float advanceY = position.y;

        for (int i = 0; i < text.length(); i++) {
            int codePoint = text.codePointAt(i);

            if (codePoint == 10) {
                advancedX = position.x;
                advanceY += font.lineHeight;
            } else {
                Glyph g = font.glyphs.get(text.codePointAt(i));
                float x0 = advancedX + g.xOffset();
                float y0 = advanceY + g.yOffset();
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

                advancedX += g.xAdvance();
            }
        }

        verticiesBuffer.flip();
        renderer.renderCount = verticiesBuffer.limit() / 16;
        renderer.bufferSubData(verticiesBuffer);
    }

    public void draw(double dt) {
        renderer.draw(dt);
    }

    public void dispose() {
        renderer.dispose();
    }
}
