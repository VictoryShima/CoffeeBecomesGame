package com.md.mechevo.game;

public class Obstacle extends Solid implements CollisionVisitor {
    public Obstacle(float width, float height, Coordinate initialCoordinate) {
        super(width, height, initialCoordinate);
    }

    @Override
    public void collidesWith(Player p) {

    }

    @Override
    public void collidesWith(Projectile p) {

    }

    @Override
    public void collidesWith(Obstacle o) {

    }

    @Override
    public void collidesWith(Sentry s) {

    }

    @Override
    void accept(CollisionVisitor s) {

    }
}
