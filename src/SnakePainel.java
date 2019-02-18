import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SnakePainel extends JPanel implements KeyListener, Runnable {
	
	public static final int width = 600;
	public static final int height = 600;
	
	//Render
	private Graphics2D g2d;
	private BufferedImage image;
	
	//Loop do jogo
	
	private Thread thread;
	private boolean running;
	private long targetTime;
	
	//Game Stuff-coisas do jogo
	
	private final int SIZE = 20;
	Entity head, apple;
	ArrayList<Entity> snake;
	private int score;
	private int level;
	private boolean gameover;
	
	//Movimento da cobra
	private int dx, dy;
	
	//teclas(key input)
	private boolean up, down, right, left, start; 
	
	public SnakePainel() {
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
	}
	
	private void setFPS(int fps) {
		targetTime = 1000 / fps;
	}
	
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		
		if (k == KeyEvent.VK_UP) up = true;
		if (k == KeyEvent.VK_DOWN) down = true;
		if (k == KeyEvent.VK_LEFT) left = true;
		if (k == KeyEvent.VK_RIGHT) right = true;
		if (k == KeyEvent.VK_ENTER) start = true;
	}

	public void keyReleased(KeyEvent e) {
		int k = e.getKeyCode();
		
		if (k == KeyEvent.VK_UP) up = false;
		if (k == KeyEvent.VK_DOWN) down = false;
		if (k == KeyEvent.VK_LEFT) left = false;
		if (k == KeyEvent.VK_RIGHT) right = false;
		if (k == KeyEvent.VK_ENTER) start = false;
	}

	public void keyTyped(KeyEvent arg0) {

	}
	
	public void addNotify() {
		super.addNotify();
		thread = new Thread(this);
		thread.start();
	}
	
	public void run() {
		if (running) return;
		init();
		long startTime;
		long elapsed;
		long wait;
		while (running) {
			startTime = System.nanoTime();
			
			update();
			requesteRender();
			
			elapsed = System.nanoTime() - startTime;
			wait = targetTime - elapsed / 1000000;
			
			if (wait > 0) {
				try {
					Thread.sleep(wait);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void init() {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g2d = image.createGraphics();
		running = true;
		setUplevel();
		
	}
	
	private void setUplevel() {
		snake = new ArrayList<Entity>();
		head = new Entity(SIZE);
		head.setPosition(width/2, height/2);
		snake.add(head);
		
		for (int i=1; i < 3; i++) {
			Entity e = new Entity(SIZE);
			e.setPosition(head.getX() + (i * SIZE), head.getY());
			snake.add(e);
		}
		apple = new Entity(SIZE);
		setApple();
		score = 0;
		gameover = false;
		level = 1;
		dx = dy = 0;
		setFPS(level * 7); 
	}
	public void setApple() {
		int x = (int) (Math.random() * (width - SIZE));
		int y = (int) (Math.random() * (height - SIZE));
		
		x = x - (y % SIZE);
		y = y - (y % SIZE);
		
		apple.setPosition(x, y);
	}
	private void requesteRender() {
		render(g2d);
		Graphics g = getGraphics();
		g.drawImage(image, 0,0,null);
		g.dispose();
	}

	private void update() {
		if (gameover) {
			if(start) {
				setUplevel();
			}
			return;
		}
		
		if (up && dy == 0) {
			dy = -SIZE;
			dx = 0;
		}
		if (down && dy == 0) {
			dy = SIZE;
			dx = 0;
		}
		if (left && dx == 0) {
			dx = -SIZE;
			dy = 0;
		}
		if (right && dx == 0 && dy != 0) {
			dx = SIZE;
			dy = 0;
		}
		
		if(dx != 0 || dy != 0) {
			for (int i = snake.size() - 1; i >0; i--){
				snake.get(i).setPosition(
						snake.get(i - 1).getX(),
						snake.get(i - 1).getY()
						);
			}
			
			head.move(dx, dy);
			
		}

		for (Entity e : snake) {
			if (e.isCollsion(head)) {
				gameover = true;
				break;
			}
		}
		if(apple.isCollsion(head)) {
			score++;
			setApple();
				
			Entity e = new Entity(SIZE);
			e.setPosition(-100, -100);
			snake.add(e);
				
			if (score % 10 == 0) {
				level++;
				if (level > 10) level = 10;
				setFPS(level * 10);
			}
		}
			
			if (head.getX() < 0) head.setX(width);
			if (head.getY() < 0) head.setY(height);
			if (head.getX() > width) head.setX(0);
			if (head.getX() > height) head.setY(0);
	
	}
	
	public void render(Graphics2D g2d) {
		g2d.clearRect(0, 0, width, height);
		
		g2d.setColor(Color.GREEN);
		
		for (Entity e : snake) {
			e.render(g2d);
		}
		
		g2d.setColor(Color.red);
		apple.render(g2d);
		if(gameover) {
			g2d.setColor(Color.black);
			g2d.setBackground(Color.black);
			g2d.drawString("GAME OVER!", 200, 200);
		}
		
		g2d.setColor(Color.white);
		g2d.drawString("\n\nSCORE: " + score + "         LEVEL: " + level, 10, 20);
		
		if (dx == 0 && dy == 0) {
			g2d.drawString("READY!", 200, 200);
		}
	}


}

