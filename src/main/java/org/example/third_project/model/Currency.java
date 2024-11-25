package org.example.third_project.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public final class Currency implements Serializable {
    private int id;
    private String code;
    private String name;
    private String sign;

    public Currency(int id, String code, String name, String sign) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.sign = sign;
    }

    public Currency() {
    }
}
