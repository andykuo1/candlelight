#version 330

in vec3 v_position;

out vec4 frag_color;

uniform vec4 u_color;

void main()
{
	frag_color = u_color;
}