#version 330

in vec2 v_texcoord;

out vec4 frag_color;

uniform sampler2D u_sampler;
uniform bool u_transparency = false;
uniform vec4 u_color = vec4(1, 1, 1, 0);

void main()
{
    vec4 textureColor = texture(u_sampler, v_texcoord);

    if (u_transparency && textureColor.a < 0.5) discard;

    if (u_color.w == 1)
    {
        frag_color = u_color;
    }
    else if (u_color.w == 0)
    {
        frag_color = textureColor;
    }
    else
    {
        frag_color = mix(u_color, textureColor, 1.0 - u_color.w);
    }
}