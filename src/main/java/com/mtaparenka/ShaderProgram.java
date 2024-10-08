package com.mtaparenka;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL46.*;

public class ShaderProgram {
    public final int vertexShaderID, fragmentShaderID;
    public final int programID;

    public ShaderProgram(String vertexShaderFilePath, String fragmentShaderFilePath) {
        vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
        compileVertexShader(vertexShaderFilePath);

        fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        compileFragmentShader(fragmentShaderFilePath);

        programID = glCreateProgram();
        linkProgram();
    }

    private void compileVertexShader(String vertexShaderFilePath) {
        try {
            glShaderSource(vertexShaderID, new String(Files.readAllBytes(Path.of(vertexShaderFilePath))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        glCompileShader(vertexShaderID);

        if (glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException(glGetShaderInfoLog(vertexShaderID));
        }
    }

    private void compileFragmentShader(String fragmentShaderFilePath) {
        try {
            glShaderSource(fragmentShaderID, new String(Files.readAllBytes(Path.of(fragmentShaderFilePath))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        glCompileShader(fragmentShaderID);

        if (glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException(glGetShaderInfoLog(fragmentShaderID));
        }
    }

    private void linkProgram() {
        glAttachShader(programID, vertexShaderID);
        glAttachShader(programID, fragmentShaderID);
        glLinkProgram(programID);

        if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException(glGetProgramInfoLog(programID));
        }
    }

    public void bind() {
        glUseProgram(programID);
    }

    public void dispose() {
        glUseProgram(0);
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);
        glDeleteProgram(programID);
    }
}
