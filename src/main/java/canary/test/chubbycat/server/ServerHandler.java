package canary.test.chubbycat.server;

import canary.test.chubbycat.ChubbyCat;
import canary.test.chubbycat.message.Message;
import canary.test.chubbycat.message.command.CommandHandler;
import canary.test.chubbycat.message.command.CommandNickname;

import canary.bstone.util.pair.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Created by Andy on 11/29/17.
 */
public class ServerHandler extends SimpleChannelInboundHandler<Message>
{
	public static final ChannelGroup CHANNELS = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	public static final Map<Channel, String> NAMES = new HashMap<>();
	public static final Set<CommandHandler> COMMANDS = new HashSet<>();

	static
	{
		COMMANDS.add(new CommandNickname());
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		ChubbyCat.CONSOLE.println(ctx.channel().remoteAddress() + " has joined!");
		ctx.writeAndFlush(new Message("server", "Welcome To ChubbyCat!"));
		CHANNELS.add(ctx.channel());
		NAMES.put(ctx.channel(), ctx.channel().remoteAddress().toString());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
		ChubbyCat.CONSOLE.println(NAMES.get(ctx.channel()) + " has left!");
		CHANNELS.remove(ctx.channel());
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception
	{
		if (Message.isCommand(msg))
		{
			Pair<String, String[]> cmd = Message.parseCommand(msg.getData());
			Message message = null;

			boolean flag = false;
			for(CommandHandler command : COMMANDS)
			{
				for(String name : command.getCommandNames())
				{
					if (cmd.getFirst().equals(name))
					{
						flag = true;
						break;
					}
				}

				if (flag)
				{
					if (!command.process(ctx, cmd.getFirst(), cmd.getSecond()))
					{
						message = new Message("server", command.getUsageCommand());
					}
					else
					{
						message = new Message("you", "!" + cmd.getFirst() + "(" + String.join(", ", cmd.getSecond()) + ")");
					}

					break;
				}
			}

			if (!flag)
			{
				message = new Message("server", "ERROR: Unknown command - " + cmd.getFirst());
			}

			ctx.channel().writeAndFlush(message);
			ChubbyCat.CONSOLE.println(message.toString());
		}
		else
		{
			Message out = new Message(NAMES.get(ctx.channel()), msg.getData());
			for (Channel c : CHANNELS)
			{
				if (c != ctx.channel())
				{
					c.writeAndFlush(out);
				}
				else
				{
					Message ret = new Message("you", msg.getData());
					c.writeAndFlush(ret);
				}
			}

			ChubbyCat.CONSOLE.println(out.toString());

			if ("bye".equals(msg.getData().toLowerCase()))
			{
				ctx.close();
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		cause.printStackTrace();
		ctx.close();
	}
}
