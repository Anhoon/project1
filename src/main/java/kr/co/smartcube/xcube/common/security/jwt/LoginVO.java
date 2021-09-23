package kr.co.smartcube.xcube.common.security.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("serial")
public class LoginVO implements UserDetails {
    private String obid;
    private String createdOn;
    private String email;
    private String password;
    private String name;
    private String userType;
    private String userStatus;
    private String mobile;
    private String idn;
    private String companyName;
    private String addr;
    private String tel;
    private String businessYype;
    private String sales;
    private String employeeSize;
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getObid() {
        return this.obid;
    }

    public void setObid(String obid) {
        this.obid = obid;
    }

    public String getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return this.userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserStatus() {
        return this.userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdn() {
        return this.idn;
    }

    public void setIdn(String idn) {
        this.idn = idn;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddr() {
        return this.addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getBusinessYype() {
        return this.businessYype;
    }

    public void setBusinessYype(String businessYype) {
        this.businessYype = businessYype;
    }

    public String getSales() {
        return this.sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getEmployeeSize() {
        return this.employeeSize;
    }

    public void setEmployeeSize(String employeeSize) {
        this.employeeSize = employeeSize;
    }
    
    public List<String> getRoles() {
        return this.roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
