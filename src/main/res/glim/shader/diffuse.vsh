#version 330

layout (location=0) in vec3 a_position;
layout (location=1) in vec2 a_texcoord;
layout (location=2) in vec3 a_normal;

out vec3 v_position;
out vec2 v_texcoord;
out vec3 v_normal;

uniform mat4 u_model_view_projection;

uniform mat4 u_model;
uniform mat4 u_view;
uniform mat4 u_projection;

uniform vec2 u_tex_offset;
uniform vec2 u_tex_scale = vec2(1, 1);

void main()
{
    mat4 mvp = u_model_view_projection;
    mat4 m = u_model;
    mat4 v = u_view;
    mat4 p = u_projection;

    gl_Position = mvp * vec4(a_position, 1.0);
    v_position = a_position;
    v_texcoord = vec2(a_texcoord.x * u_tex_scale.x, a_texcoord.y * u_tex_scale.y) + u_tex_offset;
    v_normal = a_normal;
}