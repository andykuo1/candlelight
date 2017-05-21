#version 150

in vec3 a_position;
in vec2 a_texcoord;
in vec3 a_normal;

out vec3 v_position;
out vec2 v_texcoord;
out vec3 v_normal;

uniform mat4 u_projection;
uniform mat4 u_view;
uniform mat4 u_model;

void main()
{
    gl_Position = u_projection * u_view * u_model * vec4(a_position, 1.0);
    
    v_position = a_position;
    v_texcoord = a_texcoord;
    v_normal = a_normal;
}
