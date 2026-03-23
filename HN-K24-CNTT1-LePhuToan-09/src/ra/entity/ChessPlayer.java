package ra.entity;

import java.util.Scanner;

public class ChessPlayer {
    private String playerId;
    private String playerName;
    private int age;
    private int elo;

    public ChessPlayer() {
    }

    public ChessPlayer(String playerId, String playerName, int age, int elo) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.age = age;
        this.elo = elo;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    // Nhập dữ liệu
    public void inputData(Scanner scanner) {
        while (true) {
            System.out.print("Nhập mã cờ thủ: ");
            this.playerId = scanner.nextLine().trim();
            if (!this.playerId.isEmpty()) {
                break;
            }
            System.out.println("Mã cờ thủ không được để trống");
        }
        while (true) {
            System.out.print("Nhập tên cờ thủ: ");
            this.playerName = scanner.nextLine().trim();
            if (!this.playerName.isEmpty()) {
                break;
            }
            System.out.println("Tên cờ thủ không được để trống");
        }

        while (true) {
            try {
                System.out.print("Nhập tuổi (>= 18): ");
                this.age = Integer.parseInt(scanner.nextLine().trim());
                if (this.age >= 18) {
                    break;
                }
                System.out.println("Tuổi phải từ 18 trở lên");
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số nguyên");
            }
        }
        while (true) {
            try {
                System.out.print("Nhập Elo: ");
                this.elo = Integer.parseInt(scanner.nextLine().trim());
                if (this.elo >= 0) {
                    break;
                }
                System.out.println("Elo phải >= 0");
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số nguyên");
            }
        }
    }

    public void displayData() {
        System.out.println(this.playerId + " | " + this.playerName + " | " + this.age + " | " + this.elo);
    }
}
