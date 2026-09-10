package com.project.lovable.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectFile {

    Long id;

    Project project;

    String path;
    String miniObjectKey;

    User createdBy;
    User updatedBy;

    Instant createdAt;
    Instant updatedAt;

}
