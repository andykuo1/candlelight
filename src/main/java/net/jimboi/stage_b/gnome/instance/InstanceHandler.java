package net.jimboi.stage_b.gnome.instance;

import java.util.List;

/**
 * Created by Andy on 4/30/17.
 */
public interface InstanceHandler
{
	void onInstanceSetup(InstanceManager instanceManager, List<Instance> instances);

	void onInstanceUpdate(InstanceManager instanceManager, Instance instance);

	void onInstanceDestroy(InstanceManager instanceManager, Instance instance);
}
