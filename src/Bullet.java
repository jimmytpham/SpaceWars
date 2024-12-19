/*
 * Jimmy Pham
 * 3711704
 * COMP 452
 * Assignment 1
 */

import java.awt.*;

public class Bullet {
    private int x, y;
    private final double directionX, directionY;

    public Bullet(int startX, int startY, int targetX, int targetY) {
        this.x = startX;
        this.y = startY;

        // difference between enemy and player x coordinates
        double deltaX = targetX - startX;
        //difference between enemy and player y coordinates
        double deltaY = targetY - startY;
        // total distance and vector between player and enemy
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        this.directionX = deltaX / distance;
        this.directionY = deltaY / distance;
    }


    public void update() {
        int speed = 10;
        x += (int) (speed * directionX);
        y += (int) (speed * directionY);
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, 5, 10);
    }

    public int getY() {
        return y;
    }

    public int getX(){
        return x;
    }

    public boolean hasHitEnemy(Enemy enemy) {
        // Check if the bullet has hit an enemy
        Rectangle bulletRect = new Rectangle(x, y, 5, 10);
        return bulletRect.intersects(enemy.getBounds());
    }
}
