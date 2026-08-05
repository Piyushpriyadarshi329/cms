package com.contraflow.cms.tenant.services;

import com.contraflow.cms.tenant.dto.TenantRequest;
import com.contraflow.cms.tenant.dto.TenantResponse;
import com.contraflow.cms.tenant.dto.TenantThemeRequest;
import com.contraflow.cms.tenant.dto.ThemeConfig;
import com.contraflow.cms.tenant.entity.Tenant;
import com.contraflow.cms.tenant.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    public TenantResponse createTenant(TenantRequest request) {

        Tenant tenant = Tenant.builder()
                .name(request.getName())
                .legalName(request.getLegalName())
                .logoUrl(request.getLogoUrl())
                .mobile(request.getMobile())
                .email(request.getEmail())
                .address(request.getAddress())
                .state(request.getState())
                .city(request.getCity())
                .pinCode(request.getPinCode())
                .country(request.getCountry())
                .verified(false)
                .build();

        Tenant savedTenant = tenantRepository.save(tenant);

        return TenantResponse.builder()
                .id(savedTenant.getId())
                .name(savedTenant.getName())
                .legalName(savedTenant.getLegalName())
                .logoUrl(savedTenant.getLogoUrl())
                .mobile(savedTenant.getMobile())
                .email(savedTenant.getEmail())
                .address(savedTenant.getAddress())
                .state(savedTenant.getState())
                .city(savedTenant.getCity())
                .pinCode(savedTenant.getPinCode())
                .country(savedTenant.getCountry())
                .verified(savedTenant.getVerified())
                .build();
    }


    public Tenant getTenantById(Long id){
        return tenantRepository.findById(id).orElseThrow(()-> new RuntimeException("Tenant not found"));
    }

    @Cacheable(cacheNames = "tenant", key = "'all'")
    public List<Tenant> getAllTenants(){
        return tenantRepository.findAll();
    }

    public Tenant updateTenant(TenantRequest request, Long id){
        Tenant tenant1 = tenantRepository.findById(id).orElseThrow(()->new RuntimeException("Tenant Not found"));
        tenant1.setName(request.getName());
        tenant1.setLegalName(request.getLegalName());
        tenant1.setEmail(request.getEmail());
        tenant1.setLogoUrl(request.getLogoUrl());
        tenant1.setMobile(request.getMobile());
        tenant1.setAddress(request.getAddress());
        tenant1.setCity(request.getCity());
        tenant1.setState(request.getState());
        tenant1.setPinCode(request.getPinCode());
        tenant1.setCountry(request.getCountry());
        return tenantRepository.save(tenant1);
    }

    public void deleteTenant(Long id){
        Tenant tenant = tenantRepository.findById(id).orElseThrow(()->new RuntimeException("Tenant Not found"));
        tenantRepository.delete(tenant);
    }

    public ThemeConfig updateTheme(Long id, TenantThemeRequest request){
        Tenant tenant = tenantRepository.findById(id).orElseThrow(()->new RuntimeException("Tenant Not found"));

        ThemeConfig config = tenant.getThemeConfig();
        if (config == null) {
            config = new ThemeConfig();
        }

        if (request.getTheme() != null) config.setTheme(request.getTheme());
        if (request.getPrimaryColor() != null) config.setPrimaryColor(request.getPrimaryColor());
        if (request.getFont() != null) config.setFont(request.getFont());
        if (request.getAppLogo() != null) config.setAppLogo(request.getAppLogo());

        if (request.getLoginHeader() != null || request.getLoginMessage() != null) {
            ThemeConfig.Login login = config.getLogin() != null ? config.getLogin() : new ThemeConfig.Login();
            if (request.getLoginHeader() != null) login.setHeader(request.getLoginHeader());
            if (request.getLoginMessage() != null) login.setMessage(request.getLoginMessage());
            config.setLogin(login);
        }

        boolean sidebarChanged = request.getSidebarColor() != null || request.getSidebarTextColor() != null
                || request.getSidebarActiveTextColor() != null || request.getSidebarLogoUrl() != null
                || request.getSidebarMiniLogoUrl() != null || request.getSidebarAccordion() != null;

        if (sidebarChanged) {
            ThemeConfig.Sidebar sidebar = config.getSidebar() != null ? config.getSidebar() : new ThemeConfig.Sidebar();
            if (request.getSidebarColor() != null) sidebar.setColor(request.getSidebarColor());
            if (request.getSidebarTextColor() != null) sidebar.setTextColor(request.getSidebarTextColor());
            if (request.getSidebarActiveTextColor() != null) sidebar.setActiveTextColor(request.getSidebarActiveTextColor());
            if (request.getSidebarLogoUrl() != null) sidebar.setLogoUrl(request.getSidebarLogoUrl());
            if (request.getSidebarMiniLogoUrl() != null) sidebar.setMiniLogoUrl(request.getSidebarMiniLogoUrl());
            if (request.getSidebarAccordion() != null) sidebar.setAccordion(request.getSidebarAccordion());
            config.setSidebar(sidebar);
        }

        tenant.setThemeConfig(config);
        Tenant saved = tenantRepository.save(tenant);
        return saved.getThemeConfig();
    }
}
