#version 330

layout (location=0) in vec3 a_position;

out vec3 v_position;

uniform mat4 u_model_view_projection;

void main()
{
    gl_Position = u_model_view_projection * vec4(a_position, 1.0);
    v_position = a_position;
}