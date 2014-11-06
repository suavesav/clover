package Entity;

import TileMap.TileMap;

import java.awt.*;

/**
 * Created by Sav on 11/6/14.
 */
public class PowerUp extends MapObject {

    private boolean used;

    public PowerUp(TileMap tm)
    {
        super(tm);
    }

    public boolean getUsed()
    {return used;}

    public void setUsed()
    {used = true;}

    public void update()
    {}

    public void draw(Graphics2D gr)
    {
        super.draw(gr);
    }
}
