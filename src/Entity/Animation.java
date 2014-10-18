package Entity;

/**
 * Created by Sav on 10/14/14.
 */
import java.awt.image.BufferedImage;

//Contains sprite animation
public class Animation {
    private BufferedImage[] frames;
    private int currentFrame;
    private long startTime;
    private long delay;

    //If already played
    private boolean played;

    public Animation()
    {
        //System.out.println("Animation Constructor");
        played = false;
    }

    public void setFrames(BufferedImage[] frames)
    {
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
        played = false;
    }

    public int getCurrentFrame() {return currentFrame;}
    public BufferedImage getImage() {return frames[currentFrame];}
    public boolean getPlayed() {return played;}

    void setDelay(long d) {delay = d;}
    void setCurrentFrame(int i) {currentFrame = i;}

    public void update()
    {
        if (delay==-1)
            return;

        long elapsed = (System.nanoTime()-startTime)/1000000;
        if(elapsed > delay)
        {
            currentFrame++;
            startTime = System.nanoTime();
        }
        if(currentFrame == frames.length)
        {
            currentFrame = 0;
            played = true;
        }
    }

}
