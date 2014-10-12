package Main;

import javax.swing.JFrame;

/**
 * Created by Sav on 10/12/14.
 */
public class Game {

    public static void main(String args[])
    {

        JFrame window = new JFrame("Clover");
        window.setContentPane(new GamePanel());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);
    }
}
