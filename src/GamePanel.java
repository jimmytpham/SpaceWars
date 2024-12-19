/*
 * Jimmy Pham
 * 3711704
 * COMP 452
 * Assignment 1
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel {
    private static Player player;
    private final ArrayList<Enemy> enemies; // List to hold enemies
    private boolean spawning = true;
    private Timer gameTimer;
    private Timer enemySpawnTimer;
    private int spawnInterval = 1000; // Initial spawn interval in milliseconds
    private int elapsedTime = 0; // Time elapsed in milliseconds
    private final Image backgroundImage;

    public GamePanel() {
        player = new Player(); // Initialize the player
        enemies = new ArrayList<>(); // Initialize the enemy list
        InputHandler inputHandler = new InputHandler(getPlayer());  // Create the InputHandler instance
        setFocusable(true);
        backgroundImage = new ImageIcon(getClass().getResource("/images/background.jpg")).getImage();
        addKeyListener(inputHandler);

        startGameTimers(); // Initialize and start game/enemy spawn timers
    }

    public Player getPlayer() {
        return player;
    }

    void spawnEnemy() {
        if (!spawning) return;
        int spawnX = 0, spawnY = 0;
        int side = (int) (Math.random() * 4); // Randomly choose 0, 1, 2, or 3 for each side

        spawnY = switch (side) {
            case 0 -> (int) (Math.random() * getHeight());
            case 1 -> {
                spawnX = getWidth();
                yield (int) (Math.random() * getHeight());
            }
            case 2 -> {
                spawnX = (int) (Math.random() * getWidth()); // Random x on the top
                yield 0;
            }
            case 3 -> {
                spawnX = (int) (Math.random() * getWidth()); // Random x on the bottom
                yield getHeight();
            }
            default -> spawnY;
        };

        enemies.add(new Enemy(spawnX, spawnY));
    }

    void update() {
        player.update(enemies);

        for (int i = player.bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = player.bullets.get(i);
            bullet.update();

            // Check if bullet hit enemy
            for (int j = enemies.size() - 1; j >= 0; j--) {
                Enemy enemy = enemies.get(j);
                if (bullet.hasHitEnemy(enemy)) {
                    enemies.remove(enemy);
                    player.bullets.remove(bullet);
                    break;
                }
            }
        }

        // Update each enemy's position
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            enemy.update(player.getX(), player.getY(), enemies);

            // Check if enemy has touched player
            if (isColliding(player, enemy)) {
                player.increaseTouchCount();
                enemies.remove(enemy);

                // Game over and Reset
                if (player.isGameOver()) {
                    player.resetMovement();
                    enemies.clear();
                    spawning = false;
                    gameOver();
                }
                break;
            }
        }

        // Reduce the spawn interval by 1s every 10s
        elapsedTime += 16; // Increment time by frame duration (~16 ms per frame)
        if (elapsedTime >= 10000) { // Every 10 seconds
            spawnInterval = Math.max(1000, spawnInterval - 1000); // Minimum spawn interval of 1 second
            elapsedTime = 0;
            enemySpawnTimer.setDelay(spawnInterval);
        }
    }

    // Enemy hits player if it is within certain range
    private boolean isColliding(Player player, Enemy enemy) {
        int playerWidth = 30, playerHeight = 30;
        int enemyWidth = 30, enemyHeight = 30;

        return player.getX() < enemy.getX() + enemyWidth &&
                player.getX() + playerWidth > enemy.getX() &&
                player.getY() < enemy.getY() + enemyHeight &&
                player.getY() + playerHeight > enemy.getY();
    }

    private void startGameTimers() {
        gameTimer = new Timer(16, _ -> {
            update();
            repaint();
        });
        gameTimer.start();

        enemySpawnTimer = new Timer(spawnInterval, _ -> spawnEnemy());
        enemySpawnTimer.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        int lives = player.getLives();

        // lives in top right corner
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Lives: " + lives, getWidth() - 150, 30);
        player.draw(g);

        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }
    }

    public void reset() {
        // Reset player
        player.reset();

        // Clear enemies and bullets
        enemies.clear();
        player.bullets.clear();

        // Reset game variables
        spawnInterval = 1000;
        elapsedTime = 0;

        // Restart timers
        if (gameTimer != null) {
            gameTimer.restart();
        }

        // Restart enemy spawning
        spawning = true;

        // Restart timers
        if (enemySpawnTimer != null) {
            enemySpawnTimer.stop(); // Stop the old timer
            enemySpawnTimer = new Timer(spawnInterval, _ -> spawnEnemy());
            enemySpawnTimer.start(); // Start a new timer with the base interval
        }

        repaint();
        System.out.println("Game reset!");
    }

    public void gameOver() {
        // Game over pop up
        JFrame frame = new JFrame("Game Over");
        frame.setSize(300, 150);
        frame.setLayout(new BorderLayout());

        JLabel message = new JLabel("Game Over! Would you like to play again?", SwingConstants.CENTER);
        frame.add(message, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton playAgainButton = new JButton("Yes");
        JButton quitButton = new JButton("Quit");

        // Play again button
        playAgainButton.addActionListener(_ -> {
            reset();
            addKeyListener(new InputHandler(getPlayer()));
            frame.dispose();
        });

        // Quit button
        quitButton.addActionListener(_ -> {
            frame.dispose();
            System.exit(0);
        });

        buttonPanel.add(playAgainButton);
        buttonPanel.add(quitButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
