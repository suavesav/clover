package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;

import Audio.*;
import Entity.Entity.Enemies.FlyingGhost;
import Entity.Player;
import Entity.Enemy;
import Entity.PowerUp;
import Entity.Entity.PowerUps.Mushroom;
import Main.GamePanel;
import TileMap.*;
import java.util.ArrayList;
import Entity.HUD;
import javax.sound.sampled.AudioFormat;

/**
 * Created by Sav on 10/13/14.
 */
public class Level1State extends GameState {

    private TileMap tileMap;
    private Background bg;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<PowerUp> powerups;
    private HUD hud;
    private long skeystart;
    private long skeyelapsed;

    //SOUNDS
    // uncompressed, 44100Hz, 16-bit, mono, signed, little-endian
    private static final AudioFormat PLAYBACK_FORMAT = new AudioFormat(44100, 16, 1, true, false);
    private static final int MANY_SOUNDS_COUNT = SoundManager.getMaxSimultaneousSounds(PLAYBACK_FORMAT);
    public SoundManager soundManager;
    public Sound fire;
    private Sound die; //accessed from level1 state
    public Sound hit;

    public Level1State(GameStateManager gsm)
    {
        this.gsm = gsm;
    }

    public void init()
    {
        tileMap = new TileMap(30);
        tileMap.loadTiles("/Tilesets/grasstileset.gif");
        tileMap.loadMap("/Maps/level1-2.map");
        tileMap.setPosition(0, 0);

        //TODO maybe change background - No back on level yet, figure out bug?
        bg = new Background("/Backgrounds/menubg.png", 0.1);
        player = new Player(tileMap);
        player.setPosition(100,100);

        populateEnemies();
        populatePowerUps();

        hud = new HUD(player);
//        start = System.nanoTime();
        initSounds(); //initialize sounds stuff
    }
    public void initSounds() { //initialize sound variables
        soundManager = new SoundManager(PLAYBACK_FORMAT);
        fire = soundManager.getSound("./Resources/Sounds/fire2.wav");
        die = soundManager.getSound("./Resources/Sounds/jumpsound.aiff");
        hit = soundManager.getSound("./Resources/Sounds/hit.wav");
    }

    private void populatePowerUps()
    {
        powerups = new ArrayList<PowerUp>();

        Mushroom mush;
        mush = new Mushroom(tileMap);
        mush.setPosition(220, 100);
        powerups.add(mush);
    }
    private void populateEnemies()
    {
        enemies = new ArrayList<Enemy>();

        FlyingGhost fg;
        fg = new FlyingGhost(tileMap);
        fg.setPosition(200, 100);
        enemies.add(fg);

        FlyingGhost fg2;
        fg2 = new FlyingGhost(tileMap);
        fg2.setPosition(823, 100);
        enemies.add(fg2);
    }

    public void update()
    {
        try
        {
//            elapsed = System.nanoTime() - start;
//            System.out.println(elapsed);
            //System.out.println("Updating Player");
            player.update();
            //System.out.println("Player Updated");
            tileMap.setPosition(GamePanel.WIDTH/2 - player.getx(), GamePanel.HEIGHT/2 - player.gety());

            for(int i = 0; i<enemies.size(); i++)
            {
                Enemy e = enemies.get(i);
                e.update();
                if(e.getDead())
                {
                    soundManager.play(die);
                    enemies.remove(i);
                    i--;
                    player.upHealth(10);
                }
                enemies.get(i).checkAttack(player);


            }
            for(int j = 0; j<powerups.size(); j++)
            {
                powerups.get(j).update();
                if(powerups.get(j).getUsed())
                {
                    powerups.remove(j);
                    j--;
                }
            }
            player.checkPowerUp(powerups);
            player.checkAttack(enemies);
            for(int i = 0; i<player.playerAttack.size(); i++)
            {
                if(player.playerAttack.get(i).getHit() == true)
                    soundManager.play(hit);
            }
            if(!player.getAttackSoundPlayed())
            {
                soundManager.play(fire);
                player.setAttackSoundPlayed();
            }


            if(player.getDead())
                gsm.setState(GameStateManager.GAMEOVERSTATE);
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
        gr.setColor(Color.BLACK);
        gr.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        //Draw BackGrounds
        //bg.draw(gr);

        try
        {
            //Draw Map
            tileMap.draw(gr);

            //Draw Player
            player.draw(gr);

            for(int i = 0; i<enemies.size(); i++)
            {
                enemies.get(i).draw(gr);
            }

            for(int i = 0; i<powerups.size(); i++)
            {
                powerups.get(i).draw(gr);
            }

            hud.draw(gr);
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
        if(k == KeyEvent.VK_S && !player.getSuperAttack()) {
            player.setAttacking();
            skeystart = System.nanoTime();
        }
        if(k == KeyEvent.VK_R)
            gsm.setState(GameStateManager.LEVEL1STATE);
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
        if(k == KeyEvent.VK_S)
        {
            skeyelapsed = System.nanoTime() - skeystart;
            skeyelapsed /= 1000000;
            if(skeyelapsed > 2000)
            {
                player.setSuperAttack();
//                System.out.println("SuperAttack is true");
            }
        }
    }
}
