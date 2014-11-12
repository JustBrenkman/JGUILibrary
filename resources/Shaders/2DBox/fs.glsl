#version 330 core

in vec4 pass_Color;
in vec4 in_Position;

out vec4 out_Color;

void main(void) {
	//float distance = sqrt(length(mouse_Pos.xy - gl_FragCoord.xy));
	//float attentuation = 10.0 / distance;
	//vec4 color = vec4(attentuation, attentuation, attentuation, pow(attentuation, 3));

	//gl_FragColor = color * pass_Color;

	gl_FragColor = pass_Color;
}
