package com.pendezzapizza.pendezzapizza_api.core.security.authorizationserver;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class UUIDMixin {}
