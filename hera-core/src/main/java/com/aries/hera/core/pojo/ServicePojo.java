package com.aries.hera.core.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServicePojo {
    private String name;
    private String ip;
    private int port;
}
