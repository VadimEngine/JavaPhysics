import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * Particle object that moves according to particle physics. Collides with other particles and the bottom, left/right
 * borders. Also is pulled by gravity is it is enabled.
 * 
 * Should remove the isSelected field and the logic around it and update it to have Handler have a reference to the 
 * Selected particle since only 1 can be selected and the selected particle doesn't need to know its selected
 * @author Vadim
 *
 */
public class Particle {
	
	private ArrayList<Force> forces = new ArrayList<>();
	
	private double xAccel = 0;
	private double yAccel = 0;
	
	private double presXAccel;
	private double presYAccel;
	
	private double xVel = 0;
	private double yVel = 0;
	
	private double x;
	private double y;
	
	private double mass = 1;
	private double width = 15;
	
	/**
	 * Remove in future updates
	 */
	private boolean isSelected;
	
	private Color color = Color.BLACK;
	
	/**
	 * Constructor to set only the x and y position. The velocities are initialized to 0.
	 * @param x the X position
	 * @param y the Y position
	 */
	public Particle(double x, double y) {
		this(x, y, 0, 0);
	}
	
	/**
	 * Constructor to set only the x and y position and the x and y velocity.
	 * @param x the X position
	 * @param y the Y position
	 * @param initxVel the X Velocity
	 * @param intiyVel the Y Velocity
	 */
	public Particle(double x, double y, double initxVel, double intiyVel) {
		this.x = x;
		this.y = y;
		xVel = initxVel;
		yVel = intiyVel;
	}
	
	/**
	 * Moves the particle depending on its velocities which are recalculating based on the forces acting on it (such as 
	 * gravity). Bounces off Left/Right and bottom border.
	 */
	public void tick() {
		//calculate acceleration from the array of forces
		for (int i = 0; i < forces.size(); i++) {
			yAccel += forces.get(i).getYMagnitude() / mass;
			xAccel += forces.get(i).getXMagnitude() / mass;
		}
				
		presYAccel = yAccel;
		presXAccel = xAccel;
		
		xVel += xAccel;
		yVel += yAccel;
		x += xVel;
		y += yVel; 
		
		if (y > 700) {
			y = 700;
			yVel = - yVel * .9;
		}
		
		if (x > 700) {
			x = 700;
			xVel = - xVel * .9;
		}
		
		if (x < 10) {
			x = 10;
			xVel = - xVel * .9;
		}
		
		xAccel = 0;
		yAccel = 0;
		//clear the forces
		forces.clear();
	}
	
	/**
	 * Draws the circle at x, y of filled with the particles color. If the particle is selected, then a red box is
	 * drawn around the particle, as well as the acceleration vector.
	 * @param g the Canvas graphics object
	 */
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
	 * Add a force to the particle which will be used in the next tick collection for movement 
	 * @param dir Direction in degrees.
	 * @param force Magnitude of the force
	 */
	public void forceAct(double dir, double force) {//magnitude
		//add the forces to the array
		force /= (60);
		forces.add(new Force(dir, force));
	}
	
	/**
	 * Collides with the Particle with the inputed particle, updates both particle's velocities
	 * 
	 * Needs to be updated to take the masses into account
	 * @param p2 The particle to collide with
	 */
	public void collide(Particle p2) {//proper momentum calculation with masses
		double newxVel = (xVel + p2.xVel) / 2;
		double newyVel = (yVel + p2.yVel) / 2;
		xVel = p2.xVel = newxVel;
		yVel = p2.yVel = newyVel;
	}
	
	/**
	 * Used to render the  Particles velocity/acceleration vector when the particle is selected
	 * @param g the Canvas Graphics object
	 * @param xVec the X dimension of the vector
	 * @param yVec the Y dimension of the vector
	 */
	private void renderVector(Graphics g, double xVec, double yVec) {
		g.drawLine((int)(x + (width / 2)), (int)(y + (width / 2)), (int)(x + (width / 2) 
				+ (xVec * 60)), (int)(y + (width / 2) + (yVec * 60)));
	}
	
	
	//Getters and Setters-----------------------------------------------------------------------------------------------
	
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
	
	//End of Getters and Setters----------------------------------------------------------------------------------------
	
	/**
	 * Force object class, used for force direction and magnitude and managing force calculations
	 * @author Vadim
	 *
	 */
	private static class Force {
		
		private double dir;
		private double magnitude;
		
		
		private Force(double dir, double magnitude) {
			this.dir = dir;
			this.magnitude = magnitude;
		}
		//Getters and Setters-------------------------------------------------------------------------------------------
				
		public double getYMagnitude() {
			return -(magnitude * Math.sin(dir * (Math.PI / 180.0)));

		}
		
		public double getXMagnitude() {
			return (magnitude * Math.cos(dir * (Math.PI / 180.0)));
		}
		//End of Getters and Setters------------------------------------------------------------------------------------
	}
}