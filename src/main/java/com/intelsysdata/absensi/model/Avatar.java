package com.intelsysdata.absensi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Avatar {
    @Column(name = "FIELD_FOTO_FILE_NAME")
    private String filename;

    @Column(name = "FIELD_FOTO_URL")
    private String url;
    @JsonIgnore
    @Column(name = "FIELD_FOTO_FILE_DATA")
    private byte[] fileData;
}
