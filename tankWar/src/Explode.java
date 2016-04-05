import java.awt.Color;
import java.awt.Graphics;

public class Explode {
	int x,y;
	private tankwar1 tc;
	private boolean live = true;
	
	public Explode (int x,int y,tankwar1 tc){
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	int[] diameter = {4,7,18,26,38,49,30,14,6};
	int step = 0;
	
	public void draw(Graphics g){
		if(!live){
			tc.explodes.remove(this);
			return;
		}
		
		if(step == diameter.length){
			live = false;
			step = 0;
			return;
			}
		
		Color c = g.getColor();
		g.setColor(Color.ORANGE);
		g.fillOval(x, y, diameter[step], diameter[step]);
		g.setColor(c);
		step++;

			
	}

}
