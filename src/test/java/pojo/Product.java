package pojo;
public record Product(
        String title,
        double price,
        String description,
        String image,
        String category
) {}