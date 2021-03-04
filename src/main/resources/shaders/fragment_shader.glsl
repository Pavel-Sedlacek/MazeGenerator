#version 330 core

out vec4 FragColor;

in vec3 fC;

void main() {
    FragColor = vec4(fC, 1.0);
}
