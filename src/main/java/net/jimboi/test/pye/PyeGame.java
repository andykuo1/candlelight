package net.jimboi.test.pye;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Created by Andy on 10/15/17.
 */
public class PyeGame
{
	public static void main(String[] args)
	{
		int width = 640;
		int height = 480;

		GraphicsPanel panel = new GraphicsPanel();

		JFrame frame = new JFrame();
		frame.setSize(width, height);
		frame.setPreferredSize(new Dimension(width, height));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - width / 2, dim.height / 2 - height / 2);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				frame.dispose();
				System.exit(0);
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
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_SPACE)
				{
					GameWorld world = new GameWorld();
					init(world);
					panel.setWorld(world);
				}
			}
		});
		frame.setContentPane(panel);
		frame.setVisible(true);

		while(true)
		{
			GameWorld world = panel.getWorld();
			if (world != null)
			{
				world.update();
				panel.repaint();
			}

			try
			{
				Thread.sleep(100);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private static void init(GameWorld world)
	{
		for(int i = 0; i < 10; ++i)
		{
			world.addGameObject(new Pye(200 + (float) (200 * Math.random()), 200 + (float) (200 * Math.random())));
		}
		world.addGameObject(new Rock(400, 400));
		world.addGameObject(new Rock(300, 200));
		world.addGameObject(new Rock(300, 300));
	}
}
