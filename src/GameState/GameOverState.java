package GameState;

import Main.GamePanel;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Sav on 10/22/14.
 */
public class GameOverState extends GameState {
    private int currentChoice = 0;
    private String[] options = {"Reset", "Menu", "Quit"};

    private Font font;
    private Background bg;

    public GameOverState(GameStateManager gsm)
    {
        this.gsm = gsm;

        try
        {
            bg = new Background("/Backgrounds/gameover_bg.jpeg", 1);
            bg.setVector(-0.1, 0);

            font = new Font("Arial", Font.PLAIN, 12);
        }
        catch(Exception e)
        {
            System.out.println("Exception in Setting Background");
            e.printStackTrace();
        }
    }

    public void init()
    {}

    public void update()
    {}

    public void draw(Graphics2D gr)
    {
        //gr.setColor(Color.BLACK);
        //gr.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        bg.draw(gr);


        gr.setFont(font);
        for(int i=0; i<options.length; i++)
        {
            if (i == currentChoice)
            {
                gr.setColor(Color.RED);
            }
            else
            {
                gr.setColor(Color.YELLOW);
            }

            gr.drawString(options[i], 145, 200+(i*15));

        }
    }

    private  void select()
    {
        if(currentChoice == 0)
        {
            //Start
            gsm.setState(GameStateManager.LEVEL1STATE);
        }
        if (currentChoice == 1)
        {
            gsm.setState(GameStateManager.MENUSTATE);
        }
        if (currentChoice == 2)
        {
            //Quit
            System.exit(0);
        }
    }

    public void keyPressed(int k)
    {
        if(k == KeyEvent.VK_ENTER)
        {
            select();
        }
        if(k == KeyEvent.VK_UP)
        {
            currentChoice--;
            if (currentChoice == -1)
                currentChoice = options.length-1;
        }
        if(k == KeyEvent.VK_DOWN)
        {
            currentChoice++;
            if (currentChoice == options.length)
                currentChoice = 0;
        }
    }

    public void keyReleased(int k)
    {}
}
