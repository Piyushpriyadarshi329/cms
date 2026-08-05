package com.contraflow.cms.tenant.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantThemeRequest {

    private String theme;
    private String primaryColor;
    private String font;
    private String appLogo;

    private String loginHeader;
    private String loginMessage;

    private String sidebarColor;
    private String sidebarTextColor;
    private String sidebarActiveTextColor;
    private String sidebarLogoUrl;
    private String sidebarMiniLogoUrl;
    private Boolean sidebarAccordion;
}
