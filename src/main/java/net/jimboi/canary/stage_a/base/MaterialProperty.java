package net.jimboi.canary.stage_a.base;

import org.bstone.material.property.PropertyBool;
import org.bstone.material.property.PropertyMat4;
import org.bstone.material.property.PropertyTexture;
import org.bstone.material.property.PropertyVec2;
import org.bstone.material.property.PropertyVec4;

/**
 * Created by Andy on 10/31/17.
 */
public class MaterialProperty
{
	public static final PropertyMat4 MODELVIEWPROJECTION = new PropertyMat4("u_model_view_projection");
	public static final PropertyTexture TEXTURE = new PropertyTexture("u_sampler", 0);
	public static final PropertyVec4 COLOR = new PropertyVec4("u_color");

	public static final PropertyVec2 SPRITE_OFFSET = new PropertyVec2("u_sprite_offset");
	public static final PropertyVec2 SPRITE_SCALE = new PropertyVec2("u_sprite_scale");
	public static final PropertyBool TRANSPARENCY = new PropertyBool("u_transparency");

}
