import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;

public class SorgShooter implements ActionListener, KeyListener
{
	public JFrame jframe; 
	public static SorgShooter sorgshooter; 
	public SorgRender fps; 
	//Game loop timer. Set tick speed.
	public Timer time = new Timer(10,this);
	public int tick, direction, bossDirection;
	public ArrayList <Integer> bulletDirection = new ArrayList<Integer>();
	public Point playerPosition, bossPosition;
	public ArrayList <Point> bullet = new ArrayList<Point>();
	public boolean isOnScreen = true;
	public boolean shot = false; 
	int bulletCount = 0;
	int boost = 0;
	boolean isBoost = false;
	boolean restart = false;
	public static final int UP = 1, DOWN = 2, RIGHT = 3, LEFT = 4, PAUSED = 5, UPRIGHT = 6, UPLEFT = 7, DOWNRIGHT = 8, DOWNLEFT = 9;  

	//Creates a new Java window and tells keyListener class to become active.
	public SorgShooter()
	{
		jframe = new JFrame("SorgShooter");  
		jframe.addKeyListener(this); 
	}
	//Displays the window in the center of the screen and starts the timer which starts the game. 
	public void startGame()
	{
		tick = 0;
		direction = PAUSED; 
		playerPosition = new Point(400,300); 
		bossPosition = new Point(700,-500); 
		bossDirection = UP;
		jframe.setVisible(true);
		jframe.setSize(800, 600);
		jframe.setResizable(false);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		time.start();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();	
		jframe.setLocation(screen.width / 2 - jframe.getWidth() / 2, screen.height / 2 - jframe.getHeight() / 2); 	
		jframe.add(fps = new SorgRender());
	}
    
