#version 330 core

layout(points) in;
layout(line_strip, max_vertices = 2) out;

in vec4 out_Position;

void main() {
    //gl_Position = gl_in[0].gl_Position + vec4(-1, 0.0, 0.0, 0.0);
    gl_Position = out_Position + vec4(-1.0, 0.0, 0.0, 0.0);
    //out_Position = in_Position + vec4(-10.0, 0.0, 0.0, 0.0);
    EmitVertex();

    //gl_Position = gl_in[0].gl_Position + vec4(1, 0.0, 0.0, 0.0);
    gl_Position = out_Position + vec4(1.0, 0.0, 0.0, 0.0);
    //out_Position = in_Position + vec4(10.0, 0.0, 0.0, 0.0);
    EmitVertex();

    EndPrimitive();
} 
