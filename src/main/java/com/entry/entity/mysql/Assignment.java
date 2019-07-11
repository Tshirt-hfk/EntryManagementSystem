package com.entry.entity.mysql;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="assignment")
public class Assignment {

    public final static Integer UNPUBLISHED = 1;
    public final static Integer PUBLISHED = 2;
    public final static Integer TOAUDITED = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(50)")
    private String entryName;

    @Column(columnDefinition = "text")
    private String content;

    @Column(nullable = false)
    private String field;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @OneToOne(mappedBy="pk.assignment", fetch=FetchType.LAZY)
    private Task task;

    @Column(columnDefinition = "TINYINT")
    private Integer state; //1:未发布；2：已发布，3：被领取

    public Assignment() {

    }

    public Assignment(String entryName, String content, String field, Subject subject) {
        this.entryName = entryName;
        this.content = content;
        this.field = field;
        this.subject = subject;
    }

    public Integer getId() {
        return id;
    }

    public String getEntryName() {
        return this.entryName;
    }

    public String getContent() {
        return content;
    }

    public String getField() {
        return field;
    }

    public Subject getSubject() {
        return subject;
    }

    public Task getTask() {
        return task;
    }

    public Integer getState() {
        return state;
    }

}
