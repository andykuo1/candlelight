#version 330

in vec2 exTexcoord;

out vec4 fragColor;

uniform sampler2D texSampler;

void main()
{
    vec4 texcolor = texture(texSampler, exTexcoord);
	fragColor = vec4(1, 1, 1, 1) * texcolor;
}