#type vertex
#version 460 core
layout (location=0) in vec3 aPos;       // Position
layout (location=1) in vec4 aColor;     // Color
layout (location=2) in vec2 aUVCoords;  // UV Coordinates
layout (location=3) in float aTexID;    // Texture ID
layout (location=4) in float aEntityID; // Game Object ID

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fUVCoords;
out float fTexID;
out float fEntityID;

void main() {
    fColor = aColor;
    fUVCoords = aUVCoords;
    fTexID = aTexID;
    fEntityID = aEntityID;

    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}

    #type fragment
    #version 460 core

in vec4 fColor;
in vec2 fUVCoords;
in float fTexID;
in float fEntityID;

uniform sampler2D uTextures[16];

out vec3 color;

void main() {
    vec4 texColor = vec4(1, 1, 1, 1);
    if(fTexID > 0){
        int id = int(fTexID);
        texColor = fColor * texture(uTextures[id], fUVCoords);
    }

    if (texColor.a < 0.5) {
        discard;
    }
    color = vec3(fEntityID, fEntityID, fEntityID);
}