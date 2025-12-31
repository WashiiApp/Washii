package br.com.washii.infra;

public class TestConnection {
    public static void main(String[] args) {
        try {
            DatabaseConfig.getConnection();
            System.out.println("Conectado com sucesso!");
        } catch (Exception e) {
            System.out.println("vixe");
            e.printStackTrace();
        }
    }
}
