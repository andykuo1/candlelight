#version 330

layout (location=0) in vec3 a_position;
layout (location=1) in vec2 a_texcoord;

out vec2 v_texcoord;

uniform vec2 u_tex_offset;
uniform vec2 u_tex_scale = vec2(1, 1);

uniform mat4 u_model_view_projection;

void main()
{
    gl_Position = u_model_view_projection * vec4(a_position, 1.0);

    v_texcoord = vec2(a_texcoord.x * u_tex_scale.x, a_texcoord.y * u_tex_scale.y) + u_tex_offset;
}