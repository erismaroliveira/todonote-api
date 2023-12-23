package com.erismaroliveira.todonote.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCreateDTO {

  @Size(min = 2, max = 100)
  @NotBlank
  private String username;

  @Size(min = 8, max = 60)
  @NotBlank
  private String password;
}
