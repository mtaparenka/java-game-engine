package com.mtaparenka;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;

public class Texture {
    public final int texId;

    public Texture(String imagePath) {
        texId = glGenTextures();
        bind();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        //stbi_set_flip_vertically_on_load(true);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        ByteBuffer imageData = stbi_load(imagePath, width, height, channels, 0);

        if (imageData == null) {
            throw new RuntimeException("Failed to load texture");
        }
        int format = 0;

        if (channels.get(0) == 3) {
            format = GL_RGB;
        } else if (channels.get(0) == 4) {
            format = GL_RGBA;
        } else if (channels.get(0) == 1){
            format = GL_RED;
        }

        glTexImage2D(GL_TEXTURE_2D, 0, format, width.get(0), height.get(0), 0, format, GL_UNSIGNED_BYTE, imageData);

        stbi_image_free(imageData);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texId);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
