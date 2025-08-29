#version 330 core
out vec4 FragColor;

in vec2 fTexCoord;

uniform sampler2D texSampler;
uniform vec4 spriteColor;

void main()
{
    FragColor = spriteColor * texture(texSampler, fTexCoord);
}
