package nl.d2n.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
public class UserKey {
    
    @Column(name = "user_key")
    private String key;

    public UserKey() {}
    public UserKey(final String key) {
        this.key = key;
    }
    
    public void setKey(String key) { this.key = key; }
    public String getKey() { return this.key; }
    
    public void check() throws ApplicationException {
        Pattern pattern = Pattern.compile("[0-9a-f]+");
        Matcher matcher = pattern.matcher(getKey());
        if (!matcher.matches()) {
            throw new ApplicationException(D2NErrorCode.TAMPERED_KEY);
        }
    }

}
