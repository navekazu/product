package tools.mailer.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import tools.mailer.entity.Account;

import java.util.List;

public class AccountSerializer extends SerializerBase<Account> {

    @Override
    protected String persistenceName() {
        return "account";
    }

    @Override
    protected TypeReference getJsonTypeReference() {
        return new TypeReference<List<Account>>(){};
    }
}
