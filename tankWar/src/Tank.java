import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.*;

public class Tank {
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	
	public static final int WIDTH = 30;
	public static final int HIGH = 30;
	
	private int x,y;
	private int xOld,yOld;
	public tankwar1 tc;
	private boolean good;
	private boolean live = true;
	private int life = 100;
	private BloodBar bar= new BloodBar();
	
	public void setLife(int life) {
		this.life = life;
	}
	public int getLife() {
		return life;
	}
	public boolean isGood() {
		return good;
	}
	
	private static Random r = new Random();
	private int step = r.nextInt(12)+3;

	private boolean bR = false,bL = false,bU = false,bD = false;
	enum Direction{
		L,LU,R,RU,U,D,LD,RD,STOP
	}
	private Direction dir = Direction.STOP;
	private Direction ptdir = Direction.D;//炮筒初始方向向下
	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.xOld = x;
		this.yOld = y;
		this.good = good;
	}
	public Tank(int x,int y,boolean good,Direction dir,tankwar1 tc){
		this(x,y,good);//调用了上一个构造方法
		this.dir = dir;
		this.tc = tc;
	}
	public void draw(Graphics g ){
		if(!live){
			if(!good)
				tc.tanks.remove(this);
			return;
			}
		if(good) bar.draw(g);
		Color c = g.getColor();
		if(good)		g.setColor(Color.RED);
		else g.setColor(Color.BLUE);
		g.fillOval(x, y, WIDTH, HIGH);
		g.setColor(c);//恢复现场
		switch(ptdir){
		case L:
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HIGH/2, x, y+Tank.HIGH/2);
			break;
		case LU:
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HIGH/2, x, y);
			break;
		case R:
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HIGH/2, x+Tank.WIDTH, y+Tank.HIGH/2);
			break;
		case RU:
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HIGH/2, x+Tank.WIDTH, y);
		case U:
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HIGH/2, x+Tank.WIDTH/2, y);
			break;
		case D:
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HIGH/2, x+Tank.WIDTH/2, y+Tank.HIGH);
			break;
		case LD:
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HIGH/2, x, y+Tank.HIGH);
			break;
		case RD:
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HIGH/2, x+Tank.WIDTH, y+Tank.HIGH);
			break;
		}
		move();
	}
	
	private void stay(){
		x = xOld;
		y = yOld;
	}
	
	private void move(){
		if(!good){
			if(step == 0){
				step = r.nextInt(12)+3;
				Direction[] dirs = Direction.values();
				dir = dirs[r.nextInt(dirs.length)];
			}
			step--;
			if(r.nextInt(40)>38){
			this.fire();
			}
		}
		xOld = x;
		yOld = y;
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
		case STOP:
			break;
		}
		if(this.dir != Direction.STOP){
			this.ptdir = this.dir;
		}
		
		if(x < 0)   x = 0;
		if(y < 30)  y = 30;
		if(x > tankwar1.WIDTH - Tank.WIDTH)  x = tankwar1.WIDTH - Tank.WIDTH;
		if(y > tankwar1.HIGH - Tank.HIGH)  y = tankwar1.HIGH - Tank.HIGH;
			
	}
	
	public void keyPressed(KeyEvent e){//就这儿，写的时候一定要注意和他定义的方法写一样的，不然，他会以为你写了一个新方法，而不是覆写
		int Key = e.getKeyCode();
		switch(Key){

		case KeyEvent.VK_RIGHT:
			bR = true; 
			break;
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		}
		locateDirection();
	}

	void locateDirection(){
		if(!bR && bL && !bU && !bD)     dir = Direction.L;
		else if(!bR && bL && bU && !bD)     dir = Direction.LU;
		else if(bR && !bL && !bU && !bD)     dir = Direction.R;
		else if(bR && !bL && bU && !bD)     dir = Direction.RU;
		else if(!bR && !bL && bU && !bD)     dir = Direction.U;
		else if(!bR && !bL && !bU && bD)     dir = Direction.D;
		else if(!bR && bL && !bU && bD)     dir = Direction.LD;
		else if(bR && !bL && !bU && bD)     dir = Direction.RD;
		else dir = Direction.STOP;//开始忘了写这一句，然后释放键盘时那个函数，说多了，是泪！

	}
	
	public void keyReleased(KeyEvent e){
		int Key = e.getKeyCode();
		switch(Key){
		case KeyEvent.VK_CONTROL:
			fire();
			break;
		case KeyEvent.VK_M:
			superFire();
			break;
		case KeyEvent.VK_RIGHT:
			bR = false; 
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		}
		locateDirection();
	}
	

	public Missile fire(){
		if(!live) return null;
		int x = this.x +WIDTH/2-Missile.WIDTH/2;
		int y = this.y +HIGH/2-Missile.HIGH/2;
		Missile m = new Missile(x,y,ptdir,this.tc,good);
		tc.missiles.add(m);
		return m;
	}
	
	public Missile fire(Direction dir){//重载
		if(!live) return null;
		int x = this.x +WIDTH/2-Missile.WIDTH/2;
		int y = this.y +HIGH/2-Missile.HIGH/2;
		Missile m = new Missile(x,y,dir,this.tc,good);
		tc.missiles.add(m);
		return m;
	}
	
	public Rectangle getRect(){
		return new Rectangle(x,y,WIDTH,HIGH);
	}
	
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	
	public boolean collidesWithWall(Wall w){
		if(this.getRect().intersects(w.getRect())){
			this.stay();
			return true;
		}
		return false;
	}
	public boolean collidesWithTank(java.util.List<Tank> tanks){
		for(int i = 0;i < tanks.size();i++){
			Tank t = tanks.get(i);
			if(this != t){
				if(this.getRect().intersects(t.getRect())){
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		return false;
	}
	
	public void superFire(){
		Direction[] dirs = Direction.values();
		for(int i = 0;i < dirs.length -1;i++){
			fire(dirs[i]);
		}
	}
	private class BloodBar{
		public void draw(Graphics g){
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y-10, WIDTH, 8);
			g.setColor(Color.RED);
			int ratio = WIDTH*life/100;
			g.fillRect(x, y-10, ratio, 8);
			g.setColor(c);
		}
	}
	
	public boolean eatBlood(Blood b){
		if(this.good && this.live && b.isLive() && this.getRect().intersects(b.getRect())){
			if(life!=100)
				life+=20;
			b.setLive(false);
			return true;
		}
		return false;
	}
}
