package org.example.third_project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class CurrencyRequestDTO implements Serializable {
    private String code;
    private String name;
    private String sign;
}

