package com.mtaparenka;

import com.mtaparenka.engine.camera.OrthographicCamera;
import com.mtaparenka.engine.font.BitmapFont;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class TestScene {
    private final long windowId;
    private final OrthographicCamera camera;
    private SpriteRenderer player1;
    private SpriteRenderer player2;
    private TextRenderer testText;

    private final float cameraSpeed = 1f;

    public TestScene(long windowId) {
        this.windowId = windowId;

        Vector4f whiteColor = new Vector4f(1f, 1f, 1f, 1f);
        BitmapFont bitmapFont = new BitmapFont("assets/fonts/boldpixels.fnt", new Texture("assets/fonts/boldpixels_0.png"));
        //player1 = SpriteRenderer.plainShape(whiteColor, 0f, 0f, 2f, 24f, shaderProgram);
        player1 = new SpriteRenderer("assets/sprites/adventurer-air-attack1-01.png", whiteColor, 0f, 0f, 32f, 32f);
        testText = new TextRenderer(bitmapFont, "Sasul\nnew line", 0f, 50f);

        // model is a world-coordinate matrix, e.g. object placed at x = 100px, y = 200px
        Matrix4f model = new Matrix4f()
                .identity()
                .translate(0f, 0f, 0f);

        ShaderContext.get().setUniformMatrix4fv("model", model);

        camera = new OrthographicCamera(0f, 104f, 104f, 0f); // 0.0 top-left
    }

    public void update(double dt) {
        //camera
        ShaderContext.get().setUniformMatrix4fv("projectionView", camera.combined);

        processInput();
        camera.update();

        //draw sprites
        player1.draw(dt);
        testText.draw(dt);

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

        if (glfwGetKey(windowId, GLFW_KEY_7) == GLFW_PRESS) {
            testText.buildBuffer("Sasa <3");
        }
    }
}
