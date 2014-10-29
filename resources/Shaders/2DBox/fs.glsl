#version 150 core

in vec4 pass_Color;
in vec4 in_Position;

out vec4 out_Color;

void main(void) {
	//out_Color = pass_Color;
	gl_FragColor = pass_Color;
}