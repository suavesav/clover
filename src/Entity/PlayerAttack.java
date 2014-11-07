package Entity;

import TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Created by Sav on 10/19/14.
 */
public class PlayerAttack extends MapObject{

    private boolean hit;
    private boolean remove;
    private boolean hitSoundPlayed;
    private BufferedImage[] sprites;

    public PlayerAttack(TileMap tm, boolean right)
    {
        super(tm);
        width = 30;
        height = 30;
        cwidth = 15;
        cheight = 15;

        facingRight = right;
        hitSoundPlayed = false;

        moveSpeed = 3.8;
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

    public boolean getHitSoundPlayed() {return hitSoundPlayed;}

    public void setHitSoundPlayed() {hitSoundPlayed = true;}

    public boolean getHit()
    {
        return hit;
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
