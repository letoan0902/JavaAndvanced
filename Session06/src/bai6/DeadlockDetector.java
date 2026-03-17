package bai6;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class DeadlockDetector {
    public static String detectOnce() {
        ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
        long[] ids = mbean.findDeadlockedThreads();
        if (ids == null || ids.length == 0) {
            return "Không phát hiện deadlock.";
        }

        ThreadInfo[] infos = mbean.getThreadInfo(ids, true, true);
        StringBuilder sb = new StringBuilder();
        sb.append("PHÁT HIỆN DEADLOCK! Thread liên quan: ").append(ids.length).append("\n");
        for (ThreadInfo info : infos) {
            sb.append(info.toString()).append("\n");
        }
        sb.append("Gợi ý phá vỡ thủ công: dừng một trong các thread/counter đang kẹt.\n");
        return sb.toString();
    }
}
