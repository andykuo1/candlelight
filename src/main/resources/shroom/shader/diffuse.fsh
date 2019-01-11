#version 330

in vec3 v_position;
in vec2 v_texcoord;
in vec3 v_normal;
in vec4 v_shadow;

out vec4 frag_color;

struct Light
{
    vec4 position;
    vec3 intensity;
    float attenuation;
    float ambientCoefficient;

    float coneAngle;
    vec3 coneDirection;
};

uniform mat4 u_model;
uniform vec3 u_camera_pos;

#define MAX_LIGHTS 10
uniform int u_light_count;
uniform Light u_light [MAX_LIGHTS];

uniform sampler2D u_sampler;
uniform bool u_transparency = false;
uniform float u_shininess = 0.0;
uniform vec3 u_specular_color = vec3(1, 1, 1);
uniform vec4 u_diffuse_color = vec4(1, 1, 1, 0);

uniform sampler2D u_shadow_sampler;
uniform float u_shadow_map_size;
uniform int u_pcf_kernel_size;

vec3 applyLight(Light light, vec3 surfaceColor, vec3 surfaceNormal, vec3 surfacePos, vec3 surfaceToCamera, float shininess, vec3 specularColor, float lightFactor)
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
    return ambient + attenuation * (diffuse * lightFactor + specular);
}

void main()
{
    vec4 surfaceColor;
    vec4 textureColor = texture(u_sampler, v_texcoord);
    if (u_diffuse_color.w == 1)
    {
        surfaceColor = u_diffuse_color;
    }
    else if (u_diffuse_color.w == 0)
    {
        surfaceColor = textureColor;
    }
    else
    {
        surfaceColor = mix(u_diffuse_color, textureColor, u_diffuse_color.w);
    }
    if (u_transparency && surfaceColor.a < 0.5) discard;

    float texelSize = 1.0 / u_shadow_map_size;
    float texelCount = pow(u_pcf_kernel_size * 2 + 1.0, 2);
    float total = 0.0;

    for(int x = -u_pcf_kernel_size; x <= u_pcf_kernel_size; x++)
    {
        for(int y = -u_pcf_kernel_size; y <= u_pcf_kernel_size; y++)
        {
            float objNearestLight = texture(u_shadow_sampler, v_shadow.xy + vec2(x, y) * texelSize).r;
            if (v_shadow.z > objNearestLight + 0.002)
            {
                total += 1.0;
            }
        }
    }

    total /= texelCount;

    float lightFactor = clamp(1.0 - (total * v_shadow.w), 0.0, 1.0);

    vec3 surfaceNormal = normalize(transpose(inverse(mat3(u_model))) * v_normal);
    vec3 surfacePos = vec3(u_model * vec4(v_position, 1));
    vec3 surfaceToCamera = normalize(u_camera_pos - surfacePos);

    vec3 linearColor = vec3(0, 0, 0);
    for(int i = 0; i < u_light_count; ++i)
    {
        linearColor += applyLight(u_light[i], surfaceColor.rgb, surfaceNormal, surfacePos, surfaceToCamera, u_shininess, u_specular_color, lightFactor);
    }

    vec3 gamma = vec3(1.0 / 2.2);

    frag_color = vec4(pow(linearColor, gamma), surfaceColor.a);
}