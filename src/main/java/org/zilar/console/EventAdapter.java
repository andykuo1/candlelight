package org.zilar.console;

/**
 * Created by Andy on 10/7/17.
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

final class EventAdapter extends WindowAdapter
		implements MouseListener, KeyListener, ActionListener
{
	private final Console console;

	EventAdapter(Console console)
	{
		this.console = console;
	}

	public <E> void addEvent(Consumer<E> consumer, E argument)
	{
		this.console.fireEvent(consumer, argument);
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		this.addEvent(this.console::onWindowCloseEvent, e);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getSource() == this.console.textContent)
		{
			this.addEvent(this.console::onContentMouseClickedEvent, e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (e.getSource() == this.console.textContent)
		{
			this.addEvent(this.console::onContentMouseReleasedEvent, e);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getSource() == this.console.inputField)
		{
			this.addEvent(this.console::onInputFieldKeyPressedEvent, e);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.console.inputField)
		{
			this.addEvent(this.console::onInputFieldActionEvent, e);
		}
	}
}
