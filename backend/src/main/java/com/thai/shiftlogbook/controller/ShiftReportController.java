package com.thai.shiftlogbook.controller;

import com.thai.shiftlogbook.domain.ShiftReport;
import com.thai.shiftlogbook.domain.User;
import com.thai.shiftlogbook.dto.AuditLogEntryResponse;
import com.thai.shiftlogbook.dto.CreateReportRequest;
import com.thai.shiftlogbook.dto.PublishReportRequest;
import com.thai.shiftlogbook.dto.ShiftReportResponse;
import com.thai.shiftlogbook.repository.AuditLogRepository;
import com.thai.shiftlogbook.repository.UserRepository;
import com.thai.shiftlogbook.service.ShiftReportService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reports")
public class ShiftReportController {

    private final ShiftReportService reportService;
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public ShiftReportController(ShiftReportService reportService,
                                 AuditLogRepository auditLogRepository,
                                 UserRepository userRepository) {
        this.reportService = reportService;
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ShiftReportResponse create(@AuthenticationPrincipal User currentUser,
                                      @Valid @RequestBody CreateReportRequest request) {
        User handoffTo = userRepository.findById(request.getHandoffToUserId())
                .orElseThrow(() -> new IllegalStateException("Selected recipient does not exist"));

        ShiftReport report = reportService.createDraft(currentUser,
                request.getActiveIncidents(), request.getOngoingInvestigations(),
                request.getWatchlistItems(), request.getSeverity(), request.getTags(), handoffTo);
        return new ShiftReportResponse(report);
    }

    @PutMapping("/{id}")
    public ShiftReportResponse update(@PathVariable UUID id,
                                      @Valid @RequestBody CreateReportRequest request) {
        ShiftReport report = reportService.updateDraft(id,
                request.getActiveIncidents(), request.getOngoingInvestigations(),
                request.getWatchlistItems(), request.getSeverity(), request.getTags());
        return new ShiftReportResponse(report);
    }

    @PostMapping("/{id}/publish")
    public ShiftReportResponse publish(@PathVariable UUID id,
                                       @AuthenticationPrincipal User currentUser) {
        ShiftReport report = reportService.publish(id, currentUser);
        return new ShiftReportResponse(report);
    }

    @PostMapping("/{id}/acknowledge")
    public ShiftReportResponse acknowledge(@PathVariable UUID id,
                                           @AuthenticationPrincipal User currentUser) {
        ShiftReport report = reportService.acknowledge(id, currentUser);
        return new ShiftReportResponse(report);
    }

    @GetMapping("/{id}")
    public ShiftReportResponse getOne(@PathVariable UUID id) {
        ShiftReport report = reportService.getOrThrowPublic(id);

        List<AuditLogEntryResponse> history = auditLogRepository
                .findByReportIdOrderByOccurredAtAsc(id)
                .stream()
                .map(AuditLogEntryResponse::new)
                .toList();

        return new ShiftReportResponse(report, history);
    }

    @GetMapping
    public List<ShiftReportResponse> getAll(
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) Instant since) {

        return reportService.search(severity, tag, since)
                .stream()
                .map(ShiftReportResponse::new)
                .toList();
    }

    @GetMapping("/my-drafts")
    public List<ShiftReportResponse> getMyDrafts(@AuthenticationPrincipal User currentUser) {
        return reportService.getMyDrafts(currentUser)
                .stream()
                .map(ShiftReportResponse::new)
                .toList();
    }

    @GetMapping("/pending-for-me")
    public List<ShiftReportResponse> getPendingForMe(@AuthenticationPrincipal User currentUser) {
        return reportService.getPendingForMe(currentUser)
                .stream()
                .map(ShiftReportResponse::new)
                .toList();
    }
}