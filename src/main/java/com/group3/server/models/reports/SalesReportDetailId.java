package com.group3.server.models.reports;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Embeddable
@EqualsAndHashCode
public class SalesReportDetailId implements Serializable {
    private Long savingTypeId;
    private Long reportId;
    
}
