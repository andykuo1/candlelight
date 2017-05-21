package net.jimboi.mod3.blob;

import org.bstone.camera.PerspectiveCamera;
import org.qsilver.render.Material;
import org.qsilver.render.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 4/30/17.
 */
public class Renderer
{
	public static final List<Light> lights = new ArrayList<>();
	public static PerspectiveCamera camera;

	public static Model currentModel;
	public static Material currentMaterial;
}