	@Override
	//Everything in here will happen once every frame and then refresh.
	public void actionPerformed(ActionEvent arg0) 
	{
		//We can start to re-use bullet images that are arleady rendered.
		if(bulletCount > 20)
		{
			bulletCount = 0;
			restart = true;
		}
		//Refresh the graphics.
		fps.repaint();
		if(fps.level2)
		{
			//This whole first section is used to ommunicate new positions of boss, bullet, and player to SorgRender.java.
			tick++;
	     	fps.setPlayer(playerPosition);
	       	fps.setBoss(bossPosition);
			//If bullet ArrayList has already been filled past 20, continue moving already fired bullets so that they do not stop in place.
	    	if(restart)
	       	{
	       		for(int x = bulletCount; x <= 20; x++)
		       	{
		       		fps.setBullet(bullet.get(x), x);
		       	}
	       	}
			//Newly fired bullets will continue to move. 
	       	for(int x = 0; x < bulletCount; x++)
	       	{
	       		fps.setBullet(bullet.get(x), x);
	       	}
			//Player moves off the screen.
			if (playerPosition.x > 759  || playerPosition.x < 1 || playerPosition.y > 559 || playerPosition.y < 1)		 
			{
				isOnScreen = false; 
			}
			//If player is not within the window, return player to the center.
			if (!isOnScreen)
			{
	      		playerPosition = new Point(400,300);
	     		isOnScreen = true;
	      	}
			//Player has entered one of the arrow keys causing bullet to fire. This will change the position of bullets in the bullet array.
			if (shot)
			{
				//If bullet ArrayList has already been filled past 20, continue moving already fired bullets so that they do not stop in place.
				if(restart)
				{
					//Change the position of bullet x depnding on its specific direction y. There may be multible bullets with different directions, this must be done for each.
					for(int x = bulletCount; x <= 20; x++)
					{
						if (bulletDirection.get(x) == UP)
						{
							bullet.set(x, new Point(bullet.get(x).x,bullet.get(x).y + 28));
						}
						if (bulletDirection.get(x) == DOWN)
						{
							bullet.set(x, new Point(bullet.get(x).x,bullet.get(x).y - 28));
						}
						
						if (bulletDirection.get(x) == RIGHT)
						{
							bullet.set(x, new Point(bullet.get(x).x + 28,bullet.get(x).y));
						}
						if (bulletDirection.get(x) == LEFT)
						{
							bullet.set(x, new Point(bullet.get(x).x - 28,bullet.get(x).y));
						}			
					}
				}
				//Same idea as previous but for newly fired bullets. 
				for(int x = 0; x < bulletCount; x++)
				{
					if (bulletDirection.get(x) == UP)
					{
						bullet.set(x, new Point(bullet.get(x).x,bullet.get(x).y + 28));
					}
					if (bulletDirection.get(x) == DOWN)
					{
						bullet.set(x, new Point(bullet.get(x).x,bullet.get(x).y - 28));
					}
					
					if (bulletDirection.get(x) == RIGHT)
					{
						bullet.set(x, new Point(bullet.get(x).x + 28,bullet.get(x).y));
					}
					if (bulletDirection.get(x) == LEFT)
					{
						bullet.set(x, new Point(bullet.get(x).x - 28,bullet.get(x).y));
					}			
				}
			}
			//Will be true if player hits the spacebar giving him/her a speed boost.
			if (isBoost)
			{
				boost = 3; 
			}
			//No boost, so boost = 1 for 1 * normal speed.
			else
			{
			boost = 1; 
			}
			//Change player position based on direction which is determined by the keys that are pressed.
			if (direction == UP)
			{
				playerPosition = new Point(playerPosition.x, playerPosition.y + (4 * boost));

			}
			else if (direction == DOWN)
			{
				playerPosition = new Point(playerPosition.x, playerPosition.y - (4 * boost)); 
			}
			else if (direction == RIGHT)
			{
				playerPosition = new Point(playerPosition.x + (4 * boost), playerPosition.y); 
			}
			else if (direction == LEFT)
			{
				playerPosition = new Point(playerPosition.x - (4 * boost), playerPosition.y); 
			}	
			//Generate a new random boss direction every 300 ticks so that its movement is unpredictable.	
			if (tick % 300 == 0)
			{  
				int bD = 0; 
				Random ran = new Random();
				bD = ran.nextInt(DOWNLEFT + 3) + 1; 
				bossDirection = bD; 
			}
			if (bossDirection == LEFT)
			{
				bossPosition = new Point(bossPosition.x - 12, bossPosition.y);
			}
			if (bossDirection == RIGHT)
			{
				bossPosition = new Point(bossPosition.x + 12, bossPosition.y);
			}
			if (bossDirection == UP)
			{
				bossPosition = new Point(bossPosition.x, bossPosition.y - 12);
			}
			if (bossDirection == LEFT)
			{
				bossPosition = new Point(bossPosition.x, bossPosition.y + 12);
			}
			if (bossDirection == UPLEFT)
			{
				bossPosition = new Point(bossPosition.x - 12, bossPosition.y - 12);
			}
			if (bossDirection == DOWNLEFT)
			{
				bossPosition = new Point(bossPosition.x - 12, bossPosition.y + 12);
			}
			if (bossDirection == UPRIGHT)
			{
				bossPosition = new Point(bossPosition.x + 12, bossPosition.y - 12);
			}
			if (bossDirection == DOWNRIGHT)
			{
				bossPosition = new Point(bossPosition.x + 12, bossPosition.y + 12);
			}
			//This is BOSS MODE. Basically the boss will stop moving randomly and seek out the player specifically.
			else		
			{
	
				if (bossPosition.x < playerPosition.x)
				{
					bossPosition = new Point(bossPosition.x + 4, bossPosition.y);
				}
				if (bossPosition.y < playerPosition.y)
				{
					bossPosition = new Point(bossPosition.x, bossPosition.y + 4);
				}
				if (bossPosition.x > playerPosition.x)
				{
					bossPosition = new Point(bossPosition.x - 4, bossPosition.y);
				}
				if (bossPosition.y > playerPosition.y)
				{
					bossPosition = new Point(bossPosition.x, bossPosition.y - 4);
				}
			}
			
			//Next few blocks of code are used to change boss direction if the boss starts to move off-screen.
			if (bossPosition.x > 759)
			{
				if (bossDirection == RIGHT)
				{ 
					Random ran = new Random();
					if(ran.nextInt() + 1 == 1)
					{
						bossDirection  = DOWNLEFT;
					} 
					else 
					{
						bossDirection = UPLEFT; 
					}
				}
					if (bossDirection == UPRIGHT)
					{
						bossDirection = UPLEFT;
					}
					else 
					{
						bossDirection = DOWNLEFT; 
					}
			}
			if (bossPosition.x < 1)
			{
				if (bossDirection == LEFT)
					{
						Random ran = new Random();
						if(ran.nextInt() + 1 == 1)
						{
							bossDirection  = DOWNRIGHT;
						} 
						else 
						{
							bossDirection = UPRIGHT; 
						}
					}
				if (bossDirection == UPLEFT)
				{
					bossDirection = DOWNLEFT;
				}
				else 
				{
					bossDirection = DOWNRIGHT; 
				}
			}
			if(bossPosition.y > 559)
			{
				if (bossDirection == DOWN)
				{
					Random ran = new Random();
					if(ran.nextInt() + 1 == 1)
					{
						bossDirection  = UPRIGHT;
					} 
					else 
					{
						bossDirection = UPLEFT; 
					}
				}
				if (bossDirection == DOWNLEFT)
				{
					bossDirection = UPLEFT;
				}
				else 
				{
					bossDirection = UPRIGHT; 
				}
			}
			
			if(bossPosition.y < 1)
			{ 
				if (bossDirection == UP)
				{
					Random ran = new Random();
					if(ran.nextInt() + 1 == 1)
					{
						bossDirection  = DOWNRIGHT;
					} 
					else 
					{
						bossDirection = DOWNLEFT; 
					}
				}
				if (bossDirection == UPLEFT)
				{
					bossDirection = DOWNLEFT;
				}
				else 
				{
					bossDirection = DOWNRIGHT; 
				}
			}
		}
	}
	//Generated method that comes from keyListener that will detect input from computer keyboard. 
	@Override
	public void keyPressed(KeyEvent e) 
	{
		//WASD used to change player directiom.
		int i = e.getKeyCode();

		if (i == KeyEvent.VK_A)
		{
			direction = LEFT;
		}

		if (i == KeyEvent.VK_D)
		{
			direction = RIGHT;
		}

		if (i == KeyEvent.VK_W)
		{
			
			direction = DOWN;
		}

		if (i == KeyEvent.VK_S)
		{
			
			direction = UP;
		}
		//Arrow keys used to determine bullet fire and bullet fire diretion. Either will create new bullets or re-locate old off-screen ones.
		if 	(i == KeyEvent.VK_LEFT)
		{  
			fps.shoot(bulletCount, restart);
			if(!restart)
			{
				bullet.add(bulletCount, new Point(playerPosition.x + 10, playerPosition.y + 15));
				fps.addBullet(bullet.get(bulletCount), bulletCount);
				bulletDirection.add(LEFT);
			}
			else
			{
				bullet.set(bulletCount, new Point(playerPosition.x + 10, playerPosition.y + 15));
				fps.setBullet(bullet.get(bulletCount), bulletCount);
				bulletDirection.set(bulletCount, LEFT);
			}
			bulletCount++;
			shot = true; 
		}
		if 	(i == KeyEvent.VK_RIGHT)
		{   
			fps.shoot(bulletCount, restart);
			if(!restart)
			{
				bullet.add(bulletCount, new Point(playerPosition.x + 10, playerPosition.y + 15));
				fps.addBullet(bullet.get(bulletCount), bulletCount);
				bulletDirection.add(RIGHT);
			}
			else
			{
				bullet.set(bulletCount, new Point(playerPosition.x + 10, playerPosition.y + 15));
				fps.setBullet(bullet.get(bulletCount), bulletCount);
				bulletDirection.set(bulletCount, RIGHT);
			}
			bulletCount++;
			shot = true;
		}
		if 	(i == KeyEvent.VK_UP)
		{
			fps.shoot(bulletCount, restart);
			if(!restart)
			{
				bullet.add(bulletCount, new Point(playerPosition.x + 10, playerPosition.y + 15));
				fps.addBullet(bullet.get(bulletCount), bulletCount);
				bulletDirection.add(DOWN);
			}
			else
			{
				bullet.set(bulletCount, new Point(playerPosition.x + 10, playerPosition.y + 15));
				fps.setBullet(bullet.get(bulletCount), bulletCount);
				bulletDirection.set(bulletCount, DOWN);
			}
			bulletCount++;
			shot = true;
			
		}
		if 	(i == KeyEvent.VK_DOWN)
		{  
			fps.shoot(bulletCount, restart);
			if(!restart)
			{
				bullet.add(bulletCount, new Point(playerPosition.x + 10, playerPosition.y + 10));
				fps.addBullet(bullet.get(bulletCount), bulletCount);
				bulletDirection.add(UP);
			}
			else
			{
				bullet.set(bulletCount, new Point(playerPosition.x + 10, playerPosition.y + 10));
				fps.setBullet(bullet.get(bulletCount), bulletCount);
				bulletDirection.set(bulletCount, UP);
			}
			bulletCount++;
			shot = true;
		}
		//If space bar is pressed and held, give the player a boost of 3 times speed.
		if (i == KeyEvent.VK_SPACE)
		{
			isBoost = true; 
		}
	}
	@Override
	//When space bar is released player boost will go away.
	public void keyReleased(KeyEvent e) 
	{
		isBoost = false; 
		
	}
	@Override
	//IGNORE THIS BUT DO NOT REMOVE IT
	public void keyTyped(KeyEvent e) 
	{

	}


}