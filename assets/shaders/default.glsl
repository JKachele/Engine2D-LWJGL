#type vertex
#version 460 core
layout (location=0) in vec2 aPos;       // Position
layout (location=1) in vec4 aColor;     // Color
layout (location=2) in vec2 aUVCoords;  // UV Coordinates
layout (location=3) in float aTexID;    // Texture ID

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fUVCoords;
out float fTexID;

void main() {
    fColor = aColor;
    fUVCoords = aUVCoords;
    fTexID = aTexID;
    gl_Position = uProjection * uView * vec4(aPos, 0.0, 1.0);
}

#type fragment
#version 460 core

in vec4 fColor;
in vec2 fUVCoords;
in float fTexID;

uniform sampler2D uTextures[16];

out vec4 color;

void main() {
    if(fTexID > 0){
        int id = int(fTexID);
        color = fColor * texture(uTextures[id], fUVCoords);
    } else {
        color = fColor;
    }
}