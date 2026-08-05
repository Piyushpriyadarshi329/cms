package com.contraflow.cms.tenant.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThemeConfig {

    private String theme;
    private String primaryColor;
    private String font;
    private String appLogo;

    private Login login;
    private Sidebar sidebar;

    @Data
    public static class Login {
        private String header;
        private String message;
    }

    @Data
    public static class Sidebar {
        private String color;
        private String textColor;
        private String activeTextColor;
        private String logoUrl;
        private String miniLogoUrl;
        private Boolean accordion;
    }
}
