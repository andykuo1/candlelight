#version 330

in vec3 v_position;
in vec2 v_texcoord;
in vec3 v_normal;

out vec4 frag_color;

uniform sampler2D u_sampler;
uniform bool u_transparency = false;
uniform vec4 u_diffuse_color = vec4(1, 1, 1, 0);

void main()
{
    vec4 surfaceColor;
    vec4 textureColor = texture(u_sampler, v_texcoord);
    if (u_diffuse_color.w == 1)
    {
        surfaceColor = u_diffuse_color;
    }
    else if (u_diffuse_color.w == 0)
    {
        surfaceColor = textureColor;
    }
    else
    {
        surfaceColor = mix(u_diffuse_color, textureColor, u_diffuse_color.w);
    }
    if (u_transparency && surfaceColor.a < 0.5) discard;

    frag_color = surfaceColor;
}