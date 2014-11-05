package Entity;
import java.awt.*;
/**
 * Created by Sav on 10/22/14.
 */
public class HUD {
    private Player player;
    private Font font;
    private Color textColor;

    public HUD(Player P)
    {
        player = P;
        font = new Font("Arial", Font.BOLD, 14);
        textColor = new Color(128, 0, 0);
    }

    public void draw(Graphics2D gr)
    {
        gr.setFont(font);
        gr.setColor(textColor);
        gr.drawString("HEALTH: " + player.getHealth() + "/" + player.getMaxHealth(), 35, 20);
        gr.drawString("POINTS: " + player.getPoints(), 195, 20);
    }
}
