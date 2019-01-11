package apricot.stage_a.dood.worldgen;

import apricot.bstone.mogli.Mesh;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import apricot.qsilver.util.ArrayUtil;
import apricot.zilar.meshbuilder.MeshBuilder;
import apricot.zilar.meshbuilder.ModelUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Andy on 5/23/17.
 */
public class WorldGenHills
{
	public final List<Vector3f> vertices = new ArrayList<>();

	public void generate(Object... args)
	{
		Random random = ArrayUtil.getOrDefault(args, 0, new Random());
		int width = ArrayUtil.getOrDefault(args, 1, 200);
		int height = ArrayUtil.getOrDefault(args, 2, 10);
		float interval = ArrayUtil.getOrDefault(args, 3, 4F);

		this.vertices.clear();

		float maxSlope = 1F;

		float x = 0;
		float y = 0;

		float dy = 0;

		for (float i = 0; i < width; i += interval)
		{
			dy += (random.nextBoolean() ? -1 : 1) * (random.nextFloat() * maxSlope);
			if (dy > maxSlope) dy = maxSlope;
			if (dy < -maxSlope) dy = -maxSlope;

			x += interval;
			y += dy;

			Vector3f vec = new Vector3f(x, y, 0);
			this.vertices.add(vec);
		}
	}

	public Mesh createMeshFromVertices()
	{
		MeshBuilder mb = new MeshBuilder();
		Vector2f tex1 = new Vector2f();
		Vector2f tex2 = new Vector2f(1);

		Vector3fc prev = new Vector3f();
		Vector3f vec1 = new Vector3f();
		Vector3f vec2 = new Vector3f();
		Vector3f vec3 = new Vector3f();
		Vector3f vec4 = new Vector3f();

		for (Vector3f vec : this.vertices)
		{
			vec1.set(prev);
			vec2.set(vec);
			vec1.z = 1;
			vec2.z = 1;
			mb.addPlane(vec1, prev, vec, vec2, tex1, tex2);

			vec3.set(vec1);
			vec3.y -= 4;
			vec4.set(vec2);
			vec4.y -= 4;
			mb.addPlane(vec3, vec1, vec2, vec4, tex1, tex2);

			prev = vec;
		}
		return ModelUtil.createStaticMesh(mb.bake(false, false));
	}

	public List<Vector3f> getVertices()
	{
		return this.vertices;
	}
}
