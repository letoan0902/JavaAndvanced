package ra.business;

import ra.entity.ChessPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


// Lớp nghiệp vụ
public class PlayerBusiness {

    private static PlayerBusiness instance;

    private final List<ChessPlayer> players;
    private PlayerBusiness() {
        players = new ArrayList<>();
    }

    public static PlayerBusiness getInstance() {
        if (instance == null) {
            instance = new PlayerBusiness();
        }
        return instance;
    }

    // Hiển thị danh sách
    public void displayAll() {
        if (players.isEmpty()) {
            System.out.println("Danh sách cờ thủ hiện đang trống!");
            return;
        }
        players.forEach(ChessPlayer::displayData);
    }

    // Thêm cờ thủ
    public boolean addPlayer(ChessPlayer player) {
        boolean isDuplicate = players.stream()
                .anyMatch(p -> p.getPlayerId().equals(player.getPlayerId()));

        if (isDuplicate) {
            System.out.println("Mã cờ thủ đã tồn tại");
            return false;  
        }

        players.add(player);
        return true;  
    }

    // Tìm kiếm theo ID
    public Optional<ChessPlayer> findById(String id) {
        return players.stream()
                .filter(p -> p.getPlayerId().equals(id))
                .findFirst();
    }

    // Xóa cờ thủ
    public void deletePlayer(String id) {
        int sizeBefore = players.size();

        players.removeIf(p -> p.getPlayerId().equals(id));

        if (players.size() == sizeBefore) {
            System.out.println("Mã cờ thủ không tồn tại");
        } else {
            System.out.println("Xóa cờ thủ thành công");
        }
    }

    // Tìm kiếm theo tên
    public List<ChessPlayer> searchByName(String name) {
        return players.stream()
                .filter(p -> p.getPlayerName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Sắp xếp giảm dần theo Elo
    public List<ChessPlayer> sortByEloDescending() {
        return players.stream()
                .sorted(Comparator.comparingInt(ChessPlayer::getElo).reversed())
                .collect(Collectors.toList());
    }

    // Lọc cờ thủ xuất sắc
    public List<ChessPlayer> filterGoodPlayers() {
        return players.stream()
                .filter(p -> p.getElo() >= 1500)
                .collect(Collectors.toList());
    }
}
