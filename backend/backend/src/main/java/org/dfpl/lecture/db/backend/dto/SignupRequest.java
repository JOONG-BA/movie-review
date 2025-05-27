package org.dfpl.lecture.db.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    private String email;
    private String password;
    private String nickname;
}
