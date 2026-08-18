package com.thai.shiftlogbook.exception;

import com.thai.shiftlogbook.domain.ReportStatus;

public class ReportNotEditableException extends RuntimeException {
    public ReportNotEditableException(ReportStatus status) {
        super("Report is not editable in status " + status);
    }
}