#version 330

in vec2 v_texcoord;

out vec4 frag_color;

uniform sampler2D u_sampler;
uniform vec4 u_color = vec4(1, 1, 1, 0);

void main()
{
    vec4 textureColor = texture(u_sampler, v_texcoord);
    vec4 finalColor = vec4(u_color.rgb, 1) * u_color.a;
    finalColor += vec4(textureColor.rgb, 1) * (1.0 - u_color.a);
    finalColor.a = textureColor.a;
    frag_color = finalColor;
}