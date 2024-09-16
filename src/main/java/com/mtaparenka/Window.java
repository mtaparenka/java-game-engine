package com.mtaparenka;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private long window;

    public void run() {
        init();
        loop();

        glfwFreeCallbacks(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Cannot init glfw");
        }

        initWindowHints();

        window = glfwCreateWindow(1920, 1080, "Pong", NULL, NULL);

        initWindowCallbacks();

        if (window == NULL) {
            throw new RuntimeException("Failed to create window");
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void initWindowCallbacks() {
        glfwSetMouseButtonCallback(window, MouseEventListener::mouseButtonCallback);
    }

    private static void initWindowHints() {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
    }

    private void loop() {
        GL.createCapabilities();

        glClearColor(0.0f, 1.0f, 0.0f, 0.0f);
        long startTime = System.nanoTime();
        long endTime;

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT);
            glfwSwapBuffers(window);
            glfwPollEvents();

            endTime = System.nanoTime();
            var dt = endTime-startTime;
            System.out.println("FPS: " + ( 1.0f/dt*1E9));
            startTime = System.nanoTime();
        }
    }
}
