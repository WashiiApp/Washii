package br.com.washii.infra.database;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DatabaseConfig {

    // Adicionamos .configure().ignoreIfMissing().load()
    // Isso evita que o app trave caso você decida usar variáveis de ambiente do sistema no futuro
    static Dotenv dotenv = Dotenv.configure()
            .directory("./") // Força a busca na pasta onde o JAR está sendo executado
            .ignoreIfMissing()
            .load();

    private static final String HOST = dotenv.get("DB_HOST");
    private static final String PORT = dotenv.get("DB_PORT");
    private static final String DB_NAME = dotenv.get("DB_NAME");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");
    private static final String SSLMODE = dotenv.get("DB_SSLMODE");
    private static final String CHANNEL_BINDING = dotenv.get("DB_CHANNEL_BINDING");

    private static final String URL =
            "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME +
                    "?sslmode=" + SSLMODE +
                    "&channelBinding=" + CHANNEL_BINDING;


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
