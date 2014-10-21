package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;

import Entity.Player;
import Entity.Enemy;
import Main.GamePanel;
import TileMap.*;
import java.util.ArrayList;

/**
 * Created by Sav on 10/13/14.
 */
public class Level1State extends GameState {

    private TileMap tileMap;
    private Background bg;
    private Player player;
    private ArrayList<Enemy> enemies;

    public Level1State(GameStateManager gsm)
    {
        this.gsm = gsm;
    }

    public void init()
    {
        tileMap = new TileMap(30);
        tileMap.loadTiles("/Tilesets/grasstileset.gif");
        tileMap.loadMap("/Maps/level1-1.map");
        tileMap.setPosition(0, 0);

        //TODO maybe change background - No back on level yet, figure out bug?
        bg = new Background("/Backgrounds/menubg.png", 0.1);
        player = new Player(tileMap);
        player.setPosition(100,100);
    }

    public void update()
    {

        try
        {
            //System.out.println("Updating Player");
            player.update();
            //System.out.println("Player Updated");
            tileMap.setPosition(GamePanel.WIDTH/2 - player.getx(), GamePanel.HEIGHT/2 - player.gety());
        }
        catch(Exception E)
        {
            System.out.println("Exception in updating the player");
            E.printStackTrace();
        }

    }

    public void draw(Graphics2D gr)
    {
        //Clear Screen
        gr.setColor(Color.WHITE);
        gr.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        //Draw BackGrounds
        //bg.draw(gr);

        try
        {
            //Draw Map
            tileMap.draw(gr);

            //Draw Player
            player.draw(gr);
        }
        catch (Exception E)
        {
            System.out.println("Exception while drawing things in Level1State: ");
            E.printStackTrace();
        }
    }

    public void keyPressed(int k)
    {
        if(k == KeyEvent.VK_LEFT)
            player.setLeft(true);
        if(k == KeyEvent.VK_RIGHT)
            player.setRight(true);
        if(k == KeyEvent.VK_UP)
            player.setJumping(true);
        if(k == KeyEvent.VK_LEFT)
            player.setDown(true);
        if(k == KeyEvent.VK_S)
            player.setAttacking();
    }

    public void keyReleased(int k)
    {
        if(k == KeyEvent.VK_LEFT)
            player.setLeft(false);
        if(k == KeyEvent.VK_RIGHT)
            player.setRight(false);
        if(k == KeyEvent.VK_UP)
            player.setJumping(false);
        if(k == KeyEvent.VK_LEFT)
            player.setDown(false);
    }
}
