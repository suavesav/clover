package TileMap;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
/**
 * Created by Sav on 10/12/14.
 */
public class Background {

    private BufferedImage image;

    private double x;
    private double y;
    private double dx;
    private double dy;
    private double moveScale;

    public Background(String s, double ms)
    {
        //This is how you import image files into the game
        try
        {
            image = ImageIO.read(getClass().getResourceAsStream(s));
            moveScale = ms;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setPosition(double x, double y)
    {
        //Don't want it to go off the screen, want it to reset if it does
        this.x = (x * moveScale) % GamePanel.WIDTH;
        this.y = (y * moveScale) * GamePanel.HEIGHT;
    }

    public void setVector(double dx, double dy)
    {
        this.dx = dx;
        this.dy = dy;
    }

    public void update()
    {
        x += dx;
        y += dy;
    }

    public void draw(Graphics2D gr)
    {
        gr.drawImage(image, (int)x, (int)y, null);

        //Because scrolling background
        if(x < 0)
        {
            gr.drawImage(image, (int)x + GamePanel.WIDTH, (int)y, null);
        }

        if(x > 0)
        {
            gr.drawImage(image, (int)x - GamePanel.WIDTH, (int)y, null);
        }
    }
}
