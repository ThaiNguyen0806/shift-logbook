package com.shiftlog.domain;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum ReportStatus {
    DRAFT,
    PUBLISHED,
    ACKNOWLEDGED;

    private static final Map<ReportStatus, Set<ReportStatus>> ALLOWED_TRANSITIONS =
            new EnumMap<>(ReportStatus.class);

    static {
        ALLOWED_TRANSITIONS.put(DRAFT, EnumSet.of(PUBLISHED));
        ALLOWED_TRANSITIONS.put(PUBLISHED, EnumSet.of(ACKNOWLEDGED));
        ALLOWED_TRANSITIONS.put(ACKNOWLEDGED, EnumSet.noneOf(ReportStatus.class));
    }

    public boolean canTransitionTo(ReportStatus target) {
        return ALLOWED_TRANSITIONS.get(this).contains(target);
    }

    public boolean isEditable() {
        return this == DRAFT;
    }
}