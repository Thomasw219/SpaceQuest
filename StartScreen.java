import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.FileInputStream;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class StartScreen extends JPanel
{

    private BufferedImage image;
    private Timer timer;
    private boolean flash;

    public StartScreen() 
    {
       try {image = ImageIO.read(new FileInputStream("Background.jpg"));} 
       catch (IOException e) {e.printStackTrace();}
       flash = true;
       timer = new Timer(1000, new ActionListener(){
    	   @Override
    	   public void actionPerformed(ActionEvent e)
    	   {
    		   flash = !flash;
    		   repaint();
    	   }
       });
       timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
        Graphics2D g2 = ((Graphics2D) g);
        g2.setColor(Color.yellow);
		Font font = new Font("Verdana", Font.BOLD, 20);
  		g2.setFont(font);
        if (flash)
        	g2.drawString("Click Anywhere to Continue", 350, 700);
    }

}