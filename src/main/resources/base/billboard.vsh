#version 330

layout (location=0) in vec3 a_position;
layout (location=1) in vec2 a_texcoord;

out vec2 v_texcoord;

uniform mat4 u_model_view;
uniform mat4 u_projection;

uniform vec2 u_sprite_offset;
uniform vec2 u_sprite_scale = vec2(1, 1);

#define CYLINDRICAL 0
#define SPHERICAL 1

uniform int u_billboard_type;

void main()
{
    u_model_view[0][0] = 1.0;
    u_model_view[0][1] = 0.0;
    u_model_view[0][2] = 0.0;

    if (u_billboard_type == SPHERICAL)
    {
        u_model_view[1][0] = 0.0;
        u_model_view[1][1] = 1.0;
        u_model_view[1][2] = 0.0;
    }

    u_model_view[2][0] = 0.0;
    u_model_view[2][1] = 0.0;
    u_model_view[2][2] = 1.0;

    vec4 pos = u_model_view * vec4(a_position, 1);
    gl_Position = u_projection * pos;
    v_texcoord = vec2(a_texcoord.x * u_sprite_scale.x, a_texcoord.y * u_sprite_scale.y) + u_sprite_offset;
}