#version 330 core

layout (location = 0) in vec3 aPos;

layout (location = 1) in vec3 fCol;

out vec3 fC;

void main() {
    fC = fCol;
    gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);
}
