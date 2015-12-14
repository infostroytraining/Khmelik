package entity;

public enum Type {

    TRACE("blue"),
    DEBUG("green"),
    INFO("black"),
    WARN("blue"),
    ERROR("red"),
    FATAL("white");

    private String color;

    Type(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}