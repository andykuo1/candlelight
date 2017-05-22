#version 330

in vec3 exPosition;
in vec2 exTexcoord;
in vec3 exNormal;

out vec4 fragColor;

struct Light
{
    vec4 position;
    vec3 intensity;
    float attenuation;
    float ambientCoefficient;

    float coneAngle;
    vec3 coneDirection;
};

uniform mat4 worldMatrix;
uniform vec3 cameraPosition;

#define MAX_LIGHTS 10
uniform int u_light_count;
uniform Light u_light [MAX_LIGHTS];

uniform sampler2D texSampler;
uniform float shininess;
uniform vec3 specularColor;

vec3 applyLight(Light light, vec3 surfaceColor, vec3 surfaceNormal, vec3 surfacePos, vec3 surfaceToCamera, float shininess, vec3 specularColor)
{
    vec3 surfaceToLight;
    float attenuation = 1.0;
    if (light.position.w == 0.0)
    {
        //directional light
        surfaceToLight = normalize(light.position.xyz);
        attenuation = 1.0;
    }
    else
    {
        //point light
        surfaceToLight = normalize(light.position.xyz - surfacePos);
        float distanceToLight = length(light.position.xyz - surfacePos);
        attenuation = 1.0 / (1.0 + light.attenuation * pow(distanceToLight, 2));

        //cone light
        float lightToSurfaceAngle = degrees(acos(dot(-surfaceToLight, normalize(light.coneDirection))));
        if (lightToSurfaceAngle > light.coneAngle)
        {
            attenuation = 0.0;
        }
    }

    //ambient
    vec3 ambient = light.ambientCoefficient * surfaceColor.rgb * light.intensity;

    //diffuse
    float diffuseCoefficient = max(0.0, dot(surfaceNormal, surfaceToLight));
    vec3 diffuse = diffuseCoefficient * surfaceColor.rgb * light.intensity;

    //specular
    float specularCoefficient = 0.0;
    if(diffuseCoefficient > 0.0)
    {
        specularCoefficient = pow(max(0.0, dot(surfaceToCamera, reflect(-surfaceToLight, surfaceNormal))), shininess);
    }
    vec3 specular = specularCoefficient * specularColor * light.intensity;

    //linear color (color before gamma correction)
    return ambient + attenuation * (diffuse + specular);
}

void main()
{
    vec3 surfaceNormal = normalize(transpose(inverse(mat3(worldMatrix))) * exNormal);
    vec3 surfacePos = vec3(worldMatrix * vec4(exPosition, 1));
    vec4 surfaceColor = texture(texSampler, exTexcoord);
    vec3 surfaceToCamera = normalize(cameraPosition - surfacePos);

    vec3 linearColor = vec3(0, 0, 0);
    for(int i = 0; i < u_light_count; ++i)
    {
        linearColor += applyLight(u_light[i], surfaceColor.rgb, surfaceNormal, surfacePos, surfaceToCamera, shininess, specularColor);
    }

    vec3 gamma = vec3(1.0 / 2.2);
    fragColor = vec4(pow(linearColor, gamma), surfaceColor.a);
}