package kr.co.springtricount.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public record MemberDTO(
        Long id,
        String identity,
        String name,
        @JsonIgnore String password
) {
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MemberDTO memberDTO = (MemberDTO) object;
        return Objects.equals(id, memberDTO.id) &&
                Objects.equals(identity, memberDTO.identity) &&
                Objects.equals(name, memberDTO.name) &&
                Objects.equals(password, memberDTO.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identity, name, password);
    }
}
