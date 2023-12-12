import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class Particle{
	float px;
	float py;
	float lx;
	float ly;
	
	float lastX, lastY;
	
	int STATE;
	int AlarmState = 1;
	
	boolean duringCollision = false;
	int collisionDuration = 0;
	
	ArrayList<Particle> particles = new ArrayList<Particle>();
	
	Color color = Color.white;
	
	public Particle(float px, float py, float lx, float ly) {
		this.px = px; this.py = py;
		this.lx = lx; this.ly = ly;
		this.lastX = px;
		this.lastY = py;
	}
	public void Execute() {
		if (duringCollision == true) {
			color = Color.red;
			collisionDuration--;
			if (collisionDuration <= 0)
				duringCollision = false;
		} else {
			color = Color.white; }
		RandomMove();
	}
	public void RandomMove() {

			switch(STATE) {
			case 1: px++; break;
			case 2: px--; break;
			case 3: py++; break;
			case 4: py--; break;
			case 5: px++; py++; break;
			case 6: px--; py--; break;
			case 7: px++; py--; break;
			case 8: px--; py++; break;
		    default: break;
			}
			
			this.AlarmState++;
			if (AlarmState > 100) {
				Random r = new Random();
				this.STATE = r.nextInt(9);
				AlarmState = 0;
			}
			
			if (this.px < 0) this.px = this.ly;
			if (this.py < 0) this.py = this.ly;
			if (this.px > this.lx) this.px = 0;
			if (this.py > this.ly) this.py = 0;
			
		lastX = px;
		lastY = py;
	}
	
	public void CollisionCheck() {
		for(int i = 0; i < particles.size(); i++) 
		{
			if(particles.get(i) != this) 
			{
				Particle other = particles.get(i);
				if(px <= other.px + 4 &&
				   px + 4 >= other.px &&
				   py <= other.py + 4 &&
				   py + 4 >= other.py) 
				{
					duringCollision = true;
					collisionDuration = 10;
					other.duringCollision = true;
					other.collisionDuration = 10;
				} 
			}
		}
	}
	
	public void Collide() 
	{
		px = lastX;
		py = lastY;
		return;
	}
	
	public void Draw(Graphics g) {
		g.setColor(this.color);
		g.fillOval((int)this.px, (int)this.py, 6, 6);
	}
	
}
