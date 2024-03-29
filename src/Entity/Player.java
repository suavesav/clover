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
import GameState.Level1State;
import Audio.*;


public class Player extends MapObject {
    private int health;
    private int maxHealth;
    private int attack;
    private int maxAttack;
    private boolean dead;
    private int points;
    public long saStart;
    public long saElapsed;
    private int saCount;
    private boolean superAttackVar;
    private boolean invincible;



    //Attack
    private boolean attacking;
    private int attackDamage;
    public ArrayList<PlayerAttack> playerAttack; //To access playerAttack in Level1State

    private ArrayList<BufferedImage[]> sprites;
//    private final int[] numFrames = {2, 8};
    private final int[] numFrames = {6};
//    private static final int IDLE = 0;
    private static final int WALKING = 0;
    private boolean attackSoundPlayed;
    private long shoottimer;
    private int shootCounter;
    private long invincibilityTimer;
    private double invxcount;

    private long stillHealthTimer;
    private double xold;

    private boolean yellowBlock;
    private long yellowBlockTimer;
    private long yellowBlockPosDelta;

    public Player(TileMap tm)
    {
        super(tm);
        //System.out.println("Player Constructor");
        //todo change width to 24
        width = 24;
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
        maxHealth = 40;
        attack = 5000;
        maxAttack = 5000;
        attackDamage = 5;
        playerAttack = new ArrayList<PlayerAttack>();

        superAttackVar = false;
        saCount = 0;

        xcount = 0;
        attackSoundPlayed = true;
        shoottimer = System.nanoTime();
        shootCounter = 0;
        stillHealthTimer = System.nanoTime();
        xold = x;
        yellowBlock = false;


        //Load Sprites
        try
        {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/player.png"));
            sprites = new ArrayList<BufferedImage[]>();
            for(int i = 0; i < numFrames.length; i++)
            {
                BufferedImage[] anim = new BufferedImage[numFrames[i]];
                for(int j = 0; j < numFrames[i]; j++)
                {
                    anim[j] = spritesheet.getSubimage(i*width, j*height, width, height);
                }
                sprites.add(anim);
            }
        }
        catch (Exception E)
        {
            System.out.println("Player Constructor Loading Animation Exception:");
            E.printStackTrace();
        }

        //System.out.println(sprites.get(0).length);
        //System.out.println(sprites.get(1).length);
        animation = new Animation();
        animation.setFrames(sprites.get(WALKING));
        animation.setDelay(400);
        //System.out.println("Player Constructor DONE");
    }

    //Getters
    public int getHealth() {return health;}
    public int getMaxHealth() {return maxHealth;}
    public int getAttack() {return attack;}
    public int getMaxAttack() {return maxAttack;}
    public int getPoints() {return points;}


    public void setAttacking(boolean b) {attacking = b;}

    public void setAttackSoundPlayed()
    {
        attackSoundPlayed = true;
    }

    public boolean getAttackSoundPlayed() {return attackSoundPlayed;}

    public void setSuperAttack()
    {
        superAttackVar = true;
    }

    public boolean getSuperAttack()
    {
        return superAttackVar;
    }

    public void superAttack()
    {
        saElapsed = System.nanoTime() - saStart;
        saElapsed /= 1000000;

        if((saCount==0) || (saCount<10 && saElapsed>1000))
        {
            setAttacking(true);
            saCount++;
            saStart = System.nanoTime();
        }
        else if(saCount == 10)
        {
            saCount = 0;
            superAttackVar = false;
        }
    }

    public void checkAttack(ArrayList<Enemy> enemies)
    {
        for(int i = 0; i < enemies.size(); i++)
        {
            Enemy e = enemies.get(i);
            for(int j = 0; j < playerAttack.size(); j++)
            {
                if(playerAttack.get(j).intersects(e))
                {
                    e.hit(attackDamage);
                    //soundManager.play(hit);
                    playerAttack.get(j).setHit();
                    break;
                }
            }

            //Player collision with Enemy
            if(intersects(e) && !getInvincible())
                hit(e.getDamage());
        }
    }

