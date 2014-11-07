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


//    private ArrayList<PlayerAttack> enemyAttack;

    public Enemy(TileMap tm) {
        super(tm);
//        enemyAttack = new ArrayList<PlayerAttack>();
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

//    public void checkAttack(Player player)
//    {
//        for (int j = 0; j < enemyAttack.size(); j++)
//        {
//            if (enemyAttack.get(j).intersects(player))
//            {
//                player.hit(attackDamage);
//                enemyAttack.get(j).setHit();
//                break;
//            }
//        }
//    }

    public void update()
    {}

    public void draw(Graphics2D gr)
    {
        super.draw(gr);
    }
}
