package com.mtaparenka.engine;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Timer {
    private double timer;

    public void setTimer(int seconds) {
        timer = glfwGetTime() + seconds;
    }

    public boolean isPassed() {
        return timer <= glfwGetTime();
    }
}
