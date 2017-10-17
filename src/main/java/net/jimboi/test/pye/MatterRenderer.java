package net.jimboi.test.pye;

import org.joml.Vector2f;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Created by Andy on 10/17/17.
 */
public class MatterRenderer
{
	private static final Color COLOR_OUTLINE = Color.WHITE;

	public void draw(Graphics g, Matter matter)
	{
		int centerX = (int) matter.posX;
		int centerY = (int) matter.posY;
		int radius = (int) matter.radius;
		int size = radius * 2;
		float dir = matter.rotation;

		int bodyFromX = centerX - radius;
		int bodyFromY = centerY - radius;

		g.setColor(matter.getColor());
		g.fillOval(bodyFromX, bodyFromY, size, size);

		g.setColor(COLOR_OUTLINE);
		g.drawOval(bodyFromX, bodyFromY, size, size);
		g.drawLine(centerX, centerY,
				centerX + (int) (Math.cos(dir) * radius),
				centerY + (int) (Math.sin(dir) * radius));

		if (matter instanceof Pye)
		{
			Pye pye = (Pye) matter;
			for(Appendage appendage : pye.getAppendages())
			{
				if (appendage == null) continue;
				int i = pye.indexOf(appendage);
				Vector2f vec = pye.getPosition(i, new Vector2f());
				g.setColor(Color.GREEN);
				g.fillOval((int) vec.x() - 2, (int) vec.y() - 2, 4, 4);
			}
		}
	}
}
