package com.mtaparenka.engine;

public class ShaderContext {
    private static ShaderProgram shaderProgram;

    public static ShaderProgram get() {
        return shaderProgram;
    }

    public static void set(ShaderProgram shaderProgram) {
        ShaderContext.shaderProgram = shaderProgram;
    }
}
