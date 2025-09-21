package com.mtaparenka.pong;

import com.mtaparenka.engine.ShaderContext;
import com.mtaparenka.engine.Timer;
import com.mtaparenka.engine.Window;
import com.mtaparenka.engine.camera.OrthographicCamera;
import com.mtaparenka.engine.render.Scene;
import com.mtaparenka.engine.render.TextRenderer;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class TestScene extends Scene {
    private final Window window;
    private final OrthographicCamera camera;
    private final Player player1;
    private final Player player2;
    private final TextRenderer player1Score;
    private final TextRenderer player2Score;
    private final TextRenderer winText;
    private final Ball ball;
    private final float viewportWidth = 640f;
    private final float viewportHeight = 400f;
    private final float playerWidth = 10f;
    private final float playerHeight = 80f;
    private final float ballSize = 10f;
    private boolean isGameFinished;

    public TestScene(Window window) {
        this.window = window;

        player1 = new Player(new Vector2f(0f, viewportHeight / 2 - playerHeight / 2), playerWidth, playerHeight);
        player2 = new Player(new Vector2f(viewportWidth - playerWidth, viewportHeight / 2 - playerHeight / 2), playerWidth, playerHeight);
        player1Score = new TextRenderer("0", new Vector2f(viewportWidth / 2 - 50f, 20f), new Vector2f(3f));
        player2Score = new TextRenderer("0", new Vector2f(viewportWidth / 2 + 40f, 20f), new Vector2f(3f));
        winText = new TextRenderer("Player 1 won", new Vector2f(viewportWidth / 2 - 125f, viewportHeight / 2 - 120f), new Vector2f(3f));
        ball = new Ball(new Vector2f(viewportWidth / 2 - ballSize / 2, viewportHeight / 2 - ballSize), ballSize, ballSize);

        // model is a world-coordinate matrix, e.g. object placed at x = 100px, y = 200px
        Matrix4f model = new Matrix4f()
                .identity();

        ShaderContext.get().setUniformMatrix4fv("model", model);

        camera = new OrthographicCamera(0f, viewportWidth, viewportHeight, 0f); // 0.0 top-left
        ShaderContext.get().setUniformMatrix4fv("projectionView", camera.combined);
    }

    Random random = new Random();
    float xSpeed = 4f;
    float ySpeed = 6f;
    double dx = (random.nextInt(2) == 1 ? 1 : -1) * Math.cos(Math.toRadians(0)) * xSpeed;
    double dy = Math.sin(Math.toRadians(random.nextDouble(26d))) * ySpeed;
    Timer timer = new Timer();

    public void updatePhysics(double dt) {
        if (!isGameFinished) {
            if (ball.position.x <= 0) {
                handleScore(player2Score);
            } else if (ball.position.x + ballSize >= viewportWidth) {
                handleScore(player1Score);
            } if (player1.collisionBox.aabbCollide(ball.collisionBox)) {
                handlePaddleHit(player1);
            } else if (player2.collisionBox.aabbCollide(ball.collisionBox)) {
                handlePaddleHit(player2);
            }

            if (ball.position.y <= 0 || ball.position.y + ball.height > viewportHeight) {
                dy = -dy;
            }

            if (timer.isPassed()) {
                ball.position.x -= dx;
                ball.position.y -= dy;
            }

            ball.spriteRenderer.update();
            player1.spriteRenderer.update();
            player2.spriteRenderer.update();
        }
    }

    public void update(double dt) {
        processInput();
        camera.update();

        //draw sprites
        player1.draw(dt);
        player2.draw(dt);
        ball.draw(dt);
        player1Score.draw(dt);
        player2Score.draw(dt);

        if (isGameFinished) {
            winText.draw(dt);
        }
        glBindVertexArray(0);
    }

    private void handleScore(TextRenderer playerScore) {
        int newScore = Integer.parseInt(playerScore.text) + 1;

        if (newScore == 5) {
            isGameFinished = true;
            int winner = playerScore == player1Score ? 1 : 2;
            winText.update("Player " + winner + " won");
        }

        playerScore.update(newScore + "");
        ball.position.x = viewportWidth / 2 - ballSize / 2;
        ball.position.y = viewportHeight / 2 - ballSize;
        dx = (random.nextInt(2) == 1 ? 1 : -1) * Math.cos(Math.toRadians(0)) * xSpeed;
        dy = Math.sin(Math.toRadians(random.nextDouble(26d))) * ySpeed;
        timer.setTimer(1);
    }

    private void handlePaddleHit(Player player) {
        float normalizedHit = -((ball.position.y + ball.height / 2) - (player.position.y + (player.height / 2))) / (player.height / 2);
        double maxAngle = Math.toRadians(75);
        double bounceAngle = normalizedHit * maxAngle * 0.6;
        dx = -dx;
        dy = Math.sin(bounceAngle) * ySpeed;
    }

    public void dispose() {

    }

    public void processInput() {
        if (glfwGetKey(window.id, GLFW_KEY_W) == GLFW_PRESS && player1.position.y >= 0) {
            player1.position.y -= 1f;
        }
        if (glfwGetKey(window.id, GLFW_KEY_S) == GLFW_PRESS && player1.position.y + playerHeight <= viewportHeight) {
            player1.position.y += 1f;
        }

        if (glfwGetKey(window.id, GLFW_KEY_UP) == GLFW_PRESS && player2.position.y >= 0) {
            player2.position.y -= 1f;
        }
        if (glfwGetKey(window.id, GLFW_KEY_DOWN) == GLFW_PRESS && player2.position.y + playerHeight <= viewportHeight) {
            player2.position.y += 1f;
        }
    }
}
