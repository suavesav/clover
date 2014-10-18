package Entity;

/**
 * Created by Sav on 10/14/14.
 */

import TileMap.*;
import java.io.File;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Player extends MapObject {
    private int health;
    private int maxHealth;
    private int attack;
    private int maxAttack;
    private boolean dead;

    //Attack
    private boolean attacking;
    private int attackDamage;

    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {6};
    private static final int WALKING = 0;

    public Player(TileMap tm)
    {
        super(tm);
        //System.out.println("Player Constructor");
        //todo change width to 30
        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;

        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;

        facingRight = true;

        health = 20;
        maxHealth = 20;
        attack = 5000;
        maxAttack = 5000;
        attackDamage = 5;

        //Load Sprites
        try
        {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/player.png"));
            sprites = new ArrayList<BufferedImage[]>();
            BufferedImage[] anim = new BufferedImage[6];
            for(int i = 0; i < 6; i++)
            {
                anim[i] = spritesheet.getSubimage(0, i*height, width, height);
            }
            sprites.add(anim);
        }
        catch (Exception E)
        {
            System.out.println("Player Constructor Loading Animation Exception:");
            E.printStackTrace();
        }

        System.out.println(sprites.get(0).length);
        animation = new Animation();
        animation.setFrames(sprites.get(WALKING));
        animation.setDelay(400);
        System.out.println("Player Constructor DONE");
    }

    //Getters
    public int getHealth() {return health;}
    public int getMaxHealth() {return maxHealth;}
    public int getAttack() {return attack;}
    public int getMaxAttack() {return maxAttack;}


    //Setters
    public void setAttacking()
    {
        attacking = true;
    }

    //Update
    public void update()
    {
        //Update position
        //System.out.println("Getting Next Position...");
        getNextPosition();
        //System.out.println("Checking Tile Collision...");
        checkTileCollision();
        //System.out.println("Setting Position...");
        setPosition(xtemp, ytemp);

        //Movement Animation
        //System.out.println("Creating Animation");
        animation.setFrames(sprites.get(WALKING));
        animation.setDelay(40);
        width = 24;

        //System.out.println("Updating Animation");
        animation.update();

        //if(attacking)
        //{
            //todo Add attacking animation
        //}
        //todo add any other animations, jumping, falling, etc...

        if(right)
            facingRight = true;
        if(left)
            facingRight = false;
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
        else
        {
            if(dx > 0)
            {
                dx-=stopSpeed;
                if(dx < 0)
                    dx = 0;
            }
            else if(dx < 0)
            {
                dx+=stopSpeed;
                if(dx > 0)
                    dx = 0;
            }
        }

        if(jumping && !falling)
        {
            dy = jumpStart;
            falling = true;
        }

        if(falling)
        {
            if(dy > 0)
                jumping = false;

            if(dy > maxFallSpeed)
                dy = maxFallSpeed;
        }

    }

    public void draw(Graphics2D gr)
    {
        setMapPosition();
        //System.out.printf("%d, %d, %d, %d", (int)x, (int)xmap, (int)y, (int)ymap);
        if(facingRight)
            gr.drawImage(animation.getImage(),(int)(x + xmap - width/2), (int)(y + ymap - height/2), null);
        else
            gr.drawImage(animation.getImage(),(int)(x + xmap - width/2 + width), (int)(y + ymap - height/2), -width, height, null);
    }
}
