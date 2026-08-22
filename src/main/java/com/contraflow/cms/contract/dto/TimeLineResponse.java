package com.contraflow.cms.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * Timeline node in the shape the shared frontend timeline component expects:
 * { id, email (stage label), type, status, name, time, actionTime, owner{...}, data }.
 * Times are epoch millis.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeLineResponse {

    private String id;
    private String email;    // stage label shown as the node heading (e.g. "App Started", "Approver")
    private String type;     // "start" / workflow type / "Completed"
    private String status;   // COMPLETED / PENDING
    private String name;     // "Submitted by X" for the start node, else the actor's name
    private Long time;       // epoch millis — when the node was created
    private Long actionTime; // epoch millis — when the action was taken
    private Owner owner;

    @Builder.Default
    private List<Object> data = List.of();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Owner {
        private Long id;
        private String ownerType;   // ADMIN / EMPLOYEE
        private String ownerId;
        private String ownerName;
    }
}
