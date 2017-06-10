package net.jimboi.mod2.asset;

/**
 * Created by Andy on 6/9/17.
 */
@FunctionalInterface
public interface ResourceLoader<T, P extends ResourceParameter<T>>
{
	T load(P args);
}
