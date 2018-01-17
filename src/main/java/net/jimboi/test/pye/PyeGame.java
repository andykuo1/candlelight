package net.jimboi.test.pye;

import org.bstone.util.Counter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Created by Andy on 10/17/17.
 */
public class PyeGame
{
	private static final int WIDTH = 320;
	private static final int HEIGHT = 320;
	private static final Color BACKGROUND = Color.BLACK;

	private static final int TICKS_PER_SECOND = 60;
	private static final double FRAMETIME_STEP = 1000000000D / TICKS_PER_SECOND;
	private static final boolean LIMIT_FRAMERATE = true;

	private static PyeGame instance;

	private static JFrame frame;
	private static JPanel content;

	private static Queue<Renderable> renderables = new PriorityBlockingQueue<>();

	@FunctionalInterface
	interface Renderable extends Comparable<Renderable>
	{
		void onDraw(Graphics g);

		default int getDrawDepth()
		{
			return 0;
		}

		@Override
		default int compareTo(Renderable o)
		{
			return this.getDrawDepth() - o.getDrawDepth();
		}
	}

	public static <T extends Renderable> T addRenderable(T renderable)
	{
		renderables.add(renderable);
		return renderable;
	}

	public static void removeRenderable(Renderable renderable)
	{
		renderables.remove(renderable);
	}

	private static Queue<Inputable> inputables = new PriorityBlockingQueue<>();

	@FunctionalInterface
	interface Inputable extends Comparable<Inputable>
	{
		boolean onInput(int keycode, int action);

		default int getInputPriority()
		{
			return 0;
		}

		@Override
		default int compareTo(Inputable o)
		{
			return this.getInputPriority() - o.getInputPriority();
		}
	}

	public static <T extends Inputable> T addInputable(T inputable)
	{
		inputables.add(inputable);
		return inputable;
	}

	public static void removeInputable(Inputable inputable)
	{
		inputables.remove(inputable);
	}

	private static Counter updates;
	private static Counter frames;

	private static volatile boolean running = false;

	public static void main(String[] args)
	{
		final Dimension frameSize = new Dimension(WIDTH, HEIGHT);
		frame = new JFrame();
		frame.setSize(frameSize.width, frameSize.height);
		frame.setPreferredSize(frameSize);
		frame.setMinimumSize(frameSize);
		frame.setMaximumSize(frameSize);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frameSize.width / 2,
				dim.height / 2 - frameSize.height / 2);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				running = false;
			}
		});
		frame.addKeyListener(new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
			}

			@Override
			public void keyPressed(KeyEvent e)
			{
				final int keycode = e.getKeyCode();
				final int action = 1;

				Iterator<Inputable> iter = inputables.iterator();
				while(iter.hasNext())
				{
					Inputable inputable = iter.next();
					if (inputable.onInput(keycode, action))
					{
						return;
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				final int keycode = e.getKeyCode();
				final int action = 0;

				Iterator<Inputable> iter = inputables.iterator();
				while(iter.hasNext())
				{
					Inputable inputable = iter.next();
					if (inputable.onInput(keycode, action))
					{
						return;
					}
				}
			}
		});

		content = new JPanel(){
			@Override
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);

				Iterator<Renderable> iter = renderables.iterator();
				while(iter.hasNext())
				{
					Renderable renderable = iter.next();
					renderable.onDraw(g);
				}
			}
		};
		frame.setContentPane(content);

		frame.setBackground(BACKGROUND);
		content.setBackground(BACKGROUND);

		//Show frame
		frame.pack();
		frame.setVisible(true);

		//Loop variables
		double timePrevious;
		double timeLatency;
		double timeDelta;
		boolean dirty = false;

		double timeCounter;

		//Initialization
		updates = new Counter();
		frames = new Counter();

		instance = new PyeGame();

		running = true;
		timeCounter = System.currentTimeMillis();
		timePrevious = System.nanoTime();
		timeLatency = 0;

		instance.onCreate();

		//Main loop
		while (running)
		{
			final double current = System.nanoTime();
			final double elapsed = current - timePrevious;

			timePrevious = current;
			timeLatency += elapsed;

			//Fixed updates
			while (timeLatency >= FRAMETIME_STEP)
			{
				instance.onFixedUpdate();
				timeLatency -= FRAMETIME_STEP;
				dirty = true;

				updates.next();
			}
			timeDelta = timeLatency / FRAMETIME_STEP;

			//Updates
			if (dirty || !LIMIT_FRAMERATE)
			{
				instance.onUpdate(timeDelta);
				frame.repaint();
				dirty = false;

				frames.next();
			}

			//Update / Frame counters
			if (System.currentTimeMillis() - timeCounter > 1000)
			{
				timeCounter += 1000;

				System.out.println("[UPS: " + updates.poll() + " || FPS: " + frames.poll() + "]");
			}
		}

		instance.onDestroy();

		//Clean up
		renderables.clear();

		//Destroy frame
		frame.dispose();
		System.exit(0);
	}

	private PetriDish dish;
	private MatterRenderer renderer;

	public void onCreate()
	{
		this.dish = new PetriDish();
		this.renderer = new MatterRenderer();

		addRenderable(g -> {
			for(Matter matter : this.dish.getMatters())
			{
				this.renderer.draw(g, matter);
			}
		});

		addInputable((keycode, action) -> {
			if (action != 0) return false;
			if (keycode == KeyEvent.VK_SPACE)
			{
				this.dish.destroy();

				this.createMatter(this.dish);
				this.createMatter(this.dish);
				this.createMatter(this.dish);
				this.createMatter(this.dish);
				this.createMatter(this.dish);

				Matter matter = new Matter(0, 0, 0);
				matter.radius = 16;
				matter.motionX = 1;
				this.dish.addMatter(matter);

				Matter other = new Matter(100, 0, 0);
				other.radius = 16;
				other.motionX = -1;
				this.dish.addMatter(other);

				this.createPye(this.dish);
				this.createPye(this.dish);
				this.createPye(this.dish);
				this.createPye(this.dish);
				this.createPye(this.dish);

				return true;
			}
			return false;
		});
	}

	public void onDestroy()
	{
		this.dish.destroy();
	}

	public void onFixedUpdate()
	{
		this.dish.update();
	}

	public void onUpdate(double delta)
	{
	}

	public void onInputUpdate(int keycode)
	{

	}

	private Matter createMatter(PetriDish dish)
	{
		Matter matter = new Matter(rand(100, 200), rand(100, 200), rand(0, (float) Math.PI * 2F));
		matter.radius = rand(8, 16);
		matter.motionX = rand(-1, 1);
		matter.motionY = rand(-1, 1);
		matter.motionRot = rand(-1, 1);
		matter.mass = matter.radius;
		this.dish.addMatter(matter);
		return matter;
	}

	private Pye createPye(PetriDish dish)
	{
		Appendage[] appendages = new Appendage[3];
		for(int i = 0; i < appendages.length; ++i)
		{
			appendages[i] = Appendages.getRandomAppendage();
		}
		Pye pye = new Pye(appendages);
		pye.posX = rand(100, 200);
		pye.posY = rand(100, 200);
		pye.rotation = rand(0, (float) Math.PI * 2F);

		pye.radius = rand(16, 24);
		pye.mass = pye.radius;
		this.dish.addMatter(pye);
		return pye;
	}

	private static float rand(float min, float max)
	{
		return min + ((float) Math.random() * (max - min));
	}
}
