/*
* Jimmy Pham
* 3711704
* COMP 452
* Assignment 1
*/

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputHandler extends KeyAdapter {
    private final Player player;

    public InputHandler(Player player) {
        this.player = player;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        player.move(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.move(e.getKeyCode(), false);
    }
}
