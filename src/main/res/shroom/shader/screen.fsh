#version 330

in vec2 v_texcoord;

out vec4 frag_color;

uniform sampler2D u_sampler;

void main()
{
    vec4 texcolor = texture(u_sampler, v_texcoord);
	frag_color = vec4(1, 1, 1, 1);
}