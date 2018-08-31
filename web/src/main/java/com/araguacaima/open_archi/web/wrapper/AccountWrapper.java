package com.araguacaima.open_archi.web.wrapper;

import com.araguacaima.open_archi.persistence.meta.Account;
import com.araguacaima.open_archi.persistence.meta.Avatar;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.oauth.profile.google2.Google2Profile;

import java.net.URL;

public class AccountWrapper {
    public static Account toAccount(UserProfile profile) {
        Account account = null;
        if (profile != null) {
            account = new Account();
            account.setName(((Google2Profile) profile).getFamilyName());
            account.setLastname(((Google2Profile) profile).getFamilyName());
            account.setLogin(((Google2Profile) profile).getDisplayName());
            account.setEmail(((Google2Profile) profile).getEmail());
            String url = profile.getAttribute("image.url").toString();
            Avatar avatar = new Avatar();
            avatar.setUrl(url);
            account.setAvatar(avatar);
        }
        return account;
    }
}
