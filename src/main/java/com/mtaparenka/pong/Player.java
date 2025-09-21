package com.mtaparenka.pong;

import com.mtaparenka.engine.CollisionBox2D;
import com.mtaparenka.engine.render.SpriteRenderer;
import org.joml.Vector2f;

import static com.mtaparenka.pong.Constants.WHITE_COLOR;

public class Player {
    public Vector2f position;
    public float width;
    public float height;
    public SpriteRenderer spriteRenderer;
    public CollisionBox2D collisionBox;

    public Player(Vector2f position, float width, float height) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.collisionBox = new CollisionBox2D(position, width, height);

        spriteRenderer = SpriteRenderer.plainShape(WHITE_COLOR, position, width, height);
    }

    public void draw(double dt) {
        spriteRenderer.draw(dt);
    }
}
