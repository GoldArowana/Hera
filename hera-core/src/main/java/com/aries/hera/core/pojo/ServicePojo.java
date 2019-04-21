package com.aries.hera.core.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServicePojo {
    private String name;
    private String host;
    private int port;
}
