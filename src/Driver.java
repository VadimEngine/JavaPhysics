import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Main class of the application, includes the main method and the main tick and render methods. 
 * Uses multi-threading to run a "run" method which controls the whole game. 
 * @author Vadim
 *
 */
public class Driver extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	private boolean running;
	private Thread thread;

	private int FPS = 0;

	private Handler handler = new Handler();

	/**
	 * Constructor, sets the size of the Canvas and sets the Handler as the mouse listener.
	 */
	public Driver() {
		Dimension size = new Dimension(1000, 700);
		setSize(size);
		setPreferredSize(size);

		setFocusable(true);
		addMouseMotionListener(handler);
		addMouseListener(handler);
	}

	public synchronized void start() {
		if (running) {
			return;
		}
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop(){
		if (!running) {
			return;
		}
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method that runs the entire game, keeps track how often the methods  called and keep them at 60 calls per
	 * second or below. Update "if (ticked)" to  "(ticked|| this != null)" to see the max FPS
	 */
	@Override
	public void run() {
		int frames = 0;
		double unprocessedSeconds = 0;
		long lastTime = System.nanoTime();
		double secondsPerTick = 1 / 60.0;
		int tickCount = 0;

		while (running) {
			long now = System.nanoTime();
			long passedTime = now - lastTime;
			lastTime = now;
			if (passedTime < 0) {
				passedTime = 0;
			}
			if (passedTime > 100000000) {
				passedTime = 100000000;
			}

			unprocessedSeconds += passedTime / 1000000000.0;

			boolean ticked = false;
			while (unprocessedSeconds > secondsPerTick) {
				tick();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;

				tickCount++;
				if (tickCount % 60 == 0) {
					FPS = frames;
					lastTime += 1000;
					frames = 0;
				}
			}
			if (ticked) {// || this != null
				render();
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * Main tick method, calls the handler tick to update the back-end particle environment 
	 */
	public void tick() {
		handler.tick();
	}

	/**
	 * Main render method, draws using triple buffering, draws the FPS as well as the application game information, 
	 *  which is gathered and created by the Handler class.
	 */
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		handler.render(g);
		g.setColor(Color.BLACK);
		g.drawString("" + FPS, 15, 15);

		g.dispose();
		bs.show();
	}

	/**
	 * The main method, initializes the Driver Canvas object to start the application, builds a Jframe with a Jpanel
	 * and adds the Driver Canvas into the panel
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		Driver driver = new Driver();

		JFrame frame = new JFrame("Physics 101: Chapter 2");
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(driver, BorderLayout.CENTER);

		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		driver.start();
	}
}
