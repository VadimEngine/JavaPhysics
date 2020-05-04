import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;

/**
 * HUD for the application, allows setting/viewing values for a selected particle, pausing the application,
 * toggling gravity, and selecting next/previous particles 
 * 
 * Could improve by having a selectedParicle field as well as index instead of iterating the particles to find
 * the selected particle
 * @author Vadim
 *
 */
public class ParticleView {

	private int x;
	private int y;
	private int height;
	private int width;

	private List<Particle> p;
	private Particle selectedP = null;

	private Handler handler;

	private static DecimalFormat df = new DecimalFormat("#.00"); 

	/**
	 * Constructor that initializes the dimensions and related fields.
	 * @param x Top left x position of the HUD 
	 * @param y Top left y position of the HUD
	 * @param height Height of the HUD
	 * @param width Width of the HUD
	 * @param p The environment particles
	 * @param handler The handler
	 */
	public ParticleView(int x, int y, int height, int width, List<Particle> p, Handler handler) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.p = p;
		this.handler = handler;
	}

	/**
	 * Sets the selected particle which has its information displayed and allow for editing
	 */
	public void tick() {
		for (int i = 0; i < p.size(); i++) {
			if (p.get(i).isSelected()) {
				selectedP = p.get(i);
				break;
			}
		}

	}

	/**
	 * Renders the HUD and particle information and buttons for editing.
	 * @param g
	 */
	public void render(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(x, y, width, height);

		g.setColor(Color.WHITE);
		g.drawLine(x, y + 20, x+ width, y + 20);

		g.drawString("Particle Observer", x + width/3, y + 16);
		g.drawRect(x + 253, y + 24, 40, 18);
		g.drawString("Next", x + 256, y + 38);

		g.drawRect(x + 14, y + 24, 62, 18);
		g.drawString("Previous", x + 16, y + 38);

		if (selectedP != null) {
			renderPInfo(g);
		}
	}

	/**
	 * Calls the action for button in the HUD, such as edits for the particle, toggle gravity, pause time, select
	 * next/previous particles
	 * 
	 * Try catch could be removed if a selected particle feature is improved, and buttons are decoupled. Can
	 * iterate all buttons and call their action if click is inbound
	 * 
	 * @param x The x coordinate of the click action
	 * @param y The y coordinate of the click action
	 */
	public void click(int x, int y) {
		try {
			if (x >= this.x + 253 && x <= this.x + 253 + 40 && y > this.y + 24 && y < this.y + 24 + 18) {
				selectNext();
			} else if (x >= this.x + 14 && x <= this.x + 14 + 62 && y > this.y + 24 && y < this.y + 24 + 18) {
				selectPrev();
			} else if (x >= this.x + 145 && x <= this.x + 145 + 128 && y > this.y + 67 && y < this.y + 67 + 14) {
				double input = Double.valueOf(JOptionPane.showInputDialog("input mass"));
				selectedP.setMass(input);
			} else if (x >= this.x + 145 && x <= this.x + 145 + 128 && y > this.y + 87 && y < this.y + 87 + 14) {
				double input = Double.valueOf(JOptionPane.showInputDialog("input x"));
				selectedP.setX(input);
			} else if (x >= this.x + 145 && x <= this.x + 145 + 128 && y > this.y + 107 && y < this.y + 107 + 14) {
				System.out.println("Set y");
				double input = Double.valueOf(JOptionPane.showInputDialog("input y"));
				selectedP.setY(input);
			} else if (x >= this.x + 145 && x <= this.x + 145 + 128 && y > this.y + 127 && y < this.y + 127 + 14) {
				double input = Double.valueOf(JOptionPane.showInputDialog("input x velocity"));
				selectedP.setXVel(input);
			} else if (x >= this.x + 145 && x <= this.x + 145 + 128 && y > this.y + 147 && y < this.y + 147 + 14) {
				double input = Double.valueOf(JOptionPane.showInputDialog("input y velocity"));
				selectedP.setYVel(input);
			} else if (x >= this.x + 145 && x <= this.x + 145 + 128 && y > this.y + 167 && y < this.y + 167 + 14) {
				double input = Double.valueOf(JOptionPane.showInputDialog("input x acceleration"));
				selectedP.setXAccel(input);
			} else if (x >= this.x + 145 && x <= this.x + 145 + 128 && y > this.y + 187 && y < this.y + 187 + 14) {
				double input = Double.valueOf(JOptionPane.showInputDialog("input y acceleration"));
				selectedP.setYAccel(input);
			} else if (x >= this.x + 145 && x <= this.x + 145 + 128 && y > this.y + 207 && y < this.y + 207 + 14) {
				Color color = JColorChooser.showDialog(null,
						"A Color Chooser", selectedP.getColor());
				selectedP.setColor(color);
			} else if (x >= this.x + 120 && x <= this.x + 120 + 65 && y > this.y + 657 && y < this.y + 657 + 16) {
				handler.pauseAction();
			} else if (x >= this.x + 85 && x <= this.x + 85 + 125 && y > this.y + 681 && y < this.y + 681 + 16) {
				handler.toggleGravity();
			}
		} catch(Exception e) {
			//e.printStackTrace();
			System.out.println("Invalid input");
		}

	}

	/**
	 * Selects the next particle in the list if one is available
	 */
	private void selectNext() {//better if statements
		if (p.size() >= 2) {
			for (int i = 0; i < p.size(); i++) {
				if (p.get(i).isSelected()) {
					p.get(i).unSelect();
					if (i == p.size() - 1) {
						p.get(0).select();
						System.out.println("zero is p because i - 1 = " + (i - 1));
						return;
					} else {
						p.get(i + 1).select();
						System.out.println(i + 1 + " is p");
						return;
					}
				}
			}
		} 
		if (p.size() >= 1) {
			p.get(0).select();
		}

	}
	
	/**
	 * Selects the previous particle in the list if one is available
	 */
	private void selectPrev() {//better if statements
		if (p.size() >= 2) {
			for (int i = 0; i < p.size(); i++) {
				if (p.get(i).isSelected()) {
					p.get(i).unSelect();
					if (i == 0) {
						p.get(p.size() - 1).select();
						System.out.println(p.size() - 1 + "is p");
						return;
					} else {
						p.get(i - 1).select();
						System.out.println(i - 1 + " is p");
						return;
					}
				}
			}
		} 

		if (p.size() >= 1) {
			p.get(0).select();
		}

	}

	/**
	 * Renders the information of the selected particle.
	 * 
	 * Should return if there is not selected particle.
	 * @param g The Application Graphics object
	 */
	private void renderPInfo(Graphics g) {
		g.drawString("mass: " + df.format(selectedP.getMass()), x +10, y + 80);
		g.drawString("x: " + df.format(selectedP.getX()), x +10, y + 100);
		g.drawString("y: " + df.format(selectedP.getY()), x +10, y + 120);
		g.drawString("x-Velocity: " + df.format(selectedP.getxVel()), x +10, y + 140);
		g.drawString("y-Velocity: " + df.format(selectedP.getyVel()), x +10, y + 160);
		g.drawString("x-Acceleration: " + df.format(selectedP.getxAccel()), x +10, y + 180);
		g.drawString("y-Acceleration: " + df.format(selectedP.getyAccel()), x +10, y + 200);

		g.drawString("Set mass", x + 150, y + 80);
		g.drawRect(x + 145, y + 67, 128, 14);
		g.drawString("Set x", x + 150, y + 100);
		g.drawRect(x + 145, y + 87, 128, 14);
		g.drawString("Set y", x + 150, y + 120);
		g.drawRect(x + 145, y + 107, 128, 14);
		g.drawString("Set x-Velocity", x + 150, y + 140);
		g.drawRect(x + 145, y + 127, 128, 14);
		g.drawString("Set y-Velocity", x + 150, y + 160);
		g.drawRect(x + 145, y + 147, 128, 14);
		g.drawString("Set x-acceleration", x + 150, y + 180);
		g.drawRect(x + 145, y + 167, 128, 14);
		g.drawString("Set y-acceleration", x + 150, y + 200);
		g.drawRect(x + 145, y + 187, 128, 14);
		g.drawString("Set Color", x + 150, y + 220);
		g.drawRect(x + 145, y + 207, 128, 14);
	}

	/**
	 * Renders seconds into Hour:Minute:Seconds
	 * 
	 * @param g The Application Graphics object
	 * @param time The time in seconds to print, converted to Hour:Minute:Seconds
	 */
	public void rendertime(Graphics g, int time) {
		int seconds = (time / 60) % 60;
		int minutes = (time / 60 / 60) % 60;
		int hours = (seconds / 60 / 60 / 60) % 60;

		g.setColor(Color.WHITE);
		g.drawString(hours + ":" + minutes + ":" + seconds, x + 130, y + 650);
		String string;
		if (handler.isPaused()) {
			string = "resume";
		} else {
			string = "pause";
		}
		g.drawString(string, x + 125, 670);
		g.drawRect(x + 120, y + 658, 65, 16);

	}

	/**
	 * Renders the Toggle gravity button
	 * 
	 * Should be handled in the render() method and pull the grav from the handler object
	 * 
	 * @param g The Graphics Object
	 * @param grav If gravity is enabled
	 */
	public void renderGravity(Graphics g, boolean grav) {
		String string;
		if (grav) {
			string = "Disable Gravity";
		} else {
			string = "Enable  Gravity";
		}
		g.drawString(string, x + 98, y + 695);
		g.drawRect(x + 85, y + 681, 125, 16);
	}

}