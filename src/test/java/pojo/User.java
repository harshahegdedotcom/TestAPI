package pojo;

public record User(
                    String email,
                    String username,
                    String password,
                    Name name,
                    Address address,
                    String phone
)
{
}

