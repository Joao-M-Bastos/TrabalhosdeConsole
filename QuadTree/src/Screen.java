import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Screen extends JPanel implements ActionListener {
	public static Main main;
	
	public static int SCREENRES_X = 1290;
	public static int SCREENRES_Y = 900;
	
	public static int PARTICLENUMBER = 0;
	public static int PSIZE = 6;
	
	public static long MSTIME = 0;
	
	static ArrayList<Particle> particles = new ArrayList<Particle>();
	
	public static enum CollisionMode { NORMAL, QUADTREE };
	
	public static CollisionMode mode = CollisionMode.NORMAL;
	
	//Initializing Quad
	public static Quad quad;
	
	
	public Screen(Main main) {
		this.main = main;
		
		initScreen();
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				addParticle();
			}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			
		});
	}
	
	public void initScreen() {
		
        setBackground(Color.black);
        setFocusable(true);
        setPreferredSize(new Dimension(SCREENRES_X, SCREENRES_Y));
        beginScene();
	}
	
	public void beginScene() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Quantas particulas você gostaria de simular?\n");
		
		try {
			PARTICLENUMBER = Integer.parseInt(br.readLine());
		} catch (NumberFormatException e) {
			System.out.println("Não é possivel trabalhar com um texto. Eu presiso de um numero!");
			System.exit(1);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int choice = 0;
		
		System.out.println("Qual tipo de colissão você gostaria de usar?\n");
		System.out.println("\nAperte 1 para colisão normal do sistema.\n");
		System.out.println("\nAperte 2 para uma colisão baseada em QuadTree.\n");
		
		try {
			choice = Integer.parseInt(br.readLine());
		} catch (NumberFormatException e) {
			System.out.println("Não é possivel trabalhar com um texto. Eu presiso de um numero!");
			System.exit(1);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(choice == 1) mode = CollisionMode.NORMAL;
		else if(choice == 2) mode = CollisionMode.QUADTREE;
		else {
			System.out.println("O valor passado não é  valido, por favor complete com 1 ou 2!");
			System.exit(1);
		}
		
		for (int i = 0; i < PARTICLENUMBER; i++) {
			Random r = new Random();
			float rx = r.nextFloat() * SCREENRES_X;
			float ry = r.nextFloat() * SCREENRES_Y;
			particles.add(new Particle(rx, ry, SCREENRES_X, SCREENRES_Y, true));
		}
		
		if(mode == CollisionMode.NORMAL) for(Particle p : particles) p.particles = particles;
		
	    Timer timer = new Timer(1, this);
	    timer.setRepeats(true);
	    timer.start();
	}
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawScene(g);
    }
    
    public void drawScene(Graphics g) {
		//Creation of Particles
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).Draw(g);
			}
		//Creation of Quadtree
		if(quad != null) quad.Draw(g);
		
		//Speed Timer Draw
		g.setColor(Color.blue);
		g.drawRect(SCREENRES_X - 120, 30, 100, 30);
		g.setColor(Color.black);
		g.fillRect(SCREENRES_X - 120, 30, 100, 30);
		g.setColor(Color.white);
		g.drawString(MSTIME + "ms", SCREENRES_X - 100, 60);
    }
    
    public void run() {
    	
		long start = System.currentTimeMillis();
		
		switch(mode) 
		{
		case NORMAL:
			for(Particle p : particles) p.Execute();
			for(Particle p : particles) p.CollisionCheck();
			break;
		case QUADTREE:
			quad = new Quad(new Rect(0, 0, SCREENRES_X, SCREENRES_Y), 8);
			for(Particle p : particles) quad.Insert(p);
			for(Particle p : particles) p.Execute();
			for(Particle p : particles) p.CollisionCheck();
			for(Particle p : particles) p.particles = new ArrayList<Particle>();
			break;
		}
		repaint();
		
		long totalTime = System.currentTimeMillis() - start;
		MSTIME = totalTime;
		
		main.setTitle("[Quadtree] Particles: " + particles.size() +
				" / " + totalTime + "ms " + 
				"(" + SCREENRES_X + "x" + SCREENRES_Y + ")");	
    }
    
    public void addParticle() {
    	Point mouse = getMousePosition();
    	particles.add(new Particle(mouse.x, mouse.y, SCREENRES_X, SCREENRES_Y, false));
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		run();
	}
}
