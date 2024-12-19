/*
 * Jimmy Pham
 * 3711704
 * COMP 452
 * Assignment 1
 */

import javax.swing.*;

public class SpaceWarGame extends JFrame {


    public SpaceWarGame() {
        setTitle("Space Wars");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        initializeGame();
    }

    private void initializeGame() {
        GamePanel gamePanel = new GamePanel();
        add(gamePanel);
    }


    public static void main(String[] args) {
        SpaceWarGame game = new SpaceWarGame();
        game.setVisible(true);
    }
}
