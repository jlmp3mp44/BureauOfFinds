package com.bureau_of_finds.bureau.models;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Find {
    public Find(){}


    public Find(String title, String key_words, String description, String telephone) {
        this.title = title;
        this.key_words = key_words;
        this.description = description;
        this.telephone = telephone;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title, key_words, description, telephone;
    private LocalDateTime dateOfCreated;
    private String imagePath;

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Image getImage() {
        return image;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "find")
    private Image image;

    public void setPreviewImageId(Long previewImageId) {
        this.previewImageId = previewImageId;
    }

    private Long previewImageId;

    @Column(name = "dateOfCreated")
    @PrePersist
    private void init() {
        dateOfCreated =  LocalDateTime.now();
    }
    public String getDateOfCreated() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = dateOfCreated.format(formatter);
        return formattedDate;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey_words() {
        return key_words;
    }

    public void setKey_words(String key_words) {
        this.key_words = key_words;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTelephone() {
        return telephone;
    }
    public void setUser(User user){this.user=user;}

    public User getUser() {
        return user;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public void addImageToProduct(Image image) {
        image.setFind(this);
        this.image=image; // Set the image on the Find entity
    }



}
