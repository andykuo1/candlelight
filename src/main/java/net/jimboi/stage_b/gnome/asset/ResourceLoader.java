package net.jimboi.stage_b.gnome.asset;

/**
 * Created by Andy on 6/9/17.
 */
@FunctionalInterface
public interface ResourceLoader<T, P extends ResourceParameter<T>>
{
	T load(P args);
}
