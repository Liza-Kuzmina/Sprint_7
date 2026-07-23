package ru.scooter.api.model;

import java.util.List;
import java.util.Objects;

public class Order {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final List<String> color;

    public Order(String firstName, String lastName, String address, String metroStation,
                 String phone, int rentTime, String deliveryDate, String comment, List<String> color) {
        this.firstName = Objects.requireNonNull(firstName, "firstName cannot be null");
        this.lastName = Objects.requireNonNull(lastName, "lastName cannot be null");
        this.address = Objects.requireNonNull(address, "address cannot be null");
        this.metroStation = Objects.requireNonNull(metroStation, "metroStation cannot be null");
        this.phone = Objects.requireNonNull(phone, "phone cannot be null");
        this.rentTime = rentTime;
        this.deliveryDate = Objects.requireNonNull(deliveryDate, "deliveryDate cannot be null");
        this.comment = comment != null ? comment : "";
        this.color = Objects.requireNonNull(color, "color cannot be null");
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getMetroStation() {
        return metroStation;
    }

    public String getPhone() {
        return phone;
    }

    public int getRentTime() {
        return rentTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getComment() {
        return comment;
    }

    public List<String> getColor() {
        return color;
    }


    @Override
    public String toString() {
        return "Order{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", metroStation='" + metroStation + '\'' +
                ", phone='" + phone + '\'' +
                ", rentTime=" + rentTime +
                ", deliveryDate='" + deliveryDate + '\'' +
                ", comment='" + comment + '\'' +
                ", color=" + color +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return rentTime == order.rentTime &&
                Objects.equals(firstName, order.firstName) &&
                Objects.equals(lastName, order.lastName) &&
                Objects.equals(address, order.address) &&
                Objects.equals(metroStation, order.metroStation) &&
                Objects.equals(phone, order.phone) &&
                Objects.equals(deliveryDate, order.deliveryDate) &&
                Objects.equals(comment, order.comment) &&
                Objects.equals(color, order.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                firstName, lastName, address, metroStation, phone,
                rentTime, deliveryDate, comment, color
        );
    }
}