    public void checkPowerUp(ArrayList<PowerUp> powerups)
    {
        for(int i = 0; i < powerups.size(); i++)
        {
            if(intersects(powerups.get(i)))
            {
                if(powerups.get(i).getType() == 1)
                    upHealth(5);
                else if(powerups.get(i).getType() == 2)
                    setInvincible();
                powerups.get(i).setUsed();
            }
        }
    }

    public void setInvincible()
    {
        invincible = true;
        invincibilityTimer = System.nanoTime();
    }

    public boolean getInvincible()
    {
        return invincible;
    }


    public void upHealth(int h)
    {
        health += h;
        if(health > maxHealth)
            health = maxHealth;
    }

    public void hit(int damage)
    {
        if(!getInvincible())
            health -= damage;

        if(health < 0)
            health = 0;

        if(health == 0)
            dead = true;
    }

    public void unsetShootCounter()
    {
        shootCounter = 0;
    }
    //Update
    public void update()
    {
        getNextPosition();
        checkTileCollision();
        //System.out.printf("Exploding:%b Gas:%b\n", isExploding, isGas);
        if(isExploding)
        {
            unsetIsExploding();
            hit(10);
        }

        if(isGas)
        {
            unsetIsGas();
            yellowBlock = true;
            yellowBlockTimer = System.nanoTime();
            yellowBlockPosDelta = 0;
        }
        setPosition(xtemp, ytemp);

        attack +=1;
        if(attack>maxAttack)
            attack = maxAttack;

        if(yellowBlock)
        {
            yellowBlockPosDelta += Math.abs(dx);
            if((System.nanoTime()-yellowBlockTimer)/1000000 > 1000)
                yellowBlock = false;
            if(yellowBlockPosDelta > 400)
                yellowBlock = false;
        }

        if(attacking&&!yellowBlock)
        {
            if(shootCounter == 10)
            {
                if(((System.nanoTime() - shoottimer)/1000000 > 1000))
                    shootCounter = 0;
            }
            else if(((System.nanoTime() - shoottimer)/1000000 > 500) && shootCounter<10)
            {
                shootCounter++;
                PlayerAttack pa = new PlayerAttack(tileMap, facingRight);
                pa.setPosition(x,y);
                playerAttack.add(pa);
                //attacking = false;
                attackSoundPlayed = false;
                shoottimer = System.nanoTime();
            }
        }

        if(invincible)
        {
            invxcount += Math.abs(xtemp - x);
            if(((System.nanoTime() - invincibilityTimer)/1000000 > 1000) || invxcount>400)
            {
                invincible = false;
                invxcount = 0;
            }
        }

        if(superAttackVar)
        {
            superAttack();
        }

        if((System.nanoTime() - stillHealthTimer)/1000000 > 1000)
        {
            if(xold == x)
            {
                upHealth(5);
            }
            xold = x;
            stillHealthTimer = System.nanoTime();
        }

        if(xcount>40 || xcount < -40)
        {
            xcount = 0;
            upHealth(1);
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

        if(left || right || jumping)
        {
            if(currAction != WALKING)
            {
                currAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(40);
                width = 24;
            }
        }

        //IDLE ANIMATION WAS HERE
        else
        {
            if(currAction != WALKING)
            {
                currAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(400);
                width = 24;
            }
        }

        animation.update();

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
            xcount -= dx;
            if (dx < -maxSpeed)
                dx = -maxSpeed;
        }
        else if(right)
        {
            dx+=moveSpeed;
            xcount+=dx;
            if(dx > maxSpeed)
                dx = maxSpeed;
        }
        else
        {
            if(dx > 0)
            {
                dx-=stopSpeed;
                xcount-=dx;
                if(dx < 0)
                    dx = 0;
            }
            else if(dx < 0)
            {
                dx+=stopSpeed;
                xcount  += dx;
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

        //System.out.printf("%d, %d, %d, %d\n", (int)x, (int)xmap, (int)y, (int)ymap);
        super.draw(gr);

    }

    public boolean getDead() {return dead;}
}
