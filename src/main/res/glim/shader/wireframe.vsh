#version 330

layout (location=0) in vec3 a_position;

out vec3 v_position;

uniform mat4 u_model_view_projection;
uniform mat4 u_model_view;

uniform mat4 u_model;
uniform mat4 u_view;
uniform mat4 u_projection;

uniform vec3 u_color;

void main()
{
    mat4 mvp = u_model_view_projection;
    mat4 mv = u_model_view;
    mat4 m = u_model;
    mat4 v = u_view;
    mat4 p = u_projection;

    gl_Position = mvp * vec4(a_position, 1.0);
    v_position = a_position;
}