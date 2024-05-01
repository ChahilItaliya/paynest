package com.example.payment;

public class Suid {
    String s;
    String name;
    String email;
    String imgurl;

    private static final Suid ourInstance = new Suid();

    public static Suid getInstance() {
        return ourInstance;
    }
    private Suid() {
    }
    public void setData(String s) {
        this.s = s;
    }
    public String getData() {
        return s;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getImgurl() {
        return imgurl;
    }
}
