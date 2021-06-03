import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Font;
import java.util.ArrayList;

//This class will be used to render the graphics based on the data computed in SorgShooter.java. 
@SuppressWarnings("serial")
public class SorgRender extends JPanel
{
	int count = 0;
	public boolean level2 = false;
	int boat = 0;
	int red = 0;
	int green = 0;
	int blue = 0;
	int playerHealth = 0; 
	int sorgHealth = 0; 
	int counter = 0;
	int isDrawn = 0;
	int bulletCount = 0;
	boolean beenShot = false;
	private Point player = new Point(0,0);
	private Point boss = new Point(600,500);
	public ArrayList <Point> bullet = new ArrayList<Point>();
	private BufferedImage pImage;
	private BufferedImage bImage;
	private ArrayList<BufferedImage> fbImage = new ArrayList<BufferedImage>();
	private BufferedImage background;
	private BufferedImage ship;
	private BufferedImage sun;
	private BufferedImage spooky;
	private BufferedImage win;
	private BufferedImage lose;
	private BufferedImage bossHealth;
	private BufferedImage playerH;
	private BufferedImage ghostHit;

	//We want player in the class to inherit player Position from SorgShooter.java.
	public void setPlayer(Point p)
	{
		player = p;
	}
	//We want boss in the class to inherit boss Position from SorgShooter.java.
	public void setBoss(Point p)
	{
		boss = p;
	}
	//We want bullet in the class to inherit bullet Position from SorgShooter.java.
	public void setBullet(Point p, int index)
	{
		bullet.set(index, p);
	}
	//Add a bullet to the bullet ArrayList in this class specified by SorgShooter.java.
	public void addBullet(Point p, int index)
	{
		bullet.add(index, p);
	}
	//If player has shot a bullet, we need to render the first 20 or so fireball images because there may need to be multible bullets displayed at once.
	public void shoot(int index, boolean restart)
	{
		//If 20 bullets are already rendered, no need to continue doing this. We will simply change the position of existing bullets. 
		if(restart)
		{
			bulletCount = index;
			return;
		}
		//Add fireball image to ArrayList. 
		try 
		{                
  			fbImage.add(bulletCount, ImageIO.read(new File("Assets/Fireball.png")));
		}
		catch (IOException ex) 
		{
			System.out.println("Error opening image");
		}
		beenShot = true;
		bulletCount = index;
	}
	@Override
	//Built in method to display graphics in Jpanel. Everything here will also happen once per frame. 
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		//Short cutscene that will be displayed before boss fight. 
		if(!level2)
		{
			count++;
			boat+= 2;
			//We only need to render the images once, not once per frame. Much less CPU expensive.
			if(isDrawn == 0)
			{
				//All of these must be surrounded by try and catch. Throws IO exception if it fails to open a file.
				try 
				{                
					ship = ImageIO.read(new File("Assets/boat.png"));
				} 
				catch (IOException ex) 
				{
					System.out.println("Error opening image");
				}
				try 
				{                
					background = ImageIO.read(new File("Assets/ocean.jpg"));
				} 
				catch (IOException ex) 
				{
					System.out.println("Error opening image");
				}
				try
				{                
					sun = ImageIO.read(new File("Assets/sun.png"));
				} 
				catch (IOException ex) 
				{
					System.out.println("Error opening image");
				}
				isDrawn = 1;
			}
			//After images are rendered, display them.
			g.drawImage(background, 0,0, this);
			g.drawImage(sun, 600, 50, this);
			//The boat is the only moving object, and will sail across the screen.
			g.drawImage(ship, boat, 150, this);
			g.setFont(new Font("default", Font.BOLD, 15));
			//Display game controls to player.
			g.drawString("You make your way through the water...", 300, 100);
			g.drawString("MOVE: AWSD", 300, 120);
			g.drawString("SHOOT: ARROW KEYS", 300, 140);
			g.drawString("BOOST: SPACEBAR", 300, 160);
			//Boat has reached the end of the screen (800 pixles wide).
			if (boat > 759)
			{
				//End cutscene and start the boss fight.
				level2 = true; 
				isDrawn = 0;
			}
		}
		//Start of boss fight.
		if (level2)
		{
			//Once again, render images only once. 
			if(isDrawn == 0)
			{
				try 
				{                
					spooky = ImageIO.read(new File("Assets/fightBackground.jpg"));
				} 
				
				catch (IOException ex) 
				{
					System.out.println("Error opening image");
				}
				try 
				{                
          			pImage = ImageIO.read(new File("Assets/spaceship.png"));
      			} 
				catch (IOException ex) 
				{
					System.out.println("Error opening image");
      			}
				try 
				{                
          			bImage = ImageIO.read(new File("Assets/ghost.png"));
      			} 
				catch (IOException ex) 
				{
					System.out.println("Error opening image");
      			}
				
				try 
				{                
          			bossHealth = ImageIO.read(new File("Assets/bossHealth.png"));
      			} 
				catch (IOException ex) 
				{
					System.out.println("Error opening image");
      			}
				try 
				{                
          			playerH = ImageIO.read(new File("Assets/playerHealth.png"));
      			} 
				catch (IOException ex) 
				{
					System.out.println("Error opening image");
      			}
				try 
				{                
          			ghostHit = ImageIO.read(new File("Assets/ghostHit.png"));
      			} 
				catch (IOException ex) 
				{
					System.out.println("Error opening image");
      			}
				isDrawn = 1;
			}
			//Display background and Health text.
			g.drawImage(spooky, 0 , 0 , this);
			g.drawImage(bossHealth, 270, 50, this);
			g.drawImage(playerH, 250, 120, this);
			g.setColor(Color.GREEN);
			//Display dynamic health bars as a rectangle with a decreasing length.  
			g.fillRect(255, 105, 270 - sorgHealth/13, 20);
			g.fillRect(340, 170, 100 - playerHealth/10, 20);
			//Since there may be multible bullets, we must display them all in a for loop. 
			for(int x = 0; x < fbImage.size() && beenShot; x++)
			{
				g.drawImage(fbImage.get(x), bullet.get(x).x, bullet.get(x).y, this);
			}
			//Display player and boss at appropiate position that is calculated in SorgShooter.java and sent over. 
			g.drawImage(pImage, player.x,player.y, this);
			g.drawImage(bImage, boss.x, boss.y, this);
			
			//For loop to check for player and boss collitions. If player position every meets boss position, player must lose health. 
			for(int x = boss.x; x < boss.x + 100; x++)
			{
				for (int y = boss.y; y < boss.y + 100; y++)
				{
					for(int z = 0; z < 10; z++)
					{
						if (player.x + z == x && player.y + z == y)
						{
							playerHealth++; 
						}
					}
				}
			}
			//At this count, player has run out of health and has lost the game. Display losing screen.
			if (playerHealth > 1000)
			{  
				counter++;
				try 
				{                
					lose = ImageIO.read(new File("Assets/lose.png"));
				} 
				catch (IOException ex) 
				{
					System.out.println("Error opening image");
				}
				g.drawImage(lose, 0, 0, this);
				//After 800 ticks, Jpanel will automatically exit without the user closing it. 
				if (counter > 800)
				{
					System.exit(0);
				}
			}
			//Checks for boss and bullet collitions. If boss position ever meets bullet position boss must lose health. 
			for(int x = boss.x; x < boss.x + 100; x++)
			{
				for (int y = boss.y; y < boss.y + 100; y++)
				{
					for(int z = 0; z <= bulletCount && bulletCount != 0; z++)
					{
						for(int i = 0; i < 10; i++)
						{
							if (bullet.get(z).x + i == x && bullet.get(z).y + i == y && bullet.get(z).x != player.x && bullet.get(z).x != player.x + 1 && bullet.get(z).x != player.x + 2 && bullet.get(z).x != player.x + 3 && bullet.get(z).x != player.x + 4)
							{
								sorgHealth++; 
								g.drawImage(ghostHit, boss.x, boss.y, this);
							}
						}
					}
				}
			}
			//At this count boss has run out of health. Player has won. Display winning screen.
			if (sorgHealth > 3500)
			{
				counter++;
				try 
				{                
					win = ImageIO.read(new File("Assets/win.jpg"));
				} 
				catch (IOException ex) 
				{
					System.out.println("Error opening image");
				}
				g.drawImage(win, 0, 0, this);
				//After 800 ticks, Jpanel will automatically exit without the user closing it
				if (counter > 800)
				{
					System.exit(0);
				}
			}
	
		}
	}
}
