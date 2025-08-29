package com.mtaparenka;

import com.mtaparenka.engine.camera.OrthographicCamera;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.glfw.GLFW.*;

public class TestScene {
    private final long windowId;
    private final ShaderProgram shaderProgram;
    private final OrthographicCamera camera;
    private Sprite spriteA;
    private Sprite spriteB;
    private Sprite spriteC;

    private final float cameraSpeed = 1f;

    public TestScene(long windowId) {
        this.windowId = windowId;
        shaderProgram = new ShaderProgram("assets/shaders/default_vertex.glsl", "assets/shaders/default_fragment.glsl");
        spriteA = new Sprite("assets/sprites/adventurer-air-attack1-01.png", 0f, 0f, 50f, 50f);
        spriteB = new Sprite("assets/sprites/adventurer-air-attack1-01.png", 500f, 200f, 50f, 50f);
        spriteC = new Sprite("assets/sprites/adventurer-air-attack1-01.png", 0f, 550f, 50f, 50f);

        // model is a world-coordinate matrix, e.g. object placed at x = 100px, y = 200px
        Matrix4f model = new Matrix4f()
                .identity()
                .translate(0f, 0f, 0f);

        shaderProgram.setUniformMatrix4fv("model", model);

        camera = new OrthographicCamera(0f, 800f, 600f, 0f); // 0.0 top-left
    }

    public void update(double dt) {
        //camera
        shaderProgram.setUniformMatrix4fv("projection", camera.projection);
        shaderProgram.setUniformMatrix4fv("view", camera.view);
        shaderProgram.setUniform4fv("spriteColor", new Vector4f(1f, 1f, 1f, 1f));

        processInput();
        camera.update();

        //draw sprites
        spriteA.draw(dt);
        spriteB.draw(dt);
        spriteC.draw(dt);
    }

    public void dispose() {
        shaderProgram.dispose();
    }

    public void processInput() {
        if (glfwGetKey(windowId, GLFW_KEY_UP) == GLFW_PRESS) {
            camera.position.y -= cameraSpeed;
        }
        if (glfwGetKey(windowId, GLFW_KEY_DOWN) == GLFW_PRESS) {
            camera.position.y += cameraSpeed;
        }
        if (glfwGetKey(windowId, GLFW_KEY_LEFT) == GLFW_PRESS) {
            camera.position.x -= cameraSpeed;
        }
        if (glfwGetKey(windowId, GLFW_KEY_RIGHT) == GLFW_PRESS) {
            camera.position.x += cameraSpeed;
        }
        if (glfwGetKey(windowId, GLFW_KEY_PAGE_UP) == GLFW_PRESS) {
            camera.setZoom(camera.zoom + 0.5f);
        }
        if (glfwGetKey(windowId, GLFW_KEY_PAGE_DOWN) == GLFW_PRESS) {
            camera.setZoom(camera.zoom - 0.5f);
        }
    }
}
