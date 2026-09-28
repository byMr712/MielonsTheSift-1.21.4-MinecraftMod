#version 150

#moj_import <fog.glsl>

uniform vec2 ScreenSize;
uniform float GameTime;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in vec4 texProj0;
in vec3 viewPosition;
in float vertexDistance;

out vec4 fragColor;

float siftHash(vec2 p) {
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453);
}

float siftNoise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    return mix(mix(siftHash(i), siftHash(i + vec2(1.0, 0.0)), f.x), mix(siftHash(i + vec2(0.0, 1.0)), siftHash(i + vec2(1.0, 1.0)), f.x), f.y);
}

float siftFbm(vec2 p) {
    float n = 0.0;
    float a = 0.5;
    for (int i = 0; i < 5; i++) {
        n += a * siftNoise(p);
        p = mat2(1.6, -1.2, 1.2, 1.6) * p;
        a *= 0.5;
    }
    return n;
}

vec3 siftPortalField(vec2 uv, vec2 view, float seconds) {
    vec2 p = uv * vec2(6.0, 4.0);
    vec2 warp = vec2(siftFbm(p * 0.7 + vec2(seconds * 0.045, 3.0)), siftFbm(p * 0.7 + vec2(7.0, -seconds * 0.036))) - 0.5;
    float far = siftFbm(p * 0.75 + view * 0.13 + vec2(seconds * 0.055, seconds * 0.019));
    float middle = siftFbm(p * 1.35 + warp * 1.8 + view * 0.32 + vec2(-seconds * 0.043, seconds * 0.062));
    float near = siftFbm(p * 2.1 - warp + view * 0.62 + vec2(seconds * 0.032, -seconds * 0.049));
    float pockets = smoothstep(0.38, 0.62, far * 0.35 + middle * 0.4 + near * 0.25);
    float wisps = smoothstep(0.34, 0.66, middle) * smoothstep(0.36, 0.63, near);
    vec3 color = mix(vec3(0.94, 0.995, 1.0), vec3(0.20, 0.66, 0.76), clamp(pockets * 0.95 + wisps * 0.35, 0.0, 1.0));
    float haze = smoothstep(0.5, 0.73, siftFbm(p * 0.55 + warp + vec2(-seconds * 0.02, 0.0)));
    return mix(color, vec3(0.99, 1.0, 1.0), haze * 0.65);
}

void main() {
    vec2 uv = texProj0.xy / max(texProj0.w, 0.0001);
    uv.x = (uv.x - 0.5) * ScreenSize.x / max(ScreenSize.y, 1.0) * 0.65 + 0.5;
    vec2 ray = viewPosition.xy / max(abs(viewPosition.z), 1.0);
    vec3 color = siftPortalField(uv, ray, GameTime * 1200.0);
    fragColor = linear_fog(vec4(color, 1.0), vertexDistance, FogStart, FogEnd, FogColor);
}
