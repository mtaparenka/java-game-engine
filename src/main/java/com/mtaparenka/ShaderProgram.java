package com.mtaparenka;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL46.*;

public class ShaderProgram {
    public final int vertexShaderID, fragmentShaderID;
    public final int programID;

    private final FloatBuffer tmpBuffer16 = BufferUtils.createFloatBuffer(16);

    public ShaderProgram(String vertexShaderFilePath, String fragmentShaderFilePath) {
        vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
        compileVertexShader(vertexShaderFilePath);

        fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        compileFragmentShader(fragmentShaderFilePath);

        programID = glCreateProgram();
        linkProgram();
        use();
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

    public void setUniformMatrix4fv(String uniformName, Matrix4f matrix) {
        int transLocation = glGetUniformLocation(programID, uniformName);
        matrix.get(tmpBuffer16);
        glUniformMatrix4fv(transLocation, false, tmpBuffer16);
        tmpBuffer16.clear();
    }

    public void setUniform4fv(String uniformName, Vector4f vector) {
        int transLocation = glGetUniformLocation(programID, uniformName);

        glUniform4fv(transLocation, new float[]{vector.x, vector.y, vector.z, vector.w});
    }


    public void setUniformMatrix4fv(String uniformName, FloatBuffer fb) {
        int transLocation = glGetUniformLocation(programID, uniformName);
        glUniformMatrix4fv(transLocation, false, fb);
    }

    public void use() {
        glUseProgram(programID);
    }

    public void dispose() {
        glUseProgram(0);
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);
        glDeleteProgram(programID);
    }
}
