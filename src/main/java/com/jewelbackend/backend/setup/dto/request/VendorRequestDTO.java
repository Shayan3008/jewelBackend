package com.jewelbackend.backend.setup.dto.request;

import lombok.Data;

@Data
public class VendorRequestDTO {
    Integer id;
    String vendorName;
    Integer vendorHeaderId;
    String vendorHeaderName;
}
