package net.jimboi.canary.stage_a.cuplet.scene_main.gui.base;

import org.joml.Vector2f;
import org.qsilver.util.MathUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Andy on 7/15/17.
 */
public class Gui implements Comparable<Gui>
{
	GuiManager guiManager = null;
	private final List<Gui> childs = new ArrayList<>();
	private Gui parent = null;
	private int depth = 1;
	private boolean enabled = true;

	public Vector2f position = new Vector2f();

	public float width = 1;
	public float height = 1;

	protected int prevState = 0;

	private int color = 0x444444;
	private float alpha = 1;

	public Gui setPosition(float x, float y)
	{
		this.position.set(x, y);
		return this;
	}

	public Gui setSize(float width, float height)
	{
		this.width = width;
		this.height = height;
		return this;
	}

	public Gui setAlpha(float alpha)
	{
		this.alpha = MathUtil.clamp(alpha, 0, 1);
		return this;
	}

	protected void onUpdate()
	{

	}

	protected void onGuiStateChanged(int state)
	{
		switch (state)
		{
			case -1: //DISABLED
				this.color = 0x000000;
				break;
			case 1: //HOVER
				this.color = 0x888888;
				break;
			case 2: //ACTIVE
				this.color = 0x222222;
				break;
			case 3: //ACTIVATION
				this.color = 0xFFFFFF;
				break;
			case 0: //DEFAULT
			default:
				this.color = 0x444444;
				break;
		}
	}

	public void setGuiState(int state)
	{
		this.onGuiStateChanged(state);
		this.prevState = state;
	}

	public boolean isSolid(float x, float y)
	{
		return this.isVisible() && contains(this, x, y);
	}

	public boolean isSelectable(boolean active)
	{
		return true;
	}

	public final void addChild(Gui gui)
	{
		if (gui == null) return;
		if (gui == this) throw new IllegalArgumentException("Cannot make cyclical dependencies!");
		if (gui.parent != null) throw new IllegalArgumentException("Gui already has a parent!");

		gui.parent = this;
		this.childs.add(gui);
	}

	public final void removeChild(Gui gui)
	{
		if (gui.parent != this) throw new IllegalArgumentException("Gui does not belong to this parent!");

		gui.parent = null;
		this.childs.remove(gui);
	}

	public final Gui getParent()
	{
		return this.parent;
	}

	public final Iterator<Gui> getChilds()
	{
		return this.childs.iterator();
	}

	public final Gui setDepth(int offset)
	{
		this.depth = offset;
		return this;
	}

	public final int getDepth()
	{
		if (this.parent != null)
		{
			return this.parent.getDepth() + this.depth;
		}
		else
		{
			return this.depth;
		}
	}

	public final void setEnabled(boolean enabled)
	{
		if (this.enabled != enabled)
		{
			this.enabled = enabled;

			this.setGuiState(this.enabled ? 0 : -1);
		}
	}

	public final boolean isEnabled()
	{
		return this.enabled;
	}

	public float getX()
	{
		if (this.parent != null)
		{
			return this.position.x() + this.parent.getX();
		}
		else
		{
			return this.position.x();
		}
	}

	public float getY()
	{
		if (this.parent != null)
		{
			return this.position.y() + this.parent.getY();
		}
		else
		{
			return this.position.y();
		}
	}

	public int getColor()
	{
		return this.color + (this.getDepth() * 0x111111);
	}

	public float getAlpha()
	{
		return this.alpha;
	}

	public boolean isVisible()
	{
		return this.alpha > 0;
	}

	public final GuiManager getGuiManager()
	{
		return this.guiManager;
	}

	@Override
	public int compareTo(Gui o)
	{
		if (this.equals(o)) return 0;

		int diff = this.getDepth() - o.getDepth();
		return diff != 0 ? diff : this.hashCode() - o.hashCode();
	}

	public static boolean contains(Gui gui, float x, float y)
	{
		return x >= gui.getX() && y >= gui.getY() && x < gui.getX() + gui.width && y < gui.getY() + gui.height;
	}
}
