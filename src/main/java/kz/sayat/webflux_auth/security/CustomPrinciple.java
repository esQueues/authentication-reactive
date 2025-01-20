package kz.sayat.webflux_auth.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomPrinciple implements Principal {

    private Long id;
    private String name;

}
