package com.muhammad.socialnetwork.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name =  "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    // Track which providers are linked
    private boolean linkedWithGithub = false;
   
    private boolean linkedWithGoogle = false;

    public User(){}
    public User(UUID id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email=email;
    }
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
     public boolean isLinkedWithGithub() {
        return linkedWithGithub;
    }
    public void setLinkedWithGithub(boolean linkedWithGithub) {
        this.linkedWithGithub = linkedWithGithub;
    }

        public boolean isLinkedWithGoogle() {
        return linkedWithGoogle;
    }
    public void setLinkedWithGoogle(boolean linkedWithGoogle) {
        this.linkedWithGoogle = linkedWithGoogle;
    }
    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password
                + ", linkedWithGithub=" + linkedWithGithub + ", linkedWithGoogle=" + linkedWithGoogle + "]";
    }
       


}
