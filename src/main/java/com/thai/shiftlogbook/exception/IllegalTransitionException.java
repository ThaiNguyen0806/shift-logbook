package com.thai.shiftlogbook.exception;

import com.thai.shiftlogbook.domain.ReportStatus;

public class IllegalTransitionException extends RuntimeException {
    public IllegalTransitionException(ReportStatus from, ReportStatus to) {
        super("Cannot transition report from " + from + " to " + to);
    }
}