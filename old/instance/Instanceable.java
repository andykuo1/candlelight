package org.qsilver.instance;

import java.util.List;

/**
 * Created by Andy on 4/30/17.
 */
public interface Instanceable
{
	void onInstanceSetup(List<Instance> instances);
	void onInstanceUpdate(Instance instance);
}
