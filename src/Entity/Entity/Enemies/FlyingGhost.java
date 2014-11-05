package Entity.Entity.Enemies;

import Entity.*;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Sav on 10/21/14.
 */
public class FlyingGhost extends Enemy {

    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {5, 5, 8};

    private static final int MOVING = 0;
    private static final int ATTACKING = 1;
    private static final int DEAD = 2;

    public FlyingGhost(TileMap tm)
    {
        super(tm);

        moveSpeed = 0.3;
        maxSpeed = 0.9;
        fallSpeed = 0.3;
        maxFallSpeed = 10.0;
        width = 30;
        height = 30;
        cwidth = 10;
        cheight = 10;

        health = 10;
        maxHealth = 10;
        damage = 40;
        attackDamage = 5;

        try
        {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/mon3_ani/mon3_sprite_base.png"));
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
            System.out.println("Error getting flying ghost sprites:");
            E.printStackTrace();
        }

        System.out.println(sprites.get(0).length);
        //System.out.println(sprites.get(1).length);
        animation = new Animation();
        animation.setFrames(sprites.get(MOVING));
        animation.setDelay(400);
        System.out.println("Player Constructor DONE");

        right = true;
        facingRight = true;
    }

    private void getNextPosition()
    {
        if(left)
        {
            dx-=moveSpeed;
            if (dx < -maxSpeed)
                dx = -maxSpeed;
        }
        else if(right)
        {
            dx+=moveSpeed;
            if(dx > maxSpeed)
                dx = maxSpeed;
        }

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

        if(right && dx==0)
        {
            right = false;
            left = true;
            facingRight = false;
        }
        else if(left && dx==0)
        {
            left = false;
            right = true;
            facingRight = true;
        }

        if(currAction == DEAD)
            if(animation.getPlayed())
                dead = false;
        if(currAction == ATTACKING)
            if(animation.getPlayed())
                attacking = false;

        if(attacking && !dead)
        {
            if(currAction != ATTACKING)
            {
                currAction = ATTACKING;
                animation.setFrames(sprites.get(ATTACKING));
                animation.setDelay(40);
                width = 30;
            }
        }
        else if(dead)
        {
            if(currAction != DEAD)
            {
                currAction = DEAD;
                animation.setFrames(sprites.get(DEAD));
                animation.setDelay(40);
                width = 30;
            }
        }
        else
        {
            if(currAction != MOVING)
            {
                currAction = MOVING;
                animation.setFrames(sprites.get(MOVING));
                animation.setDelay(20);
                width = 30;
            }
        }
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
