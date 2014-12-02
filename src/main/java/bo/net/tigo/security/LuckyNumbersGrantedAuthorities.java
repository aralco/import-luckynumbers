package bo.net.tigo.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by aralco on 11/11/14.
 */
public class LuckyNumbersGrantedAuthorities implements GrantedAuthority {
    private String authority;

    public LuckyNumbersGrantedAuthorities(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
