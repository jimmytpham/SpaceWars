/*
 * Jimmy Pham
 * 3711704
 * COMP 452
 * Assignment 1
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.geom.AffineTransform;

public class Player {
    private int x, y;
    private boolean movingUp, movingDown, movingLeft, movingRight;
    ArrayList<Bullet> bullets;
    private long lastBulletTime;
    private int touchCount = 0;
    private static final int MAX_TOUCHES = 3;
    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    private int shipDirection = UP;
    private final Image playerImage;


    public Player() {
        this.x = 400; // Starting position
        this.y = 300;
        bullets = new ArrayList<>();
        lastBulletTime = System.currentTimeMillis();
        // Load the player image
        this.playerImage = new ImageIcon(getClass().getResource("/images/playership.png")).getImage();
    }

    public void move(int keyCode, boolean pressed) {
        // ship graphic directions
        switch (keyCode) {
            case KeyEvent.VK_W:
                movingUp = pressed;
                if (pressed) shipDirection = UP;
                break;
            case KeyEvent.VK_S:
                movingDown = pressed;
                if (pressed) shipDirection = DOWN;
                break;
            case KeyEvent.VK_A:
                movingLeft = pressed;
                if (pressed) shipDirection = LEFT;
                break;
            case KeyEvent.VK_D:
                movingRight = pressed;
                if (pressed) shipDirection = RIGHT;
                break;
        }
    }

    public void update(ArrayList<Enemy> enemies) {
        // Player graphic movement directions
        int speed = 5;
        if (movingUp && y > 0) y -= speed;
        if (movingDown && y < 600 - 50) y += speed;
        if (movingLeft && x > 0) x -= speed;
        if (movingRight && x < 800 - 50) x += speed;

        // Player can shoot when staying still
        long currentTime = System.currentTimeMillis();
        if (!movingUp && !movingDown && !movingLeft && !movingRight) {
            // shooting reload timer
            int bulletCooldown = 500;
            if (currentTime - lastBulletTime >= bulletCooldown) {
                shoot(enemies);
                lastBulletTime = currentTime;
            }
        }

        // Update bullets
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update();

            // Remove bullet if it goes off-screen
            if (bullet.getY() < 0) {
                bullets.remove(i);
            }
        }
    }

    public void shoot(ArrayList<Enemy> enemies) {
        if (enemies.isEmpty()) return; // No enemies to shoot at

        // Find the closest enemy
        Enemy closestEnemy = null;
        double closestDistance = Double.MAX_VALUE;

        for (Enemy enemy : enemies) {
            double distance = Math.sqrt(Math.pow(enemy.getX() - this.x, 2) + Math.pow(enemy.getY() - this.y, 2));
            if (distance < closestDistance) {
                closestDistance = distance;
                closestEnemy = enemy;
            }
        }

        // Shoot at closest enemy
        if (closestEnemy != null) {
            bullets.add(new Bullet(x + 25, y, closestEnemy.getX(), closestEnemy.getY()));
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();

        // Rotate player ship graphics
        double rotationAngle = switch (shipDirection) {
            case RIGHT -> Math.PI / 2;
            case DOWN -> Math.PI;
            case LEFT -> 3 * Math.PI / 2;
            default -> 0.0;     // Default facing up
        };
        int centerX = x + 25;
        int centerY = y + 25;
        g2d.rotate(rotationAngle, centerX, centerY);

        // Player ship graphics
        g2d.drawImage(playerImage, x, y, 50, 50, null);
        g2d.setTransform(originalTransform);

        // Draw bullets
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
    }

    public int getX() {
        return x; // Return the player's x position
    }

    public int getY() {
        return y; // Return the player's y position
    }

    // Method to check if the game is over
    public boolean isGameOver() {
        return touchCount >= MAX_TOUCHES;
    }

    public void reset(){
        this.x = 400; // Starting position
        this.y = 300;
        // Reset lives
        this.touchCount = 0;
        // Clear all bullets
        this.bullets.clear();
    }
    public void resetMovement(){
        movingUp = false;
        movingDown = false;
        movingLeft = false;
        movingRight = false;
    }

    // keep count of lives
    public void increaseTouchCount() {
        touchCount++;
        isGameOver();
    }
    public int getLives(){
        return 3 - touchCount;
    }




}
