package com.araguacaima.open_archi.web.wrapper;

import com.araguacaima.open_archi.persistence.meta.Account;
import com.araguacaima.open_archi.persistence.meta.Avatar;
import org.pac4j.core.profile.UserProfile;

public class AccountWrapper {
    public static Account toAccount(UserProfile profile) {
        Account account = null;
        if (profile != null) {
            account = new Account();
            account.setName((String) profile.getAttribute("given_name"));
            account.setLastname((String) profile.getAttribute("family_name"));
            account.setLogin((String) profile.getAttribute("name"));
            account.setEmail((String) profile.getAttribute("email"));
            String url = profile.getAttribute("picture").toString();
            Avatar avatar = new Avatar();
            avatar.setUrl(url);
            account.setAvatar(avatar);
        }
        return account;
    }
}
