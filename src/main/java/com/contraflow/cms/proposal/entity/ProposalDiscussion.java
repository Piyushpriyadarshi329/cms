package com.contraflow.cms.proposal.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

import com.contraflow.cms.tenant.entity.Tenant;

@Entity
@Data
@Table(name = "proposal_discussions")
public class ProposalDiscussion {

@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;


@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "proposal_id", nullable = false)
private Proposal proposal;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "tenant_user_id")
private Tenant tenantUser;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "client_contact_id")
// private ClientContact clientContact;

@Column(name = "meeting_date")
private Date meetingDate;

@NotBlank
private String title;

private String description;

private String remarks;

private String requirement;

}
