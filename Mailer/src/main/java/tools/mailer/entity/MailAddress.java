package tools.mailer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailAddress {
    private String userName;
    private String localName;
    private String domainName;
    private String password;

    public String toMailAddress() {
        return (localName==null? "": localName)+"@"+(domainName==null? "": domainName);
    }
    public String toFullMailAddress() {
        return (userName==null? toMailAddress(): (userName.indexOf(",")==-1? userName: "\""+userName+"\"")+" <"+toMailAddress()+">");
    }
}
