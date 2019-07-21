package com.radchenko.restapi.ui.response;

public class LazyPlayerDto {
    private String fullName;
    private String position;

    public LazyPlayerDto() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
