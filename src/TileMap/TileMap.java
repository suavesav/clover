package TileMap;

/**
 * Created by Sav on 10/13/14.
 */

import Main.GamePanel;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;

public class TileMap {

    //Position
    private double x;
    private double y;
    private int xmin;
    private int ymin;
    private int xmax;
    private int ymax;

    //Map
    private int[][] map;
    private int tileSize;
    private int numRows;
    private int numCols;
    private int width;
    private int height;

    //Tile Set
    private BufferedImage tileset;
    private int numTilesAcross;
    private Tile[][] tiles;

    //Drawing - to only draw the tiles on the screen instead of all the tiles all the time
    private int rowOffset;
    private int colOffset;
    private int numRowsToDraw;
    private int numColsToDraw;

    //Constructor
    public TileMap(int tileSize)
    {
        this.tileSize = tileSize;
        numRowsToDraw = GamePanel.HEIGHT/tileSize + 2;
        numColsToDraw = GamePanel.WIDTH/tileSize + 2;
    }

    public void loadTiles(String s)
    {
        try
        {
            tileset = ImageIO.read(getClass().getResourceAsStream(s));
            numTilesAcross = tileset.getWidth()/tileSize;
            tiles = new Tile[2][numTilesAcross];

            BufferedImage subImage;
            for(int col = 0; col < numTilesAcross; col++)
            {
                subImage = tileset.getSubimage(col*tileSize, 0, tileSize, tileSize);
                tiles[0][col] = new Tile(subImage, Tile.NORMAL);

                subImage = tileset.getSubimage(col*tileSize, tileSize, tileSize, tileSize);
                tiles[1][col] = new Tile(subImage, Tile.BLOCKED);
            }
        }
        catch (Exception E)
        {
            E.printStackTrace();
        }
    }

    public void loadMap(String s)
    {
        try
        {
            InputStream in = getClass().getResourceAsStream(s);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            numCols = Integer.parseInt(br.readLine());
            numRows = Integer.parseInt(br.readLine());
            map = new int[numRows][numCols];
            width = numCols * tileSize;
            height = numRows * tileSize;

            String delimiters = "\\s+";
            for(int row = 0; row < numRows; row++)
            {
                String line = br.readLine();
                String[] tokens = line.split(delimiters);
                for(int col = 0; col < numCols; col++)
                {
                    map[row][col] = Integer.parseInt(tokens[col]);
                }
            }
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    public int getTileSize() {return tileSize;}
    public int getx() {return (int)x;}
    public int gety() {return (int)y;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public int getNumRows() {return numRows;}
    public int getNumCols() {return numCols;}

    public int getType(int row, int col)
    {
        int rc = map[row][col];
        int r = rc / numTilesAcross;
        int c = rc % numTilesAcross;
        return tiles[r][c].getType();
    }

    public void setPosition(double x, double y)
    {
        this.x = x;
        this.y = y;

        fixBounds();

        colOffset = (int)-this.x/tileSize;
        rowOffset = (int)-this.y/tileSize;
    }

    private void fixBounds()
    {
        if(x < xmin)
            x = xmin;
        if(y < ymin)
            y = ymin;
        if(x > xmax)
            x = xmax;
        if(y > ymax)
            y = ymax;
    }

    public void draw(Graphics2D gr)
    {
        for(int row = rowOffset; row < rowOffset+numRowsToDraw; row++)
        {
            if(row >= numRows)
                break;

            for(int col = colOffset; col < colOffset+numColsToDraw; col++)
            {
                if(col >= numCols)
                    break;
                if(map[row][col] == 0)
                    continue;

                int rc = map[row][col];
                int r = rc / numTilesAcross;
                int c = rc % numTilesAcross;

                gr.drawImage(tiles[r][c].getImage(), (int)x + col*tileSize, (int)y + row*tileSize, null);
            }
        }
    }
}
