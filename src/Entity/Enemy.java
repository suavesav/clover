package Entity;

import TileMap.TileMap;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Sav on 10/21/14.
 */
public class Enemy extends MapObject {

    protected int health;
    protected int maxHealth;
    protected boolean dead;
    protected int damage;
    protected int attackDamage;
    protected boolean attacking;
    private long start;
    private long elapsed;


    private ArrayList<PlayerAttack> enemyAttack;

    public Enemy(TileMap tm) {
        super(tm);
        enemyAttack = new ArrayList<PlayerAttack>();
        start = System.nanoTime();
    }

    public boolean getDead() {
        return dead;
    }

    public int getDamage() {
        return damage;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttacking() {
        attacking = true;
    }

    public void hit(int damage) {
        if (dead)
            return;

        health -= damage;

        if (health < 0)
            health = 0;

        if (health == 0)
            dead = true;
    }

    public void checkAttack(Player player)
    {
        for (int j = 0; j < enemyAttack.size(); j++)
        {
            if (enemyAttack.get(j).intersects(player))
            {
                player.hit(attackDamage);
                enemyAttack.get(j).setHit();
                break;
            }
        }
    }

    public void update()
    {
        System.out.println("ENEMY UPDATE");

        elapsed = System.nanoTime() - start;
        System.out.println(elapsed);
        if (elapsed == 500)
            setAttacking();
        if (elapsed % 2000 == 0)
            setAttacking();

        if (attacking)
        {
            PlayerAttack ea = new PlayerAttack(tileMap, facingRight);
            ea.setPosition(x, y);
            enemyAttack.add(ea);
            attacking = false;
        }

        for (int i = 0; i < enemyAttack.size(); i++)
        {
            enemyAttack.get(i).update();
            if (enemyAttack.get(i).getRemove())
            {
                enemyAttack.remove(i);
                i--;
            }
        }

        animation.update();
    }

    public void draw(Graphics2D gr)
    {
        setMapPosition();
        for (int i = 0; i < enemyAttack.size(); i++)
        {
            enemyAttack.get(i).draw(gr);
            System.out.println("ENEMY ATTACK");

        }

//        System.out.printf("%d, %d, %d, %d\n", (int) x, (int) xmap, (int) y, (int) ymap);
        super.draw(gr);
    }
}
