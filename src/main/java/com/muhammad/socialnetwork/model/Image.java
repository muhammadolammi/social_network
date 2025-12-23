package com.muhammad.socialnetwork.model;

public class Image {
   private String title;
   private long id;
   private byte[] content;
  
   public Image(String title, long id, byte[] content) {
    this.title = title;
    this.id = id;
    this.content = content;
   }
   public String getTitle() {
    return title;
   }
   public void setTitle(String title) {
    this.title = title;
   }
   public long getId() {
    return id;
   }
   public void setId(long id) {
    this.id = id;
   }
   public byte[] getContent() {
    return content;
   }
   public void setContent(byte[] content) {
    this.content = content;
   }
}
