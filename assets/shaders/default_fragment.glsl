#version 330 core
out vec4 FragColor;

in vec3 fColor;
in vec2 fTexCoord;

uniform sampler2D texSampler;

void main()
{
    FragColor = texture(texSampler, fTexCoord);
}
