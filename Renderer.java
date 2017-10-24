package flappybird;

import java.awt.Graphics;

// JPanel = parent of class
import javax.swing.JPanel;

// retains everything from actual JPanel class, + extends it with our own
public class Renderer extends JPanel      
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void paintComponent(Graphics g) 
	{
		// calls paintComponent from Jpanel
		super.paintComponent(g);
		
		// passes Graphics g into flappy
		flappy.flappybox.repaint(g);    /// passes Graphics g into flappy
	}
}
