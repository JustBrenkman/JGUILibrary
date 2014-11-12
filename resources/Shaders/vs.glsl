#version 150 core
in vec4 in_Position;
in vec4 in_Color;

out vec4 pass_Color;

uniform mat4 modelMatrix; 
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main(void) {
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * in_Position;
	pass_Color = in_Color;
}
