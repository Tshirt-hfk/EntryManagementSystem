package com.entry.entity.mysql;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="assignment")
public class Assignment {

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

    public Assignment() {

    }

    public Assignment(String entryName, String content, String field, Subject subject) {
        this.entryName = entryName;
        this.content = content;
        this.field = field;
        this.subject = subject;
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

}
