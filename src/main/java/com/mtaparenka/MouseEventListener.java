package com.mtaparenka;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class MouseEventListener {
    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS) {
            System.out.println("Right Click works");
            DoubleBuffer xb = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer yb = BufferUtils.createDoubleBuffer(1);
            GLFW.glfwGetCursorPos(window, xb, yb);

            System.out.println(xb.get() + ", " + yb.get());
        }

    }
}
