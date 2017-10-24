/**
 * Programmed by: Alex Kamieniarczyk
 * Date: 05/2017
 * 
 * Description: This is a poor man's version of the once popular "Flappy Bird" game. Since I replaced the bird with a box, 
 * 				I decided to name it "Flappy Box". Each click of the mouse causes the box to jump. The floor is lava, don't fall in. 
 * 
 */
package flappybird;     

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;               

import javax.swing.Timer;
import javax.swing.JFrame;

public class flappy implements ActionListener, MouseListener
{

	public static flappy flappybox;
	
	// creates canvas
	public final int WD = 800, HT = 800;         
	
	// creates renderer for double buffering
	public Renderer renderer;              
	
	// box is a rectangle
	public Rectangle box;           
	
	// motion of box
	public int ticks, yBoxMotion, totalScore;      
	
	// element = Rectangle, arrayList contains rectangles
	public ArrayList<Rectangle> pillars;   
	
	// rand variable draws random numbers
	public Random rand;                    
	
	public boolean gameOver, started;
	
	public flappy()                       
	{
		JFrame jframe = new JFrame();
		
		// timer requires ActionListener, = this
		Timer timer = new Timer(20, this);    
		
		// un-nulls the renderer
		renderer = new Renderer();      
		rand = new Random();
		
		jframe.add(renderer);
		
		// title
		jframe.setTitle("Flappy Box");                                        
		
		// if closed = terminates program
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		jframe.setSize(WD, HT);
		jframe.addMouseListener(this);
		jframe.setResizable(true);
		
		//sets jFrame as visible
		jframe.setVisible(true);                                        
		
		// inserts box in center of screen
		box = new Rectangle(WD / 2 - 10, HT / 2 - 10, 20, 20); 
		pillars = new ArrayList<Rectangle>();
		
		// adds 4 pillars
		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);                  
		
		// starts timer, allows renderer to buffer
		timer.start();                    
	}
	
	public void addColumn(boolean start)
	{
		int space = 300;
		int width = 100;
		
		// minimum height=50, maximum=300
		int height = 50 + rand.nextInt(300);
		
		// inserts pillars on screen
		if (start)                           
		{	
			/// move over by width, & if there's any other pillars it'll them over to the left. 
			pillars.add(new Rectangle(WD + width + pillars.size() * 300, HT - height - 120, width, height));     
			pillars.add(new Rectangle(WD + width + (pillars.size() - 1) * 300, 0, width, HT - height - space));
		}
		else
			/// retrieves pillar(rectangle) from array list at position size and subtracts 1, then adds 600 to x.
			pillars.add(new Rectangle(pillars.get(pillars.size() - 1).x + 600, HT - height - 120, width, height));    
			pillars.add(new Rectangle(pillars.get(pillars.size() - 1).x, 0, width, HT - height - space));
		
		}
	
	
	
	// method paints the pillar (rectangle block)
	public void paintColumn(Graphics g, Rectangle pillar)             
	{
		g.setColor(Color.black);
		g.fillRect(pillar.x, pillar.y, pillar.width, pillar.height);
	}
	
	public void jump()
	{
		// resets the yMotion, pillars and score. 
		if (gameOver)                 
		{	
			// places box in center of screen
			box = new Rectangle(WD / 2 - 10, HT / 2 - 10, 20, 20);  
			pillars.clear();
			yBoxMotion = 0;
			totalScore = 0;
			
			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);
			
			gameOver = false;
		}
		
		if (!started)
		{
			started = true;
		}
		// if started but not gameOver
		else if (!gameOver)           
		{
			if (yBoxMotion > 0)
			{
				yBoxMotion = 0;
			}
			
			// each click = box jumps up 10 on grid
			yBoxMotion -= 10;        
		}
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
	

		int speed = 10;
	
		ticks++;
		
		// box won't move until game is started.
		if (started)                          
		{
		
		// creates variable i, boolean, modifier	
		for (int i = 0; i < pillars.size(); i++)
		{	
			// iterates through pillars
			Rectangle pillar = pillars.get(i);
			
			// moves pillars to the left by speed
			pillar.x -= speed;                    
		}
		
		if (ticks % 2 == 0 && yBoxMotion < 15)   
		{
			yBoxMotion += 2;
		}
		
		// adds motion to box, causes it to fall
		box.y += yBoxMotion;                         

		for (int i = 0; i < pillars.size(); i++)
		{
			// iterates through pillars
			Rectangle pillar = pillars.get(i);
			
			// if pillar goes beyong screen on left(0), will be removed. 
			if (pillar.x + pillar.width < 0)        
			{
				pillars.remove(pillar);

				if (pillar.y == 0)              
				{
						addColumn(false);
				}
			}
		}}

		// for each rectangle in pillars
		for (Rectangle pillar : pillars)    
		{
			if (pillar.y == 0 && box.x + box.width / 2 > pillar.x + pillar.width / 2 - 10 && box.x + box.width / 2 < pillar.x + pillar.width / 2 + 10)     /// if box is in the center between 2 pillars
			{
				totalScore++;
			}
				
			// gameOver if box touches pillar
			if (pillar.intersects(box))
			{
				gameOver = true;
				
				// if box falls, pillar will move it
				box.x = pillar.x - box.width;      
			}
		}
		
		if (box.y > HT - 120 || box.y < 0)           
		{
			gameOver = true;
		}
		
		if (box.y + yBoxMotion >= HT - 120)
		{
			// box stops falling once it reaches the ground
			box.y = HT - 120 - box.height;       
		}
		
		// paints pillars
		renderer.repaint();             
		}
	
	
	
	
	public void repaint(Graphics g) 
	{
		// background color
		g.setColor(Color.lightGray);
	    g.fillRect(0, 0, WD, HT);
	    
	    // ground
	    g.setColor(Color.red);
	    g.fillRect(0, HT-120, WD, 150);
	    
	    g.setColor(Color.red);
	    g.fillRect(0, HT-120, WD, 20);
	    
	    // red square
	    g.setColor(Color.yellow);
	    g.fillRect(box.x, box.y, box.width, box.height);
	    
	    // iterator, for each rectangle in pillars
	    for (Rectangle pillar : pillars)  
	    {
	    	paintColumn(g, pillar);
	    }
	    
	    g.setColor(Color.white);
	    g.setFont(new Font("Verdana", 1, 100));
	    
	    if (!started)
	    {
	    	g.drawString("Click to start", 40, HT / 2 - 50);   
	    }
	    
	    if (gameOver)
	    {	
	    	g.drawString("You Suck", 150, HT / 2 - 50);          
	    }
	    
	    if (!gameOver && started)                   
	    {	
	    	// draws the totalScore
	    	g.drawString(String.valueOf(totalScore), WD / 2 - 100, 100);   
	    }
	}
	
	
	public static void main(String[] args)          
	{	
		// creates new instance of flappy
		flappybox = new flappy();           
	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{
		jump();
		
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
	
		
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		
		
	}


}
