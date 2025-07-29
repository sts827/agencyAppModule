package kr.co.wayplus.travel.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String id;
    private String name;
    private String email;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String id, String name, String email){
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static OAuthAttributes build(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
        switch (registrationId){
            case "kakao":
                return kakaoAttributes(userNameAttributeName, attributes);
            case "naver":
                return naverAttributes(userNameAttributeName, attributes);
            case "google": default:
                return googleAttributes(userNameAttributeName, attributes);
        }
    }

    private static OAuthAttributes kakaoAttributes(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String,Object> response = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) response.get("profile");
        return OAuthAttributes.builder()
                .id(attributes.get("id").toString())
                .name((String)profile.get("nickname"))
                .email((String)response.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes naverAttributes(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String,Object> response = (Map<String, Object>) attributes.get("response");
        return OAuthAttributes.builder()
                .id((String)response.get("id"))
                .name((String)response.get("name"))
                .email((String)response.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes googleAttributes(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .id((String) attributes.get("sub"))
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
}
