package com.mtaparenka.pong;

import com.mtaparenka.engine.ShaderContext;
import com.mtaparenka.engine.Window;
import com.mtaparenka.engine.camera.OrthographicCamera;
import com.mtaparenka.engine.render.Scene;
import com.mtaparenka.engine.render.TextRenderer;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class MainMenuScene extends Scene {
    private final Window window;
    private final OrthographicCamera camera;
    private final float viewportWidth = 640f;
    private final float viewportHeight = 400f;
    private final TextRenderer newGameText;

    public MainMenuScene(Window window) {
        this.window = window;
        this.newGameText = new TextRenderer("New game", new Vector2f(100f), new Vector2f(2f));

        Matrix4f model = new Matrix4f()
                .identity();

        ShaderContext.get().setUniformMatrix4fv("model", model);

        this.camera = new OrthographicCamera(0f, viewportWidth, viewportHeight, 0f);
        ShaderContext.get().setUniformMatrix4fv("projectionView", camera.combined);
    }

    public void updatePhysics(double dt) {
        processInput();
    }

    public void update(double dt) {
        camera.update();
        newGameText.draw(dt);

        glBindVertexArray(0);
    }

    public void dispose() {

    }

    private void processInput() {
        if (glfwGetMouseButton(window.id, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) {
            window.setScene(new TestScene(window));
        }
    }
}
