package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import TileMap.Background;

import javax.swing.border.TitledBorder;

/**
 * Created by Sav on 10/12/14.
 */
public class MenuState extends GameState {

    private Background bg;

    private int currentChoice = 0;
    private String[] options = {"Start", "Help", "Quit"};

    private Color titleColor;
    private Font titleFont;
    private Font font;

    public MenuState(GameStateManager gsm)
    {
        this.gsm = gsm;

        try
        {
            bg = new Background("/Backgrounds/menubg.png", 1);
            bg.setVector(-0.1, 0);

            titleColor = new Color(128, 0, 0);
            titleFont = new Font("Century Gothic", Font.PLAIN, 28);
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
    {
        bg.update();
    }

    public void draw(Graphics2D gr)
    {
        bg.draw(gr);

        gr.setColor(titleColor);
        gr.setFont(titleFont);
        gr.drawString("Clover", 100, 70);

        gr.setFont(font);
        for(int i=0; i<options.length; i++)
        {
            if (i == currentChoice)
            {
                gr.setColor(Color.RED);
            }
            else
            {
                gr.setColor(Color.BLACK);
            }

            gr.drawString(options[i], 145, 140+(i*15));

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
            //Help
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
