import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler Class that holds the particles and updates them. Also manages mouse inputs and the ParticleView HUD for user
 * input
 * 
 *  Might be better to do calculations in radiant and display values in degrees or radiant depending on a toggle
 * @author Vadim
 *
 */
public class Handler implements MouseMotionListener, MouseListener {

	private List<Particle> particles = new ArrayList<>();

	private ParticleView pv;

	private int timer = 0;
	private boolean isPaused;

	private int vectorXa;
	private int vectorYa;
	private int vectorXb;
	private int vectorYb;
	
	private boolean isVector;
	private boolean isGravity = true;

	/**
	 * Handler constructor that initializes the ParticleView HUD
	 */
	public Handler() {
		pv = new ParticleView(710, 0, 710, 300, particles, this);
	}

	/**
	 * Updates all particles if not paused and also collides all particles if they are overlapping. Also updates the
	 * ParticleView HUD.
	 */
	public void tick() {
		if (!isPaused) {
			timer++;
			for (int i = 0; i < particles.size(); i++) {
				Particle p1 = particles.get(i);
			
				if (isGravity) {
					p1.forceAct(270.0, 9.81 * p1.getMass());
				}
				for (int j = 0; j < particles.size(); j++) {
					Particle p2 = particles.get(j);
					if (j != i) {
						if (colliding(p1,p2)) {
							p1.collide(p2);
						}
					}
				}
				p1.tick();
			}
		}
		pv.tick();
	}

	/**
	 * Renders all particles, the ParticleView HUD, the velocity vector for new particles and the timer.
	 * @param g
	 */
	public void render(Graphics g) {
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).render(g);
		}
		pv.render(g);

		g.setColor(Color.BLACK);
		if (isVector) {
			//draw arrow part of vector
			g.drawLine(vectorXa, vectorYa, 
					(int)(vectorXb - 2 * xVector(vectorXa, vectorXb)),
					(int)(vectorYb - 2 * yVector(vectorYa, vectorYb)));			
		}

		pv.rendertime(g, timer);
		pv.renderGravity(g, isGravity);
	}

	//Static methods----------------------------------------------------------------------------------------------------

	/**
	 * Return the angle in degrees between 2 points
	 * 
	 * Could be improved by taking in 2 Point objects instead. Find slope and convert to angle.
	 * @param x1 X coordinate of the first point
	 * @param y1 Y coordinate of the first point
	 * @param x2 X coordinate of the second point
	 * @param y2 Y coordinate of the second point
	 * @return the Angle formed by the 2 points.
	 */
	private static double angle(double x1, double y1, double x2, double y2) {

		if (-xVector(x1, x2) > 0) {
			return (180 / Math.PI) * Math.atan(yVector(y1, y2) / -xVector(x1, x2));
		} else {
			return (180 / Math.PI) * Math.atan(yVector(y1, y2) / -xVector(x1, x2)) + 180;
		}

	}
	
	/**
	 * Return true the 2 circles overlap 
	 * 
	 * @param x1 Top left x coordinate of first circle
	 * @param y1 Top left y coordinate of first circle
	 * @param width1 Diameter of the first coordinate
	 * @param x2 Top left x coordinate of second circle
	 * @param y2 Top left y coordinate of second circle
	 * @param width2 Diameter of the second coordinate
	 * @return if two circles overlap
	 */
	private static boolean colliding(double x1, double y1, double width1, double x2, double y2, double width2) {
		if ((x2 > x1 - width2 && x2 < x1 + width1) && (y2 > y1 - width2 && y2 < y1 + width1)) {
			return true;
		}

		if ((x1 > x2 - width1 && x1 < x2 + width2) && (y1 > y2 - width1 && y1 < y2 + width2)) {
			return true;
		}

		return false;	
	}

	/**
	 * Returns true if the two particles overlap.
	 * 
	 * @param p1 First particle
	 * @param p2 Second particle
	 * @return if the two particles overlap
	 */
	private static boolean colliding (Particle p1, Particle p2) {
		return colliding(p1.getX(), p1.getY(), p1.getWidth(), p2.getX(), p2.getY(), p2.getWidth());	
	}

	/**
	 * Returns the distance between two points
	 * @param x1 x coordinate of the first point
	 * @param y1 y coordinate of the first point
	 * @param x2 x coordinate of the second point
	 * @param y2 y coordinate of the second point
	 * @return The distance between two points
	 */
	private static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}

	/**
	 * Returns the x component between two points
	 * @param x1 x coordinate of first point
	 * @param x2 x coordinate of second point
	 * @return X component between two points
	 */
	private static double xVector(double x1, double x2) {
		return (x2 - x1);
	}

	/**
	 * Returns the y component between two points
	 * @param y1 y coordinate of first point
	 * @param y2 y coordinate of second point
	 * @return Y component between two points
	 */
	private static double yVector(double y1, double y2) {
		return (y2 - y1);
	}

	//End of Static methods---------------------------------------------------------------------------------------------
	
	//Getters and Setters-----------------------------------------------------------------------------------------------

	public void pauseAction() {
		isPaused = !isPaused;
	}

	public boolean isPaused() {
		return isPaused;
	}

	public void toggleGravity() {
		isGravity = !isGravity;
	}

	public boolean isGravity() {
		return isGravity;
	}
	
	//End of Getters and Setters----------------------------------------------------------------------------------------

	//MouseMotionListener, MouseListener methods------------------------------------------------------------------------
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1 && e.getX() < 700) {
			isVector = true;
			vectorXa = e.getX();
			vectorYa = e.getY();
			vectorXb = e.getX();
			vectorYb = e.getY();
		} else if (e.getButton() == 3) {
			for (int i = 0; i < particles.size(); i++) {
				Particle p = particles.get(i);
				if (colliding(p.getX(), p.getY(), p.getWidth(), e.getX(), e.getY(), 0)) {
					for (int j = 0; j < particles.size(); j++) {
						particles.get(j).unSelect();
					}
					p.select();
				}
			}
		} else if (e.getButton() == 1 && e.getX() >= 700) {
			pv.click(e.getX(), e.getY());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == 1 && isVector) {
			Particle p = new Particle(vectorXa, vectorYa);

			double angle = angle(vectorXa, vectorYa, vectorXb, vectorYb);
			if (Double.isNaN(angle)) {
				angle = 0;
			}
			double force = distance(vectorXa, vectorYa, vectorXb, vectorYb);
			p.forceAct(angle, force);
			particles.add(p);
			isVector = false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		vectorXb = e.getX();
		vectorYb = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {}
	//End of MouseMotionListener, MouseListener methods-----------------------------------------------------------------
}
