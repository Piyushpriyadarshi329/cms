package com.contraflow.cms.proposal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.UUID;



@Data
@AllArgsConstructor
public class ProposalRequest {

    private String title;
    private String description;
    private Date proposalStartDate;





}
