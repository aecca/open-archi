package com.araguacaima.open_archi.persistence.meta;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;


@PersistenceUnit(unitName = "open-archi")
@Entity
@Table(schema = "META", name = "Account")
@DynamicUpdate
@NamedQueries(value = {@NamedQuery(name = Account.FIND_BY_EMAIL,
        query = "select a from Account a where a.email = :"
                + Account.PARAM_EMAIL), @NamedQuery(name = Account.FIND_BY_LOGIN,
        query = "select a from Account a where a.login = :"
                + Account.PARAM_LOGIN), @NamedQuery(
        name = Account.FIND_BY_EMAIL_OR_LOGIN,
        query = "select a from Account a where a.email = :"
                + Account.PARAM_EMAIL
                + " or a.login = :"
                + Account.PARAM_LOGIN), @NamedQuery(name = Account.FIND_BY_EMAIL_OR_LOGIN_NOT_FIRST_TIME,
        query = "select a from Account a where (a.email = :"
                + Account.PARAM_EMAIL
                + " or a.login = :"
                + Account.PARAM_LOGIN
                + ") and a.firstAccess=false"), @NamedQuery(name = Account.GET_ACCOUNTS_EMAIL_OR_LOGIN_STARTS_WITH,
        query = "select a from Account a where a.email like :"
                + Account.PARAM_EMAIL
                + " or a.login like :"
                + Account.PARAM_LOGIN), @NamedQuery(
        name = Account.GET_ACCOUNT_COUNT,
        query = "select count(a) from Account a"), @NamedQuery(name = Account.GET_ALL_ACCOUNTS,
        query = "select a from Account a order by a.login, a.email")})
public class Account implements Serializable, SimpleOverridable<Account> {

    public static final String FIND_BY_EMAIL = "Account.findByEmail";
    public static final String FIND_BY_LOGIN = "Account.findByLogin";
    public static final String FIND_BY_EMAIL_OR_LOGIN = "Account.findByEmailOrLogin";
    public static final String GET_ACCOUNT_COUNT = "Account.getAccountCount";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_LOGIN = "login";
    public static final String GET_ALL_ACCOUNTS = "Account.getAllAccounts";
    public static final String GET_ACCOUNTS_EMAIL_OR_LOGIN_STARTS_WITH =
            "Account.getAccountsWhoseLoginOrEmailStartsWith";
    public static final String FIND_BY_EMAIL_OR_LOGIN_NOT_FIRST_TIME = "Account.findByEmailOrLoginNotFirstTime";
    private static final long serialVersionUID = 1199840813066879040L;

    @Id
    protected String id;

    @Column(unique = true, nullable = false)
    @NotNull
    @Size(min = 1)
    private String email;

    @Column(unique = true, nullable = false)
    @NotNull
    @Size(min = 1)
    private String login;

    @Column
    private String password;

    @Column(nullable = false)
    @NotNull
    private boolean firstAccess = true;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            schema = "META",
            name = "Account_Roles",
            joinColumns = {@JoinColumn(name = "Account_Id", referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Role_Id", referencedColumnName = "Id")})
    private Set<Role> roles = new HashSet<>();
    @Column
    private String name;
    @Column
    private String lastname;
    @OneToOne
    private Avatar avatar;
    private boolean superuser;

    public Account() {
        this.id = UUID.randomUUID().toString();
    }

    public Account(String id, String login, String email, String password, Set<Role> roles) {
        this();
        this.login = login;
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public Account(String login, String email, String password, Set<Role> roles) {
        this();
        this.login = login;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isFirstAccess() {
        return firstAccess;
    }

    public void setFirstAccess(boolean firstAccess) {
        this.firstAccess = firstAccess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return new EqualsBuilder()
                .append(firstAccess, account.firstAccess)
                .append(id, account.id)
                .append(email, account.email)
                .append(login, account.login)
                .append(password, account.password)
                .append(roles, account.roles)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(email)
                .append(login)
                .append(password)
                .append(firstAccess)
                .append(roles)
                .toHashCode();
    }

    @Override
    public void override(Account source, boolean keepMeta, String suffix) {
        this.id = source.getId();
        this.email = source.getEmail();
        this.login = source.getLogin();
        this.password = source.getPassword();
        this.firstAccess = source.isFirstAccess();
        if (source.getRoles() != null) {
            if (this.roles == null) {
                this.roles = new LinkedHashSet<>();
            }
            source.getRoles().forEach(role -> {
                Role role_ = new Role();
                role_.override(role, keepMeta, suffix);
                this.roles.add(role_);
            });
        } else {
            this.roles = null;
        }

    }

    @Override
    public void copyNonEmpty(Account source, boolean keepMeta) {
        if (StringUtils.isNotBlank(source.getId())) {
            this.id = source.getId();
        }
        if (StringUtils.isNotBlank(source.getEmail())) {
            this.email = source.getEmail();
        }
        if (StringUtils.isNotBlank(source.getLogin())) {
            this.login = source.getLogin();
        }
        if (StringUtils.isNotBlank(source.getPassword())) {
            this.password = source.getPassword();
        }
        this.firstAccess = source.isFirstAccess();
        if (source.getRoles() != null) {
            if (this.roles == null) {
                this.roles = new LinkedHashSet<>();
            }
            source.getRoles().forEach(role -> {
                Role role_ = new Role();
                role_.copyNonEmpty(role, keepMeta);
                this.roles.add(role_);
            });
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public boolean isSuperuser() {
        return superuser;
    }

    public void setSuperuser(boolean superuser) {
        this.superuser = superuser;
    }
}
