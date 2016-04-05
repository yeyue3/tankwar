import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Blood {
	private int x,y;
	private static final int WIDTH = 20,HEIGHT = 20,TIME = 100;
	private boolean live = true;
	private Random r = new Random();
	private tankwar1 tc;
	private int stept = 0;
	int time = 0;
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	
	int[][] pos = {
			{5,5},{10,3},{1,0},{5,-5},{0,3},{6,4},{-1,-6},{-5,-8}
	};
	
	public Blood(tankwar1 tc) {
		x = r.nextInt(350)+50;
		y = r.nextInt(350)+50;
		this.tc = tc;
	}
	
	public void draw(Graphics g){
//在该类里，也能创造该类啊，其实就是调用构造函数对吧？
		time();
		if(live){
			Color c = g.getColor();
			g.setColor(Color.MAGENTA);
			if(stept < pos.length-1){
				stept++;
			}
			else
				stept = 0;
			g.fillRect(x+pos[stept][0], y+pos[stept][1], WIDTH, HEIGHT);
			g.setColor(c);
		}
		else
			tc.bloods.remove(this);
	}
	
	public Rectangle getRect(){
		return new Rectangle(x+pos[stept][0],y+pos[stept][1],WIDTH,HEIGHT);
	}

	public void time(){
		time++;
		if(time == TIME){
			time = 0;
			live = false;
		}
	}
}
