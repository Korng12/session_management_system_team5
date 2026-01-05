package com.team5.demo.dto;

public class RoomDTO {
    private Long id;
    private String name;
    private Integer capacity;

    // Constructors
    public RoomDTO() {
    }

    public RoomDTO(Long id, String name, Long capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }
}
