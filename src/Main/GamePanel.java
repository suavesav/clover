package Main;

/**
 * Created by Sav on 10/12/14.
 */

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

public class GamePanel extends JPanel implements Runnable, KeyListener
{

    //Game Dimensions
    public static final int WIDTH = 320;
    public static final int HEIGHT = 240;
    public static final int SCALE = 2;

    //Game Thread
    private Thread thread;
    private boolean running;
    private int FPS = 60;
    private long targetTime = 1000 / FPS;

    //Game Canvas
    private BufferedImage image;
    private Graphics2D gr;

    //Game Panel
    public GamePanel()
    {
        super();
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setFocusable(true);
        requestFocus();
    }

    //Notify me that the game is ready to go
    public void addNotify()
    {
        super.addNotify();
        if(thread == null)
        {
            thread = new Thread(this);
            addKeyListener(this);
            thread.start();
        }
    }

    //Will initialize everything
    private void init()
    {
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
        gr = (Graphics2D) gr;
        running = true;
    }

    //
    public void run()
    {
        init();

        //Timers
        long start;
        long elapsed;
        long wait;

        //Game Loop
        while(running)
        {
            start = System.nanoTime();

            update();
            draw();
            drawToScreen();

            elapsed = System.nanoTime() - start;
            //targetTime is in ns but elapsed is in ms so /1000000
            wait = targetTime - elapsed/1000000;

            try
            {
                Thread.sleep(wait);
            }
            catch (Exception e)
            {
                //Black screen without this line ALWAYS DO THIS WHEN CATCHING EXCEPTIONS
                e.printStackTrace();
            }
        }
    }

    private void update()
    {}

    private void draw()
    {}

    private void drawToScreen()
    {
        Graphics gr2 = getGraphics();
        gr2.drawImage(image, 0, 0, null);
        gr2.dispose();
    }

    public void keyTyped(KeyEvent key)
    {}

    public void keyPressed(KeyEvent key)
    {}

    public void keyReleased(KeyEvent key)
    {}

}
