package     com.example.test1.domain.usecase

class LoginUseCase {
    operator fun invoke(username: String, password: String): Boolean {
        // Implement your login logic here
        // For example, you can check if the username and password are correct
        return username == "admin" && password == "password"
    }
}