package org.hao.test.dto;

import lombok.Data;

@Data(staticConstructor = "build")
public class TestDTO {

    int position;

    int length;

    public static void main(String[] args) {
        TestDTO.build();
    }
}
