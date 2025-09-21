package com.mtaparenka.engine;

import com.mtaparenka.engine.render.Scene;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.windows.User32.HWND_TOPMOST;
import static org.lwjgl.system.windows.User32.SWP_NOMOVE;
import static org.lwjgl.system.windows.User32.SWP_NOSIZE;
import static org.lwjgl.system.windows.User32.SWP_SHOWWINDOW;
import static org.lwjgl.system.windows.User32.SetWindowPos;

public class Window {
    private static final String DEFAULT_VERTEX = "/assets/shaders/default_vertex.glsl";
    private static final String DEFAULT_FRAGMENT = "/assets/shaders/default_fragment.glsl";
    private double updateRate = 60f;
    private double updateInterval = 1.0 / updateRate;
    private double accumulator = 0;
    public long id;
    private Scene scene;

    public Window() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Cannot init glfw");
        }

        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode videoMode = glfwGetVideoMode(monitor);

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        id = glfwCreateWindow(videoMode.width(), videoMode.height(), "Pong", NULL, NULL);

        glfwSetWindowSize(id, videoMode.width(), videoMode.height());
        glfwSetWindowPos(id, 0, 0);
        glfwSetWindowAttrib(id, GLFW_DECORATED, GLFW_FALSE);

        long nId = GLFWNativeWin32.glfwGetWin32Window(id);
        SetWindowPos(nId, HWND_TOPMOST, 0, 0, videoMode.width(), videoMode.height() + 1, SWP_SHOWWINDOW);

        if (id == NULL) {
            throw new RuntimeException("Failed to create window");
        }

        glfwSetMouseButtonCallback(id, MouseEventListener::mouseButtonCallback);
        glfwSetKeyCallback(id, Window::keys);
        glfwMakeContextCurrent(id);
        glfwSwapInterval(1);
        glfwShowWindow(id);

        GL.createCapabilities();

        ShaderContext.set(new ShaderProgram(DEFAULT_VERTEX, DEFAULT_FRAGMENT));
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void run() {
        loop();

        glfwFreeCallbacks(id);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private static void keys(long window,
                             int key,
                             int scancode,
                             int action,
                             int mods) {
        //System.out.println(key);
    }

    private void loop() {
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        double dt;
        double currentFrame;
        double lastFrame = glfwGetTime();
        while (!glfwWindowShouldClose(id)) {
            currentFrame = glfwGetTime();
            dt = currentFrame - lastFrame;
            lastFrame = currentFrame;

            if (dt > 0.25) dt = 0.25;
            accumulator += dt;

            while (accumulator >= updateInterval) {
                scene.updatePhysics(updateInterval);
                accumulator -= updateInterval;
            }
            glClearColor(0f, 0f, 0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            scene.update(dt);
            //System.out.printf("%.0f%n", (1.0f / dt));
            glfwSwapBuffers(id);
            glfwPollEvents();
        }

        scene.dispose();
        ShaderContext.get().dispose();
        glfwDestroyWindow(id);
    }
}
