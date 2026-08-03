package com.contraflow.cms.proposal.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "proposal_discussions")
public class ProposalDiscussion {

@Id
@GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


 @Column(name = "proposal_id")
  private  UUID proposalId;


    @Column(name = "tenant_user_id")
    private int tenantUserId;

    @Column(name ="client_contact_id" )
    private int clientContactId;


    @Column(name = "meeting_date")
    private Date meetingDate;


    private String title;
    private String description;
    private String Remarks;
    private String requirement;

}
