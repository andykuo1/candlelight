package boron.base.asset.assetloader;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by Andy on 6/12/17.
 */
public class AssetArguments
{
	public static final Map<String, Function<String, Object>> ARGUMENTS = new HashMap<>();

	public static void clear()
	{
		ARGUMENTS.clear();
	}

	public static void registerArgument(String argType, Function<String, Object> parseFunction)
	{
		ARGUMENTS.put(argType, parseFunction);
	}

	public static void removeArgument(String argType)
	{
		ARGUMENTS.remove(argType);
	}

	public static boolean isArgumentType(String argType)
	{
		return ARGUMENTS.containsKey(argType);
	}

	public static Object getArgument(String argType, String arg)
	{
		return ARGUMENTS.get(argType).apply(arg);
	}
}
