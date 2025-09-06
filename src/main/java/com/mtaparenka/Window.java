package com.mtaparenka;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private static final String DEFAULT_VERTEX = "assets/shaders/default_vertex.glsl";
    private static final String DEFAULT_FRAGMENT = "assets/shaders/default_fragment.glsl";

    private static double lastFrame = 0.0f;
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

        while (!glfwWindowShouldClose(window)) {
            double currentFrame = glfwGetTime();
            dt = currentFrame - lastFrame;
            lastFrame = currentFrame;

            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            scene.update(dt);

            //System.out.printf("%.0f%n", (1.0f / dt));

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        scene.dispose();
        ShaderContext.get().dispose();
    }
}
