package com.entry.entity.mysql;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    public final static Integer ORDINRYUSER = 1;
    public final static Integer SUBJECTMAKER = 2;
    public final static Integer ADMINISTRATOR = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增主键
    private Integer id;

    @Column(unique = true, nullable = false, columnDefinition = "varchar(20)")
    private String name;

    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String password;

    @Column(nullable = false, columnDefinition = "TINYINT default 1")
    private Integer authorith;  // 1：普通用户; 2：专题制作人，有创建专题权限; 3：系统管理员

    @Column(columnDefinition = "varchar(20)")
    private String phone;

    @Email
    @Column(columnDefinition = "varchar(50)", unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy="pk.user",cascade=CascadeType.REFRESH,fetch=FetchType.LAZY)
    private List<GroupMember> groupMemberList;

    @OneToMany(mappedBy="user",cascade=CascadeType.REFRESH,fetch=FetchType.LAZY)
    private List<Task> taskList;

    public User() {

    }

    public User(String name, String password, Integer authorith, String phone, String email){
        this.name = name;
        this.password = password;
        this.authorith = authorith;
        this.phone = phone;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAuthorith() {
        return authorith;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public List<GroupMember> getGroupMemberList() {
        return groupMemberList;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

}
