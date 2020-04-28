import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Particle {
	
	private ArrayList<Force> forces = new ArrayList<>();
	
	int time = 0;
	
	private double xAccel = 0;
	private double yAccel = 0;
	
	private double presXAccel;
	private double presYAccel;
	
	private double xVel = 0;
	private double yVel = 0;
	
	private double x;
	private double y;
	
	private double mass = 1;//utilize mass
	
	private double width = 15;
	
	private boolean isSelected;
	
	private boolean showVector;
	
	private Color color = Color.BLACK;
	
	public Particle(double x, double y) {
		this(x, y, 0, 0);
	}
	
	public Particle(double x, double y, double initxVel, double intiyVel) {
		this.x = x;
		this.y = y;
		xVel = initxVel;
		yVel = intiyVel;
	}
	
	public void tick() {
		//calculate accelleration from the array of forces
		
		for (int i = 0; i < forces.size(); i++) {
			yAccel += forces.get(i).getYMagnitude() / mass;
			xAccel += forces.get(i).getXMagnitude() / mass;
		}
		
		//System.out.println((int)xAccel + ", " + (int)yAccel);
		
		presYAccel = yAccel;
		presXAccel = xAccel;
		
		xVel += xAccel;
		yVel += yAccel;
		time++;
		x += xVel;
		y += yVel; 
		
		if (y > 700) {
			y = 700;
			//forceAct(90, (yAccel * 500));
			//forceAct(360 - Math.atan(yAccel / xAccel) * (180 / Math.PI), Math.sqrt(xAccel * xAccel + yAccel * yAccel) * 100);
			yVel = - yVel * .9;
			//yAccel = 0;
		}
		
		if (x > 700) {
			x = 700;
			xVel = - xVel * .9;
			//xAccel = 0;
		}
		
		if (x < 10) {
			x = 10;
			xVel = - xVel * .9;
			//xAccel = 0;
		}
		
		xAccel = 0;
		yAccel = 0;
		//clear the forces
		forces.clear();
	}
	
	public void render(Graphics g) {
		//draw force vectors
		g.setColor(color);
		g.fillOval((int)(x), (int)(y), 15, 15);
		
		if (isSelected) {
			g.setColor(Color.RED);
			g.drawRect((int)x, (int)y, (int)width, (int)width);
			renderVector(g, presXAccel, presYAccel);
		}
	}
	
	/**
	 * 
	 * @param dir direction in degrees
	 * @param force
	 */
	public void forceAct(double dir, double force) {//magnitude
		//add the forces to the array
		force /= (60);
		forces.add(new Force(dir, force));
		//double xAccel = (force * Math.cos(dir * (Math.PI / 180.0)));
		//double yAccel = -(force * Math.sin(dir * (Math.PI / 180.0)));
		
		//this.xAccel += xAccel;
		//this.yAccel += yAccel;
		
		//xVel += xAccel;   
		//yVel += yAccel;
	}
	
	public void collide(Particle p2) {//proper momentum calculation with masses
//		double new momentum 
		double newxVel = (xVel + p2.xVel) / 2;
		double newyVel = (yVel + p2.yVel) / 2;
		xVel = p2.xVel = newxVel;
		yVel = p2.yVel = newyVel;
	}
	
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getxVel() {
		return xVel;
	}
	
	public double getyVel() {
		return yVel;
	}
	
	public double getxAccel() {
		return xAccel;
	}
	
	public double getyAccel() {
		return yAccel;
	}
	
	public double getMass() {
		return mass;
	}
	
	public double getWidth() {
		return width;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void select() {
		isSelected = true;
	}
	
	public void unSelect() {
		isSelected = false;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public void setMass(double mass) {
		this.mass = mass;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void setXVel(double xVel) {
		this.xVel = xVel;
	}
	
	public void setYVel(double yVel) {
		this.yVel = yVel;
	}
	
	public void setXAccel(double xAccel) {
		this.xAccel = xAccel;
	}
	
	public void setYAccel(double yAccel) {
		this.yAccel = yAccel;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	private void renderVector(Graphics g, double xVec, double yVec) {
		
		System.out.println((xVec * 60 + ", " + yVec * 60));
		
		g.drawLine((int)(x + (width / 2)), (int)(y + (width / 2)), (int)(x + (width / 2) + (xVec * 60)), (int)(y + (width / 2) + (yVec * 60)));
	}
	
	private class Force {
		
		private double dir;
		private double magnitude;
		
		
		private Force(double dir, double magnitude) {
			this.dir = dir;
			this.magnitude = magnitude;
		}
		
		public double getDir() {
			return dir;
		}
		
		public double getMagnitude() {
			return magnitude;
		}
		
		public double getYMagnitude() {
			return -(magnitude * Math.sin(dir * (Math.PI / 180.0)));

		}
		
		public double getXMagnitude() {
			return (magnitude * Math.cos(dir * (Math.PI / 180.0)));

		}
	}
	

}
