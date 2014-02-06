package com.md.mechevo.game.projectile;

import com.md.mechevo.game.*;
import com.md.mechevo.game.sentry.Sentry;

public abstract class Projectile extends Solid implements CollisionVisitor {
	protected Projectile(Position position, float width, float height, float speed, float angle) {
		super(position, width, height, speed, angle);
	}

	@Override
	public abstract void collidesWith(Player p);

	@Override
	public abstract void collidesWith(Projectile p);

	@Override
	public abstract void collidesWith(Obstacle o);

	@Override
	public abstract void collidesWith(Sentry s);

	@Override
	public void accept(CollisionVisitor s) {
		s.collidesWith(this);
	}

	public void play(State state) {

	}
}