#version 150

#moj_import <fog.glsl>
#moj_import <projection.glsl>

in vec3 Position;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec4 texProj0;
out vec3 viewPosition;
out float vertexDistance;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    viewPosition = (ModelViewMat * vec4(Position, 1.0)).xyz;

    texProj0 = projection_from_position(gl_Position);
    vertexDistance = fog_distance(Position, 0);
}
