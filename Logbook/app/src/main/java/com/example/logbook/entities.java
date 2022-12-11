package com.example.logbook;

public class entities {

  int id;
  String img;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getImg() {
    return img;
  }

  public void setImg(String img) {
    this.img = img;
  }

  public entities(int id, String img) {
    this.img = img;
    this.id = id;
  }
}
