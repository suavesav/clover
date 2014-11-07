package Entity;

/**
 * Created by Sav on 10/14/14.
 */
import Main.GamePanel;
import TileMap.TileMap;
import TileMap.Tile;
import java.awt.*;


//THIS IS THE BASE CLASS FOR ALL OBJECTS IN THE GAME
public abstract class MapObject {
    protected TileMap tileMap;
    protected int tileSize;
    protected double xmap;
    protected double ymap;

    protected double x;
    protected double y;
    protected double dx;
    protected double dy;

    //Reading Sprites
    protected int width;
    protected int height;
    //Collision Detection
    protected int cwidth, cheight;
    protected int currRow, currCol;
    protected double xdest, ydest;
    protected double xtemp, ytemp;
    protected double xcount, ycount;
    protected boolean topLeft, topRight, bottomLeft, bottomRight;

    //Animation
    protected Animation animation;
    protected int currAction;
    protected int prevAction;
    protected boolean facingRight;

    //Movement
    protected boolean up, down, left, right;
    protected boolean jumping, falling;
    protected double moveSpeed;
    protected double maxSpeed;
    protected double stopSpeed;
    protected double fallSpeed;
    protected double maxFallSpeed;
    protected double jumpStart;

    public MapObject(TileMap tm)
    {
        tileMap = tm;
        tileSize = tm.getTileSize();
    }

    public boolean intersects(MapObject mo)
    {
        Rectangle r1 = getRectangle();
        Rectangle r2 = mo.getRectangle();

        return r1.intersects(r2);
    }

    public Rectangle getRectangle()
    {
        return new Rectangle((int)x - cwidth, (int)y - cheight, cwidth, cheight);
    }

    public void calculateCorners(double x, double y)
    {
        int leftTile = (int)(x - cwidth / 2) / tileSize;
        int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
        int topTile = (int)(y - cheight / 2) / tileSize;
        int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;

        //Avoid out of bounds

        if(topTile < 0 || bottomTile >= tileMap.getNumRows() || leftTile < 0 || rightTile >= tileMap.getNumCols())
        {
            topLeft = topRight = bottomLeft = bottomRight = false;
            return;
        }


        //Getting the types of the tiles to see if it would be special
        int tl = tileMap.getType(topTile, leftTile);
        int tr = tileMap.getType(topTile, rightTile);
        int bl = tileMap.getType(bottomTile, leftTile);
        int br = tileMap.getType(bottomTile, rightTile);

        //See if the tile is blocked and act accordingly
        topLeft = tl==Tile.BLOCKED;
        topRight = tr==Tile.BLOCKED;
        bottomLeft = bl==Tile.BLOCKED;
        bottomRight = br==Tile.BLOCKED;
    }

    public void checkTileCollision()
    {
        currCol = (int)x/tileSize;
        currRow = (int)y/tileSize;

        //Future values of x and y
        xdest = x + dx;
        ydest = y + dy;

        //Need to keep track of old values of x and y
        xtemp = x;
        ytemp = y;

        //See if down is blocked
        calculateCorners(x, ydest);
        //See if up is blocked
        if(dy < 0)
        {
            if(topLeft || topRight)
            {
                //Block Player/Whatever Object from moving up
                System.out.println("BLOCKED DETECTED");
                dy=0;
                ytemp = currRow * tileSize + cheight / 2;
            }
            else
                ytemp += dy;
        }

        if(dy > 0)
        {
            if(bottomLeft || bottomRight)
            {
                //Block Player/Whatever Object from moving down
                System.out.println("BLOCKED DETECTED");
                dy=0;
                falling = false;
                ytemp = (currRow+1) * tileSize - cheight / 2;
            }
            else
                ytemp += dy;
        }

        calculateCorners(xdest, y);
        if(dx < 0)
        {
            if(topLeft || bottomLeft)
            {
//                System.out.println("BLOCKED DETECTED");
                dx=0;
                xtemp = currCol * tileSize + cwidth / 2;
            }
            else
                xtemp += dx;
        }

        if(dx > 0)
        {
            if(topRight || bottomRight)
            {
                //Block Player/Whatever Object from moving up
//                System.out.println("BLOCKED DETECTED");
                dx=0;
                xtemp = (currCol+1) * tileSize - cwidth / 2;
            }
            else
                xtemp += dx;
        }

        if(!falling)
        {
            calculateCorners(x, ydest+1);
            if(!bottomLeft && !bottomRight)
                falling = true;
        }
    }

    public boolean objectNotOnScreen()
    {
        return x+xmap+width < 0 || x+xmap-width > GamePanel.WIDTH
                || y+ymap+height < 0 || y+ymap-height > GamePanel.HEIGHT;
    }

    //Getters
    public int getx() {return (int)x;}
    public int gety() {return (int)y;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public int getCwidth() {return cwidth;}
    public int getCheight() {return cheight;}
    public boolean getJumping() {return jumping;}

    //Setters
    public void setPosition(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public void setVector(double dx, double dy)
    {
        this.dx = dx;
        this.dy = dy;
    }

    public void setMapPosition()
    {
        xmap = tileMap.getx();
        ymap = tileMap.gety();
    }

    public void setLeft(boolean b) {left = b;}
    public void setRight(boolean b) {right = b;}
    public void setUp(boolean b) {up = b;}
    public void setDown(boolean b) {down = b;}
    public void setJumping(boolean b) {jumping = b;}

    public void draw(Graphics2D gr)
    {
        if(facingRight)
            gr.drawImage(animation.getImage(),(int)(x + xmap - width/2), (int)(y + ymap - height/2), null);
        else
            gr.drawImage(animation.getImage(),(int)(x + xmap - width/2 + width), (int)(y + ymap - height/2),
                    -width, height, null);
    }
}
