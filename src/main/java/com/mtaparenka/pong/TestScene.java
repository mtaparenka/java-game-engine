package com.mtaparenka.pong;

import com.mtaparenka.engine.ShaderContext;
import com.mtaparenka.engine.camera.OrthographicCamera;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class TestScene {
    private final long windowId;
    private final OrthographicCamera camera;
    private Player player1;
    private Ball ball;

    private final float cameraSpeed = 1f;

    public TestScene(long windowId) {
        this.windowId = windowId;
        float viewportWidth = 640f;
        float viewportHeight = 400f;
        float playerWidth = 10f;
        float playerHeight = 80f;
        float ballSize = 10f;

        player1 = new Player(new Vector2f(0f, viewportHeight / 2 - playerHeight / 2), playerWidth, playerHeight);
        ball = new Ball(new Vector2f(viewportWidth / 2 - ballSize / 2, viewportHeight / 2 - ballSize), ballSize, ballSize);
        
        // model is a world-coordinate matrix, e.g. object placed at x = 100px, y = 200px
        Matrix4f model = new Matrix4f()
                .identity();

        ShaderContext.get().setUniformMatrix4fv("model", model);

        camera = new OrthographicCamera(0f, viewportWidth, viewportHeight, 0f); // 0.0 top-left
    }

    public void update(double dt) {

        float speed = 0.5f;
        double dx = Math.cos(Math.toRadians(2d)) * speed;
        double dy = Math.sin(Math.toRadians(2d)) * 2;
        ball.position.x -= dx;
        ball.position.y -= dy;
        ball.spriteRenderer.buildVertexBuffer();
    }

    public void render(double dt) {
        //camera
        ShaderContext.get().setUniformMatrix4fv("projectionView", camera.combined);

        processInput();
        camera.update();

        //draw sprites
        player1.draw(dt);
        ball.draw(dt);
        glBindVertexArray(0);
    }

    public void dispose() {

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
            camera.setZoom(camera.zoom + 0.1f);
        }
        if (glfwGetKey(windowId, GLFW_KEY_PAGE_DOWN) == GLFW_PRESS) {
            camera.setZoom(camera.zoom - 0.1f);
        }
    }
}
