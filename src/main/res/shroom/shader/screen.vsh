#version 330

layout (location=0) in vec2 a_position;

out vec2 v_texcoord;

uniform mat4 u_model;

void main()
{
    mat4 mat = u_model;
    gl_Position = vec4(a_position, 0.0, 1.0);
    v_texcoord = vec2((a_position.x + 1.0) / 2.0, 1.0 - (a_position.y + 1.0) / 2.0);
}