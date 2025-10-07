package pojo;
public record Address(
        String city,
        String street,
        int number,
        String zipcode,
        Geolocation geolocation
) {}

