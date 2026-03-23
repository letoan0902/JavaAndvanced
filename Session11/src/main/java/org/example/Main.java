package org.example;

import org.example.bai1.Bai1;
import org.example.bai2.Bai2;
import org.example.bai3.Bai3;
import org.example.bai4.Bai4;
import org.example.bai5.Bai5App;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Chạy theo tham số: bai1 | bai2
        if (args != null && args.length > 0) {
            if ("bai1".equalsIgnoreCase(args[0])) {
                Bai1.main(args);
                return;
            }
            if ("bai2".equalsIgnoreCase(args[0])) {
                Bai2.main(args);
                return;
            }
            if ("bai3".equalsIgnoreCase(args[0])) {
                // args[1] (nếu có) sẽ là bedId
                String[] forwarded = new String[Math.max(args.length - 1, 0)];
                if (args.length > 1) {
                    System.arraycopy(args, 1, forwarded, 0, args.length - 1);
                }
                Bai3.main(forwarded);
                return;
            }
            if ("bai4".equalsIgnoreCase(args[0])) {
                // args[1] (nếu có) sẽ là patient name input
                String[] forwarded = new String[Math.max(args.length - 1, 0)];
                if (args.length > 1) {
                    System.arraycopy(args, 1, forwarded, 0, args.length - 1);
                }
                Bai4.main(forwarded);
                return;
            }
            if ("bai5".equalsIgnoreCase(args[0])) {
                Bai5App.main(new String[0]);
                return;
            }
        }

        // Mặc định chạy bài 2
        Bai2.main(args);
    }
}