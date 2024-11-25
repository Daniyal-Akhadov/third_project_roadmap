package org.example.third_project.dto;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public final class CurrencyResponseDTO implements Serializable {

    private int id;
    private String code;
    private String name;
    private String sign;
}
