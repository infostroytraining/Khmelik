package dto;

import entity.User;

public class UserDTO {

    private String email;
    private String name;
    private String surname;
    private String image;

    public UserDTO(String email, String name, String surname, String image) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getUser(){
        User result = new User();
        result.setName(name);
        result.setSurname(surname);
        result.setEmail(email);
        result.setImage(image);
        return result;
    }

}