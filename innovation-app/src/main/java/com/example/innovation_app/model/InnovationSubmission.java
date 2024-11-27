package com.example.innovation_app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class InnovationSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime plannedCompletionTime;
    private Double estimatedCost;

    @Lob
    private byte[] attachment;

    private Long userId;  // ID głównego użytkownika zgłaszającego innowację

    @ElementCollection
    private List<Long> additionalUsersIds;

    private Long additionalRecipientId;

    private LocalDateTime submissionDate;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;

    public enum SubmissionStatus {
        PENDING, APPROVED, REJECTED, UNDER_REVIEW
    }

    // Gettery i settery
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getPlannedCompletionTime() {
        return plannedCompletionTime;
    }

    public void setPlannedCompletionTime(LocalDateTime plannedCompletionTime) {
        this.plannedCompletionTime = plannedCompletionTime;
    }

    public Double getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(Double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getAdditionalUsersIds() {
        return additionalUsersIds;
    }

    public void setAdditionalUsersIds(List<Long> additionalUsersIds) {
        this.additionalUsersIds = additionalUsersIds;
    }

    public Long getAdditionalRecipientId() {
        return additionalRecipientId;
    }

    public void setAdditionalRecipientId(Long additionalRecipientId) {
        this.additionalRecipientId = additionalRecipientId;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }
}
