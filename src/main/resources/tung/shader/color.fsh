#version 330

in vec3 v_position;

out vec4 frag_color;

uniform bool u_transparency;
uniform vec4 u_diffuse_color = vec4(1, 1, 1, 0);

void main()
{
    vec4 surfaceColor = u_diffuse_color;

    frag_color = surfaceColor;
}