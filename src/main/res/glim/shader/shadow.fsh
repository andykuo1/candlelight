#version 330

in vec2 v_texcoord;

out vec4 frag_color;

uniform sampler2D u_sampler;
uniform bool u_transparent = false;

void main()
{
    float alpha = texture(u_sampler, v_texcoord).a;
    if (u_transparent && alpha < 0.5)
    {
        discard;
    }
    frag_color = vec4(1.0);
}