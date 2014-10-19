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
    private ArrayList<PlayerAttack> playerAttack;

    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {2, 8};
    private static final int IDLE = 0;
    private static final int WALKING = 1;

    public Player(TileMap tm)
    {
        super(tm);
        //System.out.println("Player Constructor");
        //todo change width to 24
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
        falling = true;

        facingRight = true;

        health = 20;
        maxHealth = 20;
        attack = 5000;
        maxAttack = 5000;
        attackDamage = 5;
        playerAttack = new ArrayList<PlayerAttack>();

        //Load Sprites
        try
        {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/playersprites.gif"));
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
        catch (Exception E)
        {
            System.out.println("Player Constructor Loading Animation Exception:");
            E.printStackTrace();
        }

        System.out.println(sprites.get(0).length);
        System.out.println(sprites.get(1).length);
        animation = new Animation();
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(400);
        System.out.println("Player Constructor DONE");
    }

    //Getters
    public int getHealth() {return health;}
    public int getMaxHealth() {return maxHealth;}
    public int getAttack() {return attack;}
    public int getMaxAttack() {return maxAttack;}


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

        attack +=1;
        if(attack>maxAttack)
            attack = maxAttack;

        if(attacking)
        {
            PlayerAttack pa = new PlayerAttack(tileMap, facingRight);
            pa.setPosition(x,y);
            playerAttack.add(pa);
            attacking = false;
        }

        for(int i = 0; i < playerAttack.size(); i++)
        {
            playerAttack.get(i).update();
            if(playerAttack.get(i).getRemove())
            {
                playerAttack.remove(i);
                i--;
            }
        }
        //Movement Animation
        //System.out.println("Creating Animation");
        if(left || right || jumping)
        {
            if(currAction != WALKING)
            {
                currAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(40);
                width = 30;
            }
        }
        else
        {
            if(currAction != IDLE)
            {
                currAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(400);
                width = 30;
            }
        }
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
            dy += fallSpeed;
            if(dy > 0)
                jumping = false;

            if(dy > maxFallSpeed)
                dy = maxFallSpeed;
        }

    }

    public void draw(Graphics2D gr)
    {

        setMapPosition();
        for(int i = 0; i<playerAttack.size(); i++)
        {
            playerAttack.get(i).draw(gr);
        }
        System.out.printf("%d, %d, %d, %d\n", (int)x, (int)xmap, (int)y, (int)ymap);
        if(facingRight)
            gr.drawImage(animation.getImage(),(int)(x + xmap - width/2), (int)(y + ymap - height/2), null);
        else
            gr.drawImage(animation.getImage(),(int)(x + xmap - width/2 + width), (int)(y + ymap - height/2),
                    -width, height, null);

    }
}
