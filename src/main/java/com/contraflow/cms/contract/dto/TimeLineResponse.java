package com.contraflow.cms.contract.dto;

import com.contraflow.cms.contract.entity.ApprovalAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeLineResponse {

    private ApprovalAction action;

    // flat view of the acting user — no entity/proxy, so it serializes outside the session
    private Long actionById;
    private String actionByName;
    private String actionByEmail;

    private LocalDateTime actionAt;
    private String comment;
}
