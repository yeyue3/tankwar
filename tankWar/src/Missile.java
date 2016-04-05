import java.awt.Color;
import java.awt.Graphics;
import java.util.List;


import java.awt.Rectangle;

public class Missile {
	public static final int XSPEED = 10,YSPEED = 10;
	
	public static final int WIDTH = 10;
	public static final int HIGH = 10;
	
	int x,y;
	private boolean live = true;
	private boolean good;
	
	Tank.Direction dir;//利用Tank里的enum定义的dir，但是注意这并不是Tank里面的dir

	private tankwar1 tc;
	
	public Missile(int x, int y, Tank.Direction dir) {//构造函数，应当是从Tank里传入位置和方向的数值
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y, Tank.Direction dir,tankwar1 tc,boolean good) {
		this(x,y,dir);
		this.tc = tc;
		this.good = good;
	}
	

	
	public void draw(Graphics g){
		if(!live)
			tc.missiles.remove(this);
		Color c = g.getColor();
		if(good){
			g.setColor(Color.BLACK);
		}
		else
			g.setColor(Color.RED);
		g.fillOval(x, y,WIDTH,HIGH);
		g.setColor(c);
		
		move();
	}
	
	private void move(){//因为传过来了，所以可以直接用了
		switch(dir){
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
		case U:
			y -= YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;		
		}
		if(x < 0 ||y < 0 || x > tankwar1.WIDTH || y > tankwar1.HIGH)
			live = false;
	}
	public boolean isLive() {
		return live;
	}
	
	public Rectangle getRect(){
		return new Rectangle(x,y,WIDTH,HIGH);
	}
	public boolean hitTank(Tank t){

		if(this.live && getRect().intersects(t.getRect()) && t.isLive() && this.good!=t.isGood()){
			if(t.isGood()){
				t.setLife(t.getLife()-20);
				//if(t.getLife() != 0)
					//return false;这样不对，除了坦克未死，其他的应该还执行，主要是子弹不要飞了
				if(t.getLife() <= 0)
					t.setLive(false);
			}
			else{
			t.setLive(false);
			}
			this.live = false;
			
			Explode e = new Explode(x,y,tc);
			tc.explodes.add(e);
			
			return true;
			}
		return false;		
	}
	public boolean hitTanks(List<Tank> tanks){
		for(int i = 0;i < tanks.size();i++){
			if(hitTank(tanks.get(i)))//这个就一直调用hitTank来比较，直到打中，hitTank方法自然也会执行，但坦克并未消失把，只是不画了
				return true;//其实这些返回目前并未用到，但是，为了方法的完整性，面向对象思想什么的
		}
		return false;
	}
	
	public boolean hitWall(Wall w){
		if(this.live && this.getRect().intersects(w.getRect())){
			this.live = false;
			return true;
		}
		return false;
			
	}
}
