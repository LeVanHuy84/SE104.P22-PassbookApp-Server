package com.group3.server.models.reports;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class SalesReportDetailId implements Serializable {
    private Long savingTypeId;
    private Long reportId;
}
