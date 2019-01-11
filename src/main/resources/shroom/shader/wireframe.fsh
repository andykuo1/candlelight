#version 330

in vec3 v_position;

out vec4 frag_color;

uniform vec3 u_color;

void main()
{
	frag_color = vec4(u_color, 1.0);
}