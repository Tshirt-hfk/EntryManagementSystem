package com.entry.entity.mysql;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增主键
    private Integer id;

    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String name;

    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String password;

    @Column(nullable = false, columnDefinition = "TINYINT default 0")
    private Integer authorith;  // 0：普通用户; 1：专题制作人，有创建专题权限; 2：系统管理员

    @Column(columnDefinition = "varchar(20)")
    private String phone;

    @Email
    @Column(columnDefinition = "varchar(20)")
    private String email;

    public List<SubjectGroup> getSubjectGroupList() {
        return subjectGroupList;
    }

    public void setSubjectGroupList(List<SubjectGroup> subjectGroupList) {
        this.subjectGroupList = subjectGroupList;
    }

    @OneToMany(mappedBy="user",cascade=CascadeType.ALL)
    private List<SubjectGroup> subjectGroupList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAuthorith() {
        return authorith;
    }

    public void setAuthorith(Integer authorith) {
        this.authorith = authorith;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email){this.email = email; }

}
