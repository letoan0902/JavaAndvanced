package ra.presentation;

import ra.business.PlayerBusiness;
import ra.entity.ChessPlayer;

import java.util.List;
import java.util.Scanner;

public class PlayerManagement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PlayerBusiness pb = PlayerBusiness.getInstance();

        while (true) {
            System.out.println("********************* QUẢN LÝ CỜ THỦ *********************");
            System.out.println("1. Hiển thị danh sách toàn bộ cờ thủ");
            System.out.println("2. Thêm mới cờ thủ");
            System.out.println("3. Cập nhật thông tin sinh viên theo mã cờ thủ");
            System.out.println("4. Xóa sinh viên theo mã cờ thủ");
            System.out.println("5. Tìm kiếm cờ thủ theo tên");
            System.out.println("6. Lọc danh sách cờ thủ xuất sắc (Elo >= 1500)");
            System.out.println("7. Sắp xếp danh sách sinh viên giảm dần theo Elo");
            System.out.println("8. Thoát");
            System.out.print("Lựa chọn của bạn: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số từ 1-8");
                continue;
            }

            switch (choice) {

                case 1:
                    pb.displayAll();
                    break;
                case 2:
                    ChessPlayer newPlayer = new ChessPlayer();
                    newPlayer.inputData(sc);
                    // check trùng
                    while (pb.findById(newPlayer.getPlayerId()).isPresent()) {
                        System.out.println("Mã cờ thủ đã tồn tại! Vui lòng nhập mã khác.");
                        System.out.print("Nhập mã cờ thủ mới: ");
                        newPlayer.setPlayerId(sc.nextLine().trim());
                    }
                    pb.addPlayer(newPlayer);
                    System.out.println("Thêm cờ thủ thành công!");
                    break;

                case 3:
                    System.out.print("Nhập mã cờ thủ cần cập nhật: ");
                    String updateId = sc.nextLine().trim();

                    pb.findById(updateId).ifPresentOrElse(
                            player -> {
                                System.out.println("Thông tin hiện tại:");
                                player.displayData();

                                System.out.println("Chọn thông tin muốn cập nhật:");
                                System.out.println("1. Tên cờ thủ");
                                System.out.println("2. Tuổi");
                                System.out.println("3. Elo");
                                System.out.print("Lựa chọn: ");

                                int updateChoice;
                                try {
                                    updateChoice = Integer.parseInt(sc.nextLine().trim());
                                } catch (NumberFormatException e) {
                                    System.out.println("Lựa chọn không hợp lệ!");
                                    return;
                                }

                                switch (updateChoice) {
                                    case 1:
                                        System.out.print("Nhập tên mới: ");
                                        String newName = sc.nextLine().trim();
                                        if (!newName.isEmpty()) {
                                            player.setPlayerName(newName);
                                            System.out.println("Cập nhật tên thành công!");
                                        } else {
                                            System.out.println("Tên không được để trống!");
                                        }
                                        break;
                                    case 2:
                                        try {
                                            System.out.print("Nhập tuổi mới (>= 18): ");
                                            int newAge = Integer.parseInt(sc.nextLine().trim());
                                            if (newAge >= 18) {
                                                player.setAge(newAge);
                                                System.out.println("Cập nhật tuổi thành công");
                                            } else {
                                                System.out.println("Tuổi phải từ 18 trở lên");
                                            }
                                        } catch (NumberFormatException e) {
                                            System.out.println("Giá trị không hợp lệ");
                                        }
                                        break;
                                    case 3:
                                        try {
                                            System.out.print("Nhập Elo mới: ");
                                            int newElo = Integer.parseInt(sc.nextLine().trim());
                                            if (newElo >= 0) {
                                                player.setElo(newElo);
                                                System.out.println("Cập nhật Elo thành công");
                                            } else {
                                                System.out.println("Elo phải >= 0");
                                            }
                                        } catch (NumberFormatException e) {
                                            System.out.println("Giá trị không hợp lệ");
                                        }
                                        break;
                                    default:
                                        System.out.println("Lựa chọn không hợp lệ");
                                }
                            },
                            () -> System.out.println("Mã cờ thủ không tồn tại trong hệ thống!")
                    );
                    break;
                case 4:
                    System.out.print("Nhập mã cờ thủ cần xóa: ");
                    pb.deletePlayer(sc.nextLine().trim());
                    break;
                case 5:
                    System.out.print("Nhập tên cờ thủ muốn tìm: ");
                    List<ChessPlayer> results = pb.searchByName(sc.nextLine().trim());

                    if (results.isEmpty()) {
                        System.out.println("Không tìm thấy cờ thủ nào phù hợp!");
                    } else {
                        results.forEach(ChessPlayer::displayData);
                        System.out.println("Tổng số cờ thủ tìm thấy: " + results.size());
                    }
                    break;
                case 6:
                    List<ChessPlayer> goodPlayers = pb.filterGoodPlayers();
                    if (goodPlayers.isEmpty()) {
                        System.out.println("Không có cờ thủ nào có Elo >= 1500!");
                    } else {
                        System.out.println("Danh sách cờ thủ xuất sắc (Elo >= 1500)");
                        goodPlayers.forEach(ChessPlayer::displayData);
                    }
                    break;
                case 7:
                    List<ChessPlayer> sorted = pb.sortByEloDescending();
                    if (sorted.isEmpty()) {
                        System.out.println("Danh sách cờ thủ hiện đang trống!");
                    } else {
                        System.out.println("Danh sách sau khi sắp xếp giảm dần theo Elo");
                        sorted.forEach(ChessPlayer::displayData);
                    }
                    break;
                case 8:
                    System.out.println("Cảm ơn bạn đã sử dụng chương trình. Tạm biệt!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn từ 1-8.");
            }
        }
    }
}
