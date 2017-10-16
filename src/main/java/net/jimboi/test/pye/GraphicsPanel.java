package net.jimboi.test.pye;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Created by Andy on 10/15/17.
 */
public class GraphicsPanel extends JPanel
{
	private GameWorld world;

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (this.world != null)
		{
			this.world.render(g);
		}
	}

	public void setWorld(GameWorld world)
	{
		this.world = world;
	}

	public GameWorld getWorld()
	{
		return this.world;
	}
}
