package com.group3.server.dtos.Filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFilter {
    private String fullname;
    private String citizenID;
    private boolean isActive;
}
