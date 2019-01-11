#version 330

layout (location=0) in vec3 a_position;
layout (location=1) in vec2 a_texcoord;
layout (location=2) in vec3 a_normal;

out vec2 v_texcoord;
out vec3 v_normal;

uniform mat4 u_model_view_projection;

uniform vec2 u_sprite_offset;
uniform vec2 u_sprite_scale = vec2(1, 1);

void main()
{
    gl_Position = u_model_view_projection * vec4(a_position, 1.0);
    v_texcoord = vec2(a_texcoord.x * u_sprite_scale.x, a_texcoord.y * u_sprite_scale.y) + u_sprite_offset;
    v_normal = a_normal;
}