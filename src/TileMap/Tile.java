package TileMap;

import java.awt.image.BufferedImage;

/**
 * Created by Sav on 10/14/14.
 */
public class Tile {
    private BufferedImage image;
    private int type;

    public static final int NORMAL = 0;
    public static final int BLOCKED = 1;
    public static final int EXPLODING = 2;
    public static final int GAS = 3;

    public Tile(BufferedImage image, int type)
    {
        this.image = image;
        this.type = type;
    }

    public BufferedImage getImage() {return image;}
    public int getType() {return type;}

}
