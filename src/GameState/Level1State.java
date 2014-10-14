package GameState;

import java.awt.*;

import Main.GamePanel;
import TileMap.*;

/**
 * Created by Sav on 10/13/14.
 */
public class Level1State extends GameState {

    private TileMap tileMap;
    private Background bg;

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
    }

    public void update()
    {}

    public void draw(Graphics2D gr)
    {
        //Clear Screen
        gr.setColor(Color.WHITE);
        gr.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        //Draw BackGrounds
        //bg.draw(gr);

        //Draw Map
        tileMap.draw(gr);
    }

    public void keyPressed(int k)
    {}

    public void keyReleased(int k)
    {}
}
