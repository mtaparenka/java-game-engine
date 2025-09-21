package com.mtaparenka.engine;

import org.joml.Vector2f;

public class CollisionBox2D {
    public Vector2f position;
    public float width;
    public float height;

    public CollisionBox2D(Vector2f position, float width, float height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }

    public boolean aabbCollide(CollisionBox2D collisionBox) {
        boolean collisionX = position.x + width >= collisionBox.position.x
                && collisionBox.position.x + collisionBox.width >= position.x;

        boolean collisionY = position.y + height >= collisionBox.position.y
                && collisionBox.position.y + collisionBox.width >= position.y;

        return collisionX && collisionY;
    }
}
