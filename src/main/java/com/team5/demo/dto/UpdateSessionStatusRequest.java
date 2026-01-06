package com.team5.demo.dto;

import com.team5.demo.entities.SessionStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateSessionStatusRequest {
    @NotNull(message = "Status is required")
    private SessionStatus status;

    public UpdateSessionStatusRequest() {}

    public UpdateSessionStatusRequest(SessionStatus status) {
        this.status = status;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }
}
