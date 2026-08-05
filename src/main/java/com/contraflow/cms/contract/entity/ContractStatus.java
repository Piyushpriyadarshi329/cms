package com.contraflow.cms.contract.entity;

public enum ContractStatus {

    // Contract created
    DRAFT,

    // Internal Approval Flow
    MANAGER_APPROVAL_PENDING,

    FINANCE_APPROVAL_PENDING,

    LEGAL_APPROVAL_PENDING,

    // All approvals completed
    APPROVED,

    // E-Sign Process
    ESIGN_PENDING,

    PARTIALLY_SIGNED,

    // Contract is live
    ACTIVE,

    // Contract completed
    CLOSED,

    // Optional states
    REJECTED,

    CANCELLED
}