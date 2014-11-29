package Entity.Entity.PowerUps;

import Entity.Animation;
import Entity.PowerUp;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Sav on 11/6/14.
 */
public class Star extends PowerUp
{
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {1};

    public Star(TileMap tm)
    {
        super(tm);

        fallSpeed = 2.0;
        maxFallSpeed = 10.0;
        width = 30;
        height = 30;
        cwidth = 10;
        cheight = 10;
        setType(2);

        try
        {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Powerups/star.png"));
            sprites = new ArrayList<BufferedImage[]>();

            for(int i = 0; i < numFrames.length; i++)
            {
                BufferedImage[] anim = new BufferedImage[numFrames[i]];
                for(int j = 0; j < numFrames[i]; j++)
                {
                    anim[j] = spritesheet.getSubimage(j*width, i*height, width, height);
                }
                sprites.add(anim);
            }
        }
        catch(Exception E)
        {
            System.out.println("Error getting mushroom sprites:");
            E.printStackTrace();
        }

        System.out.println(sprites.get(0).length);
        //System.out.println(sprites.get(1).length);
        animation = new Animation();
        animation.setFrames(sprites.get(0));
        animation.setDelay(400);
        System.out.println("Player Constructor DONE");

    }

    private void getNextPosition()
    {
        if(falling)
        {
            dy+=fallSpeed;
            if(dy>maxFallSpeed)
                dy = maxFallSpeed;
        }
    }

    public void update()
    {
        getNextPosition();
        checkTileCollision();
        setPosition(xtemp, ytemp);

        animation.update();
    }
    public void draw(Graphics2D gr)
    {
        if(objectNotOnScreen())
            return;

        setMapPosition();
        super.draw(gr);
    }
}
