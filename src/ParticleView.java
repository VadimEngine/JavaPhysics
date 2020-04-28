import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;

public class ParticleView {

	private int x;
	private int y;
	private int height;
	private int width;

	private ArrayList<Particle> p;
	private Particle selectedP = null;

	private Handler handler;

	private static DecimalFormat df = new DecimalFormat("#.00"); 

	public ParticleView(int x, int y, int height, int width, ArrayList<Particle> p, Handler handler) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.p = p;
		this.handler = handler;
	}

	public void tick() {
		for (int i = 0; i < p.size(); i++) {
			if (p.get(i).isSelected()) {
				selectedP = p.get(i);
			}
		}

	}

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
