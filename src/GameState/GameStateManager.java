package GameState;

/**
 * Created by Sav on 10/12/14.
 */

import java.util.ArrayList;
import java.awt.Graphics2D;

public class GameStateManager {

    private ArrayList<GameState> gameStates;
    private int currentState;

    public static final int MENUSTATE = 0;
    public static final int LEVEL1STATE = 1;

    //Constructor - First State
    public GameStateManager()
    {
        gameStates = new ArrayList<GameState>();

        currentState = MENUSTATE;
        gameStates.add(new MenuState(this));
        gameStates.add(new Level1State(this));
    }

    //To change states
    public void setState(int state)
    {
        currentState = state;
        gameStates.get(currentState).init();
    }

    //Update
    public void update()
    {
        gameStates.get(currentState).update();
    }

    public void draw(Graphics2D gr)
    {
        gameStates.get(currentState).draw(gr);
    }

    public void keyPressed(int k)
    {
        gameStates.get(currentState).keyPressed(k);
    }

    public void keyReleased(int k)
    {
        gameStates.get(currentState).keyReleased(k);
    }
}
