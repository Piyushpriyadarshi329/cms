package com.contraflow.cms.ai.services;

import com.contraflow.cms.ai.dto.ChatRequest;
import com.contraflow.cms.ai.dto.ContractPdfResponse;
import com.contraflow.cms.aws.s3.services.S3Service;
import com.contraflow.cms.contract.dto.ContractResponse;
import com.contraflow.cms.contract.service.ContractService;
import com.contraflow.cms.proposal.dto.ProposalDiscussionResponse;
import com.contraflow.cms.proposal.dto.ProposalResponse;
import com.contraflow.cms.proposal.dto.ProposalVersionResponse;
import com.contraflow.cms.tenant.dto.TenantResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContractPdfService {

    private final ContractService contractService;
    private final ChatServices chatServices;
    private final PdfService pdfService;
    private final S3Service s3Service;

    /**
     * Fetch the contract (with its proposal, discussions and latest version), build a prompt
     * from that data, generate contract HTML via the LLM, render it to PDF, store it in S3,
     * and return the HTML plus the object key and a presigned download URL.
     */
    public ContractPdfResponse generate(Long tenantId, UUID contractId) {

        ContractResponse detail = contractService.getContractDetail(tenantId, contractId);

        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setMessage(buildPrompt(detail));

        String html = chatServices.OpenAIChat(chatRequest);

        byte[] pdf = pdfService.htmlToPdf(html);

        String objectKey = s3Service.uploadBytes(pdf, "contracts", "contract.pdf", "application/pdf");
        String pdfUrl = s3Service.getDownloadUrl(objectKey);

        return ContractPdfResponse.builder()
                .html(html)
                .objectKey(objectKey)
                .pdfUrl(pdfUrl)
                .build();
    }

    private String buildPrompt(ContractResponse d) {
        StringBuilder sb = new StringBuilder();
        sb.append("Generate a complete, professional contract document using the following data.\n\n");

        sb.append("CONTRACT\n");
        sb.append("- Title: ").append(nz(d.getContractTitle())).append("\n");
        sb.append("- Type: ").append(nz(d.getContractType())).append("\n");
        sb.append("- Status: ").append(nz(d.getStatus())).append("\n\n");

        // Prefer the actual latest proposal version; fall back to the contract's snapshot fields.
        List<ProposalVersionResponse> versions = d.getProposalVersions();
        ProposalVersionResponse latest = (versions != null && !versions.isEmpty())
                ? versions.get(versions.size() - 1) : null;

        sb.append("FINANCIAL TERMS (latest proposal version)\n");
        if (latest != null) {
            sb.append("- Version: ").append(nz(latest.getProposalVersionNumber())).append("\n");
            sb.append("- Amount: ").append(nz(latest.getCurrency())).append(" ").append(nz(latest.getProposalAmount())).append("\n");
            sb.append("- Billing: ").append(nz(latest.getBilling())).append("\n");
            sb.append("- Start Date: ").append(nz(latest.getStartDate())).append("\n");
            sb.append("- End Date: ").append(nz(latest.getEndDate())).append("\n\n");
        } else {
            sb.append("- Version: ").append(nz(d.getProposalVersionNumber())).append("\n");
            sb.append("- Amount: ").append(nz(d.getCurrency())).append(" ").append(nz(d.getProposalAmount())).append("\n");
            sb.append("- Billing: ").append(nz(d.getBilling())).append("\n");
            sb.append("- Start Date: ").append(nz(d.getStartDate())).append("\n");
            sb.append("- End Date: ").append(nz(d.getEndDate())).append("\n\n");
        }

        TenantResponse t = d.getTenant();
        if (t != null) {
            sb.append("SERVICE PROVIDER (Company)\n");
            sb.append("- Name: ").append(nz(t.getName())).append("\n");
            sb.append("- Legal Name: ").append(nz(t.getLegalName())).append("\n");
            sb.append("- Address: ").append(nz(t.getAddress())).append(", ").append(nz(t.getCity()))
                    .append(", ").append(nz(t.getState())).append(" ").append(nz(t.getPinCode()))
                    .append(", ").append(nz(t.getCountry())).append("\n");
            sb.append("- Email: ").append(nz(t.getEmail())).append(", Phone: ").append(nz(t.getMobile())).append("\n\n");
        }

        ProposalResponse p = d.getProposal();
        if (p != null) {
            sb.append("CLIENT\n");
            sb.append("- Name: ").append(nz(p.getClientName())).append("\n\n");

            sb.append("PROPOSAL\n");
            sb.append("- Number: ").append(nz(p.getProposalNumber())).append("\n");
            sb.append("- Title: ").append(nz(p.getTitle())).append("\n");
            sb.append("- Description: ").append(nz(p.getDescription())).append("\n\n");

            List<ProposalDiscussionResponse> discussions = p.getProposalDiscussion();
            if (discussions != null && !discussions.isEmpty()) {
                sb.append("SCOPE / DISCUSSION / REQUIREMENTS\n");
                for (ProposalDiscussionResponse disc : discussions) {
                    sb.append("- ").append(nz(disc.getTitle()));
                    if (disc.getRequirement() != null) sb.append(" | Requirement: ").append(disc.getRequirement());
                    if (disc.getDescription() != null) sb.append(" | ").append(disc.getDescription());
                    if (disc.getRemarks() != null) sb.append(" | Remarks: ").append(disc.getRemarks());
                    sb.append("\n");
                }
                sb.append("\n");
            }
        }

        sb.append("Produce a clean, formal contract incorporating the above: parties, financial terms, ")
                .append("duration, scope of work derived from the proposal and discussion, standard clauses, ")
                .append("and signature blocks for both parties.");
        return sb.toString();
    }

    private String nz(Object value) {
        return value == null ? "" : String.valueOf(value);
    }
}
