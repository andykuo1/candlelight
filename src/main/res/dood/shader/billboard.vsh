#version 330

layout (location=0) in vec3 a_position;
layout (location=1) in vec2 a_texcoord;

out vec2 v_texcoord;

uniform mat4 u_model_view_projection;
uniform mat4 u_model_view;

uniform mat4 u_model;
uniform mat4 u_view;
uniform mat4 u_projection;

#define CYLINDRICAL 0
#define SPHERICAL 1

uniform int u_billboard_type;

void main()
{
    mat4 mvp = u_model_view_projection;
    mat4 mv = u_model_view;
    mat4 m = u_model;
    mat4 v = u_view;
    mat4 p = u_projection;

    mv[0][0] = 1.0;
    mv[0][1] = 0.0;
    mv[0][2] = 0.0;

    if (u_billboard_type == SPHERICAL)
    {
        mv[1][0] = 0.0;
        mv[1][1] = 1.0;
        mv[1][2] = 0.0;
    }

    mv[2][0] = 0.0;
    mv[2][1] = 0.0;
    mv[2][2] = 1.0;

    vec4 pos = mv * vec4(a_position, 1);
    gl_Position = p * pos;
    v_texcoord = a_texcoord;
}