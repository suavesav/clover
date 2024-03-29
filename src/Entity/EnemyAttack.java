package Entity;

import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Sav on 10/24/14.
 */
public class EnemyAttack extends MapObject{

    private boolean hit;
    private boolean remove;
    private BufferedImage[] sprites;

    public EnemyAttack(TileMap tm, boolean right)
    {
        super(tm);

        width = 30;
        height = 30;
        cwidth = 15;
        cheight = 15;

        facingRight = right;

        moveSpeed = 2.5;
        if(right)
            dx = moveSpeed;
        else
            dx = -moveSpeed;

        try
        {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/playerattack.png"));
            sprites = new BufferedImage[3];
            for(int i = 0; i<sprites.length; i++)
            {
                sprites[i] = spritesheet.getSubimage(i*width, 0, width, height);
            }

            animation = new Animation();
            animation.setFrames(sprites);
            animation.setDelay(70);

        }
        catch(Exception E)
        {
            System.out.println("Exception in loading attack animation");
            E.printStackTrace();
        }
    }

    public void setHit()
    {
        if(hit)
            return;
        hit = true;
        dx = 0;
    }

    public boolean getRemove() {return remove;}

    public void update()
    {

        checkTileCollision();
        setPosition(xtemp, ytemp);


        animation.update();

        if(dx == 0 && !hit)
            hit = true;

        if(hit && animation.getPlayed())
            remove = true;
    }

    public void draw(Graphics2D gr)
    {

        setMapPosition();
        super.draw(gr);

    }
}
