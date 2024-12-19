/*
 * Jimmy Pham
 * 3711704
 * COMP 452
 * Assignment 1
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Enemy {
    private int x, y;
    private final Image enemgyImage;

    public Enemy(int startX, int startY) {
        this.x = startX;
        this.y = startY;

        //Load enemy image
        this.enemgyImage = new ImageIcon(getClass().getResource("/images/enemyship.png")).getImage();
    }

    public void seek(int playerX, int playerY) {
        // Seek towards the player
        // Movement speed of the enemy
        int speed = 2;
        if (x < playerX) {
            x += speed;
        } else {
            x -= speed;
        }
        if (y < playerY){
            y += speed;
        } else{
            y -= speed;
        }
    }

    public void update(int playerX, int playerY, ArrayList<Enemy> enemies) {
        seek(playerX, playerY);

        // Enemies cannot overlap
        for (Enemy other : enemies) {
            if (other != this && isOverlapping(other)) {
                adjustPosition(other);
            }
        }
    }

    public void draw(Graphics g) {
        g.drawImage(enemgyImage, x, y, null);
    }

    public Rectangle getBounds() {
        // Size of the enemy
        int size = 40;
        return new Rectangle(x, y, size, size);
    }

    private boolean isOverlapping(Enemy other) {
        int width = 50;
        int height = 50;

        return x < other.x + width && x + width > other.x &&
                y < other.y + height && y + height > other.y;
    }

    private void adjustPosition(Enemy other) {
        // Adjust enemy position if overlapping with another enemy
        double deltaX = other.x - x;
        double deltaY = other.y - y;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        // Minimum distance to maintain
        double minDistance = 50;
        if (distance < minDistance) {
            double overlap = minDistance - distance;
            if (distance != 0) {
                deltaX /= distance;
                deltaY /= distance;
                // Move this enemy away from the other enemy by the minDistance
                x -= (int) (deltaX * overlap / 2);
                y -= (int) (deltaY * overlap / 2);
                other.x += (int) (deltaX * overlap / 2);
                other.y += (int) (deltaY * overlap / 2);
            }
        }
    }

    public int getX() {
        return x; // Return the enemy's x position
    }

    public int getY() {
        return y; // Return the enemy's y position
    }
}
