package com.mtaparenka.engine;

import com.mtaparenka.pong.TestScene;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private static final String DEFAULT_VERTEX = "/assets/shaders/default_vertex.glsl";
    private static final String DEFAULT_FRAGMENT = "/assets/shaders/default_fragment.glsl";
    private double updateRate = 60f;
    private double updateInterval = 1.0 / updateRate;
    private double accumulator = 0;
    public long window;
    private TestScene scene;

    public Window() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Cannot init glfw");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode videoMode = glfwGetVideoMode(monitor);
        window = glfwCreateWindow(1920, 1080, "Pong", NULL, NULL);

        if (window == NULL) {
            throw new RuntimeException("Failed to create window");
        }

        glfwSetMouseButtonCallback(window, MouseEventListener::mouseButtonCallback);
        glfwSetKeyCallback(window, Window::keys);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        GL.createCapabilities();

        ShaderContext.set(new ShaderProgram(DEFAULT_VERTEX, DEFAULT_FRAGMENT));
    }

    public void setScene(TestScene scene) {
        this.scene = scene;
    }

    public void run() {
        loop();

        glfwFreeCallbacks(window);
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
        while (!glfwWindowShouldClose(window)) {
            currentFrame = glfwGetTime();
            dt = currentFrame - lastFrame;
            lastFrame = currentFrame;

            if (dt > 0.25) dt = 0.25;
            accumulator += dt;

            while (accumulator >= updateInterval) {
                scene.update(updateInterval);
                accumulator -= updateInterval;
            }
            glClearColor(0f, 0f, 0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            scene.render(dt);
            //System.out.printf("%.0f%n", (1.0f / dt));
            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        scene.dispose();
        ShaderContext.get().dispose();
        glfwDestroyWindow(window);
    }
}
