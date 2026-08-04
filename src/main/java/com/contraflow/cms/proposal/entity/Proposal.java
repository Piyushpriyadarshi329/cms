package com.contraflow.cms.proposal.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.Date;
import java.util.UUID;

import com.contraflow.cms.client.entity.Client;
import com.contraflow.cms.client.entity.ClientUser;
import com.contraflow.cms.tenant.entity.Tenant;

@Entity
@Data
@Table(name = "proposals")
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "proposal_number",unique = true)
    private String proposalNumber;

    @NotBlank
    @Column(name = "title",nullable = false)
    private String title;

    
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id",nullable = false)
    private Client client;
    

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProposalStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="client_user_id" )
    private ClientUser clientUser;


    @Column(name ="proposal_start_date" )
    private Date proposalStartDate;

    public enum ProposalStatus { DRAFT, SENT, ACCEPTED, REJECTED};

}


