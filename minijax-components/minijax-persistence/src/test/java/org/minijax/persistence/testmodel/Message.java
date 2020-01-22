package org.minijax.persistence.testmodel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Message {

    @Id
    private int id;

    @ManyToOne
    private User author;

    private String text;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(final User author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }
}
