package GameState;

//import java.awt.*;

/**
 * Created by Sav on 10/12/14.
 */
public abstract class GameState {

    protected GameStateManager gsm;

    public abstract void init();
    public abstract void update();
    public abstract void draw(java.awt.Graphics2D gr);
    public abstract void keyPressed(int k);
    public abstract void keyReleased(int k);

}
