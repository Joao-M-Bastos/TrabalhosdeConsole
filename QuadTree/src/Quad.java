import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Quad {
	
	Rect boundary;
	
	int limit;
	
	ArrayList<Particle> particles = new ArrayList<Particle>();
	
	Quad[] subQuads = new Quad[4];
	
	Rect[] subRects = new Rect[4];
	
	boolean divided = false;
	
	public Quad(Rect boundary, int limit)
	{
		this.boundary = boundary;
		this.limit = limit;
	}
	
	public void Insert(Particle p) 
	{
		if (this.boundary.w > 4 && this.boundary.h > 4) {
			if(particles.size() < limit) 
			{
				particles.add(p);
				p.particles = particles;
			}
			else
			{
				if(!divided) Subdivide();
				if(subQuads[0].boundary.Contains(p)) 
				{
					subQuads[0].Insert(p);
					for(Particle pt : particles) p.particles.add(pt) ;
				}
				if(subQuads[1].boundary.Contains(p))
				{
					subQuads[1].Insert(p);
					for(Particle pt : particles) p.particles.add(pt) ;
				}
				if(subQuads[2].boundary.Contains(p))
				{
					subQuads[2].Insert(p);
					for(Particle pt : particles) p.particles.add(pt) ;
				}
				if(subQuads[3].boundary.Contains(p))
				{
					subQuads[3].Insert(p);
					for(Particle pt : particles) p.particles.add(pt) ;
				}
			}
		}
	}
	
	private void Subdivide() 
	{
		subRects[0] = new Rect(boundary.x, boundary.y, boundary.w / 2, boundary.h / 2);
		subRects[1] = new Rect(boundary.x + boundary.w / 2, boundary.y, boundary.w / 2, boundary.h / 2);
		subRects[2] = new Rect(boundary.x + boundary.w / 2, boundary.y + boundary.h / 2, boundary.w / 2, boundary.h / 2);
		subRects[3] = new Rect(boundary.x, boundary.y + boundary.h / 2, boundary.w / 2, boundary.h / 2);
		
		subQuads[0] = new Quad(subRects[0], limit);
		subQuads[1] = new Quad(subRects[1], limit);
		subQuads[2] = new Quad(subRects[2], limit);
		subQuads[3] = new Quad(subRects[3], limit);
		
		divided = true;
	}
	public void Draw(Graphics g) 
	{
		g.setColor(Color.cyan);
		g.drawRect((int)boundary.x, (int)boundary.y, (int)boundary.w, (int)boundary.h);
		for(Quad q : subQuads) if(q != null) q.Draw(g);
	}
}