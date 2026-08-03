package com.contraflow.cms.proposal.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "proposals")
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String proposal_Number;

    @NotBlank
    private String title;


    private String description;



    @Column(name = "tenant_id")
    private int tenantId;


    @Column(name = "client_id")
    private int clientId;


    @Column(name ="client_contact_id" )
    private int clientContactId;


    @Column(name ="proposal_start_date" )
    private Date proposalStartDate;

}
