package Entity;

import TileMap.TileMap;

/**
 * Created by Sav on 10/21/14.
 */
public class Enemy extends MapObject {

    protected int health;
    protected int maxHealth;
    protected boolean dead;
    protected int damage;
    protected int attackDamage;


    public Enemy(TileMap tm)
    {
        super(tm);
    }

    public boolean getDead() {return dead;}
    public int getDamage() {return damage;}
    public int getAttackDamage() {return attackDamage;}

    public void hit(int damage)
    {
        if(dead)
            return;

        health -= damage;

        if(health<0)
            health = 0;

        if(health == 0)
            dead = true;
    }
}
