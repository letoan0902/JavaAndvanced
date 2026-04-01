# 📚 Tóm Tắt Bài Giảng Java Advanced

> **Ghi chú**: File này tóm tắt toàn bộ 14 session bài giảng Java Advanced.

---

## 📌 MỤC LỤC

| Session | Chủ đề | Nội dung chính |
|---------|--------|----------------|
| 1 | Exception Handling | Ngoại lệ, Try-Catch-Finally |
| 2 | Checked vs Unchecked | Phân loại ngoại lệ |
| 3 | Throw & Throws | Ném và khai báo ngoại lệ |
| 4 | Java 8 & 17 Features | Lambda, Stream, Optional, Records... |
| 5 | Unit Test - Giới thiệu | Lợi ích, các cấp độ kiểm thử |
| 6 | Unit Test - Cách viết | Arrange-Act-Assert, nguyên tắc |
| 7 | Unit Test - JUnit 5 | Annotations, Assertions, triển khai |
| 8-12 | Multithreading | Thread, đồng bộ, giao tiếp, Deadlock |
| 13 | OOAD & SOLID | Phân tích thiết kế OOP, 5 nguyên lý SOLID |
| 14 | Design Patterns | Singleton, Factory, Adapter, Facade |
| 15-16 | JDBC | Connection, Statement, PreparedStatement, CallableStatement |
| 17 | Transaction | ACID, Commit, Rollback, Isolation Levels |

---

## SESSION 1-3: EXCEPTION HANDLING (XỬ LÝ NGOẠI LỆ)

### 1. Ngoại lệ là gì?

- **Định nghĩa**: Sự kiện xảy ra trong quá trình thực thi làm gián đoạn luồng chương trình bình thường.
- **Bản chất**: Là một **Object** chứa thông tin lỗi (kiểu lỗi + trạng thái chương trình).
- **Hệ thống phân cấp**: `Throwable` → `Exception` → các loại ngoại lệ cụ thể.

### 2. Cơ chế hoạt động

```
Lỗi xảy ra → JVM tạo đối tượng Exception trên Heap
           → Tìm kiếm ngược Call Stack
           → Tìm thấy catch phù hợp? → Xử lý, tiếp tục
           → Không tìm thấy? → Chương trình crash, in Stack Trace
```

### 3. Try-Catch-Finally

```java
try {
    // Code "nguy hiểm"
} catch (SpecificException e) {
    // Xử lý lỗi cụ thể
} catch (Exception e) {
    // Xử lý lỗi chung (đặt sau cùng)
} finally {
    // LUÔN chạy - dọn dẹp tài nguyên
}
```

### 4. Phân loại ngoại lệ

```
Throwable
├── Error (Lỗi hệ thống - KHÔNG xử lý được)
│   ├── OutOfMemoryError
│   └── StackOverflowError
└── Exception (Có thể xử lý)
    ├── Checked Exception (Compiler bắt buộc xử lý)
    │   ├── IOException
    │   ├── SQLException
    │   └── FileNotFoundException
    └── RuntimeException → Unchecked (Compiler KHÔNG bắt buộc)
        ├── NullPointerException
        ├── ArithmeticException
        ├── ArrayIndexOutOfBoundsException
        └── NumberFormatException
```

| Tiêu chí | Checked Exception | Unchecked Exception |
|----------|-------------------|---------------------|
| Kế thừa từ | `Exception` (trừ RuntimeException) | `RuntimeException` |
| Nguyên nhân | Ngoài tầm kiểm soát (I/O, mạng, DB) | Lỗi logic lập trình |
| Compiler kiểm tra | ✅ Bắt buộc try-catch hoặc throws | ❌ Không bắt buộc |
| Cách xử lý đúng | try-catch hoặc throws | Sửa logic code, dùng `if` kiểm tra |

### 5. Throw vs Throws

| Đặc điểm | `throw` | `throws` |
|-----------|---------|----------|
| Vị trí | Bên trong thân phương thức | Sau tên phương thức, trước `{` |
| Số lượng | Ném **1** đối tượng mỗi lần | Khai báo **nhiều** loại (dấu phẩy) |
| Mục đích | **Hành động** ném lỗi cụ thể | **Khai báo** khả năng xảy ra lỗi |

```java
// throw - Chủ động ném ngoại lệ
public void rutTien(double soTien) throws InsufficientFundsException {
    if (soTien > soDu) {
        throw new InsufficientFundsException("Không đủ tiền!"); // throw
    }
}

// throws - Đẩy trách nhiệm cho caller
public void xuLyGiaoDich() throws InsufficientFundsException {
    rutTien(1000000);  // caller phải try-catch hoặc tiếp tục throws
}
```

### 6. Sai lầm thường gặp ⚠️

1. **Nuốt ngoại lệ**: `catch(Exception e) { }` → Debug bất khả thi
2. **Bắt quá chung**: Luôn dùng `catch(Exception e)` → Không phân biệt được lỗi
3. **Try-catch Unchecked thay vì sửa logic**: Dùng `if (obj != null)` thay vì try-catch `NullPointerException`
4. **Throw trong finally**: Nuốt mất ngoại lệ gốc
5. **Ném ngoại lệ quá chung**: `throw new Exception()` → Dùng `IllegalArgumentException` hoặc Custom Exception

---

## SESSION 4: JAVA 8 & JAVA 17 FEATURES

### A. JAVA 8 (Phát hành 03/2014 - LTS đến ~2030)

#### 1. Default & Static Methods trong Interface

**Vấn đề trước Java 8**: Thêm method mới vào interface → phá vỡ tất cả class implement.

```java
// Default method - có thân, có thể override
public interface Vehicle {
    default void stop() { System.out.println("Dừng lại"); }
}

// Static method - không override, không kế thừa, gọi qua tên interface
public interface MathUtils {
    static int add(int a, int b) { return a + b; }
}
// Gọi: MathUtils.add(1, 2);
```

| Tiêu chí | Default Method | Static Method |
|----------|---------------|---------------|
| Kế thừa | ✅ Có | ❌ Không |
| Override | ✅ Có | ❌ Không |
| Gọi qua object | ✅ Có | ❌ Không |

> **Diamond Problem**: Khi class implement 2 interface có cùng default method → **bắt buộc override** để tránh mơ hồ.

#### 2. Functional Interface & Lambda Expression

**Functional Interface** = Interface có **đúng 1 abstract method** (có thể có thêm default/static methods).

```java
@FunctionalInterface
public interface Calculator {
    int calculate(int a, int b); // Chỉ 1 abstract method
}
```

**Lambda Expression** - Cú pháp:
```
(tham số) -> { thân hàm }
```

```java
// Không tham số
Runnable r = () -> System.out.println("Hello");

// Một tham số (bỏ ngoặc được)
Consumer<String> c = s -> System.out.println(s);

// Nhiều tham số
Comparator<Integer> comp = (a, b) -> a - b;

// Có thân khối
Function<Integer, String> f = n -> {
    if (n > 0) return "Dương";
    else return "Không dương";
};
```

**4 Functional Interface chuẩn trong `java.util.function`**:

| Interface | Nhận | Trả | Dùng khi |
|-----------|------|------|----------|
| `Predicate<T>` | T | boolean | Kiểm tra điều kiện (filter) |
| `Function<T,R>` | T | R | Biến đổi dữ liệu (map) |
| `Consumer<T>` | T | void | Tiêu thụ dữ liệu (forEach, ghi file) |
| `Supplier<T>` | — | T | Tạo/cung cấp dữ liệu |

#### 3. Method References (Tham chiếu phương thức)

Rút gọn Lambda khi chỉ gọi 1 method có sẵn. Dùng toán tử `::`.

| Loại | Cú pháp | Ví dụ Lambda → Method Ref |
|------|---------|--------------------------|
| Static Method | `Class::staticMethod` | `s -> Integer.parseInt(s)` → `Integer::parseInt` |
| Instance (object cụ thể) | `object::method` | `s -> System.out.println(s)` → `System.out::println` |
| Instance (kiểu class) | `Class::method` | `(s1,s2) -> s1.equals(s2)` → `String::equals` |
| Constructor | `Class::new` | `() -> new StringBuilder()` → `StringBuilder::new` |

#### 4. Stream API

> **Stream ≠ IO Stream**. Stream API (`java.util.stream`) xử lý Collection theo pipeline.

**Pipeline gồm 3 phần**:
```
Source → Intermediate Ops (lazy) → Terminal Op (eager, kích hoạt pipeline)
```

**Intermediate Operations** (trả về Stream, lazy):

| Operation | Mô tả |
|-----------|--------|
| `filter(Predicate)` | Lọc phần tử thỏa điều kiện |
| `map(Function)` | Biến đổi phần tử |
| `flatMap(Function)` | Biến đổi + flatten |
| `sorted()` / `sorted(Comparator)` | Sắp xếp |
| `distinct()` | Loại trùng |
| `limit(n)` / `skip(n)` | Giới hạn / bỏ qua |

**Terminal Operations** (kết thúc pipeline):

| Operation | Mô tả | Trả về |
|-----------|--------|--------|
| `forEach(Consumer)` | Xử lý từng phần tử | void |
| `collect(Collector)` | Gom thành List/Set/Map | Collection |
| `reduce(BinaryOperator)` | Gộp thành 1 giá trị | Optional/T |
| `count()` | Đếm | long |
| `findFirst()` | Phần tử đầu | Optional |
| `anyMatch` / `allMatch` | Kiểm tra điều kiện | boolean |

```java
List<String> names = people.stream()
    .filter(p -> p.getAge() > 18)       // Intermediate
    .map(Person::getName)                // Intermediate
    .sorted()                            // Intermediate
    .collect(Collectors.toList());       // Terminal
```

#### 5. Optional\<T\>

Giải quyết NullPointerException bằng cách biểu diễn "giá trị có thể có hoặc không".

**Tạo Optional**:
```java
Optional.of(value)           // value != null, nếu null → NPE
Optional.ofNullable(value)   // value có thể null → Optional.empty()
Optional.empty()             // Rỗng
```

**Các API quan trọng**:

| Phương thức | Ý nghĩa |
|-------------|----------|
| `isPresent()` / `isEmpty()` | Kiểm tra có giá trị |
| `ifPresent(Consumer)` | Chạy action nếu có |
| `orElse(default)` | Giá trị mặc định (luôn tạo default) |
| `orElseGet(Supplier)` | Giá trị mặc định (lazy, tạo khi cần) |
| `orElseThrow(Supplier)` | Ném exception nếu rỗng |
| `map(Function)` | Biến đổi Optional\<T\> → Optional\<R\> |
| `flatMap(Function)` | Tránh Optional lồng Optional |
| `filter(Predicate)` | Giữ giá trị nếu thỏa điều kiện |

#### 6. DateTime API (`java.time`)

Thay thế `Date`/`Calendar` cũ (mutable, API khó đọc, không thread-safe).

| Class | Ý nghĩa | Ví dụ |
|-------|----------|-------|
| `LocalDate` | Chỉ ngày | `2024-03-27` |
| `LocalTime` | Chỉ giờ | `14:30:00` |
| `LocalDateTime` | Ngày + giờ (phổ biến nhất) | `2024-03-27T14:30:00` |
| `ZonedDateTime` | Có timezone | `2024-03-27T14:30+07:00[Asia/Ho_Chi_Minh]` |
| `Instant` | Timestamp tuyệt đối | Epoch-based |
| `Duration` | Khoảng thời gian (giây/ms) | — |
| `Period` | Khoảng thời gian (năm/tháng/ngày) | — |

**Đặc điểm thiết kế**: Immutable, Thread-safe, Fluent API.

### B. JAVA 17 FEATURES

| Tính năng | Mục đích |
|-----------|----------|
| **Records** | Data carrier class không boilerplate (auto: constructor, getter, equals, hashCode, toString) |
| **Sealed Classes** | Giới hạn class nào được kế thừa (giữa `final` và mở hoàn toàn) |
| **Pattern Matching instanceof** | Kiểm tra kiểu + ép kiểu tự động trong 1 bước |
| **Switch Expressions** | Switch trả về giá trị, dùng `->` tránh fall-through |
| **Text Blocks** | Chuỗi nhiều dòng `"""..."""`, tự căn lề |

```java
// Record
record UserDTO(String name, int age) {} // Compiler tự tạo mọi thứ

// Pattern Matching instanceof
if (obj instanceof String s) {
    System.out.println(s.length()); // Không cần cast
}

// Sealed Classes
sealed interface Shape permits Circle, Square {}

// Switch Expression
String label = switch (day) {
    case MON, TUE -> "Đầu tuần";
    case SAT, SUN -> "Cuối tuần";
    default -> "Giữa tuần";
};
```

---

## SESSION 5-7: UNIT TESTING

### 1. Unit Test là gì?

- Kiểm thử đơn vị mã nguồn nhỏ nhất (hàm, phương thức, lớp) một cách **độc lập**.
- Do **Developer** thực hiện.
- Framework chính: **JUnit 5** (JUnit Jupiter).

**4 cấp độ kiểm thử**: Unit → Integration → System → Acceptance

### 2. Lợi ích

| Lợi ích | Mô tả |
|---------|--------|
| Phát hiện lỗi sớm | Sửa lỗi ở Unit rẻ hơn nhiều so với Production |
| Dễ debug | Phạm vi hẹp → biết chính xác lỗi ở đâu |
| Cải thiện thiết kế | Buộc code phải module hóa, KISS, Single Responsibility |
| Refactoring an toàn | "Lưới bảo hiểm" khi tối ưu code cũ |
| Tài liệu sống | Test case = mô tả cách dùng hàm |

### 3. Quy trình viết Unit Test (AAA / Given-When-Then)

```
Arrange (Given)  → Chuẩn bị dữ liệu, khởi tạo objects, mocks
Act     (When)   → Gọi phương thức cần test
Assert  (Then)   → So sánh kết quả thực tế vs mong đợi
Verify           → Kiểm tra tương tác mock (nếu có)
```

### 4. Nguyên tắc vàng

- Một test case → **một logic** duy nhất
- **Độc lập**: không phụ thuộc test khác, DB, Network
- Tên test **rõ ràng**, mô tả đang kiểm tra gì
- Tập trung vào **output/behavior**, không kiểm tra chi tiết implementation
- Coverage mục tiêu: **70-90%**

### 5. JUnit 5 - Annotations

| Annotation | Ý nghĩa |
|------------|----------|
| `@Test` | Khai báo phương thức test |
| `@BeforeEach` | Chạy **trước mỗi** test (khởi tạo data sạch) |
| `@AfterEach` | Chạy **sau mỗi** test (dọn dẹp) |
| `@BeforeAll` | Chạy **1 lần** trước tất cả test (static) |
| `@AfterAll` | Chạy **1 lần** sau tất cả test (static) |

### 6. JUnit 5 - Assertions

```java
assertEquals(expected, actual)          // Bằng nhau
assertTrue(condition)                   // Điều kiện đúng
assertFalse(condition)                  // Điều kiện sai
assertNull(obj) / assertNotNull(obj)    // Null / không null
assertThrows(Exception.class, () -> {}) // Phải ném đúng exception
assertArrayEquals(expected, actual)     // Mảng giống nhau
```

### 7. Ví dụ minh họa

```java
class CalculatorTest {
    Calculator calc;

    @BeforeEach
    void setUp() { calc = new Calculator(); }      // Arrange

    @Test
    void testAdd_shouldReturnSum() {
        int result = calc.add(2, 3);                // Act
        assertEquals(5, result);                     // Assert
    }

    @Test
    void testDivide_shouldThrowWhenDivideByZero() {
        assertThrows(ArithmeticException.class,      // Assert
            () -> calc.divide(10, 0));               // Act
    }
}
```

---

## SESSION 8-12: MULTITHREADING (ĐA LUỒNG)

### 1. Khái niệm cơ bản

| Thuật ngữ | Định nghĩa |
|-----------|-------------|
| **Process** | Một chương trình đang chạy (có không gian bộ nhớ riêng) |
| **Thread** | Đơn vị thực thi nhỏ nhất trong Process |
| **Multithreading** | Kỹ thuật chạy nhiều thread đồng thời trong 1 process |

**Lợi ích**: Tăng tính phản hồi (UI không đơ), tận dụng CPU đa nhân, chia sẻ tài nguyên dễ dàng.

### 2. Tạo Thread - 2 cách

**Cách 1: Extends Thread**
```java
class MyThread extends Thread {
    @Override
    public void run() { /* công việc */ }
}
MyThread t = new MyThread();
t.start(); // PHẢI dùng start(), KHÔNG dùng run()
```

**Cách 2: Implements Runnable (✅ KHUYÊN DÙNG)**
```java
class MyTask implements Runnable {
    @Override
    public void run() { /* công việc */ }
}
Thread t = new Thread(new MyTask());
t.start();
```

| Tiêu chí | extends Thread | implements Runnable |
|----------|---------------|---------------------|
| Kế thừa thêm class khác | ❌ Không | ✅ Có |
| Tái sử dụng | Thấp | Cao |
| Tách biệt trách nhiệm | ❌ | ✅ |

> ⚠️ **QUAN TRỌNG**: `start()` tạo thread mới. `run()` chỉ gọi method bình thường. **KHÔNG** gọi `start()` 2 lần trên cùng 1 Thread object → `IllegalThreadStateException`.

### 3. Quản lý đồng bộ Thread

| Phương thức | Mục đích | Trạng thái Thread |
|-------------|----------|-------------------|
| `Thread.sleep(ms)` | Tạm dừng có thời hạn | → Timed Waiting |
| `thread.join()` | Chờ thread khác kết thúc | → Waiting |
| `Thread.yield()` | Gợi ý nhường CPU (không đảm bảo) | → Runnable |

```java
Thread worker = new Thread(() -> { /* tải ảnh */ });
worker.start();
worker.join();  // Main thread CHỜ worker xong mới tiếp tục
System.out.println("Worker đã xong!");
```

> ⚠️ `sleep()` và `join()` ném `InterruptedException` → phải try-catch.

### 4. Giao tiếp giữa các Thread (Wait/Notify)

Giải quyết vấn đề **Busy Waiting** (vòng lặp kiểm tra liên tục → lãng phí CPU).

| Phương thức | Mô tả |
|-------------|--------|
| `wait()` | Thread ngủ, giải phóng lock, chờ được đánh thức |
| `notify()` | Đánh thức **1** thread ngẫu nhiên đang wait |
| `notifyAll()` | Đánh thức **tất cả** thread đang wait |

**Quy tắc bắt buộc**:
1. Chỉ gọi trong khối `synchronized`
2. Gọi `wait()` trong vòng lặp `while` (tránh đánh thức giả)
3. Vi phạm → `IllegalMonitorStateException`

```java
// Producer-Consumer Pattern
synchronized(kho) {
    while (kho.isFull()) { kho.wait(); }   // Producer chờ kho hết đầy
    kho.add(sanPham);
    kho.notifyAll();                        // Đánh thức Consumer
}

synchronized(kho) {
    while (kho.isEmpty()) { kho.wait(); }  // Consumer chờ có hàng
    kho.remove();
    kho.notifyAll();                        // Đánh thức Producer
}
```

### 5. Deadlock và phòng chống

**Deadlock** = Các thread chờ nhau vô hạn → ứng dụng treo cứng.

**4 điều kiện (phải đủ cả 4)**:

| # | Điều kiện | Mô tả |
|---|-----------|--------|
| 1 | Mutual Exclusion | Tài nguyên chỉ 1 thread dùng/lần |
| 2 | Hold and Wait | Giữ tài nguyên A, chờ tài nguyên B |
| 3 | No Preemption | Không thể giật tài nguyên từ thread khác |
| 4 | Circular Wait | Vòng tròn chờ: T1→R2, T2→R1 |

**Cách phòng tránh** (phá vỡ ≥ 1 điều kiện):

1. **Lock Ordering** (✅ phổ biến nhất): Tất cả thread khóa tài nguyên theo **cùng thứ tự**
2. **tryLock() với timeout**: Dùng `java.util.concurrent.locks`, bỏ cuộc nếu quá thời gian
3. **Dùng concurrent collections**: `ConcurrentHashMap`, `BlockingQueue` thay vì tự quản lý `synchronized`

```java
// ❌ DEADLOCK
Thread1: synchronized(A) { synchronized(B) { ... } }
Thread2: synchronized(B) { synchronized(A) { ... } }

// ✅ FIX: Cùng thứ tự
Thread1: synchronized(A) { synchronized(B) { ... } }
Thread2: synchronized(A) { synchronized(B) { ... } }
```

---

## SESSION 13: OOAD & NGUYÊN LÝ SOLID

### 1. OOAD (Object-Oriented Analysis and Design)

- **Analysis (Phân tích)**: Xác định đối tượng, mối quan hệ → "Hệ thống làm cái gì?"
- **Design (Thiết kế)**: Chi tiết hóa lớp, phương thức, cách giao tiếp → "Làm thế nào?"
- **Mục đích**: Kiểm soát sự phức tạp, giảm ảnh hưởng dây chuyền khi thay đổi yêu cầu.

### 2. SOLID - 5 Nguyên lý thiết kế

| Ký tự | Nguyên lý | Ý nghĩa cốt lõi |
|-------|-----------|------------------|
| **S** | Single Responsibility (SRP) | Một lớp chỉ có **một lý do** để thay đổi |
| **O** | Open/Closed (OCP) | **Mở** cho mở rộng, **đóng** cho sửa đổi |
| **L** | Liskov Substitution (LSP) | Lớp con phải **thay thế được** lớp cha |
| **I** | Interface Segregation (ISP) | Nhiều interface nhỏ tốt hơn 1 interface lớn |
| **D** | Dependency Inversion (DIP) | Phụ thuộc vào **abstraction**, không vào cụ thể |

#### S - Single Responsibility Principle

```java
// ❌ Vi phạm: Employee làm quá nhiều việc
class Employee {
    void tinhLuong() { ... }
    void luuDatabase() { ... }
    void inBaoCao() { ... }
}

// ✅ Đúng: Tách riêng trách nhiệm
class Employee { /* chỉ chứa thông tin */ }
class SalaryCalculator { /* tính lương */ }
class EmployeeRepository { /* lưu trữ */ }
class ReportGenerator { /* báo cáo */ }
```

#### O - Open/Closed Principle

```java
// ✅ Dùng interface (Strategy Pattern) để mở rộng
interface SalaryStrategy { double calculate(Employee e); }
class FullTimeSalary implements SalaryStrategy { ... }
class PartTimeSalary implements SalaryStrategy { ... }
// Thêm loại mới → tạo class mới, KHÔNG sửa code cũ
```

#### L - Liskov Substitution Principle

```java
// ❌ Penguin extends Bird nhưng không fly() được
// ✅ Tách interface Flyable riêng
interface Flyable { void fly(); }
class Eagle extends Bird implements Flyable { ... }
class Penguin extends Bird { /* không implement Flyable */ }
```

#### I - Interface Segregation Principle

```java
// ❌ Interface quá to
interface Worker { void work(); void eat(); void sleep(); }

// ✅ Tách nhỏ
interface Workable { void work(); }
interface Eatable { void eat(); }
class Robot implements Workable { ... }       // Không cần eat()
class Human implements Workable, Eatable { ... }
```

#### D - Dependency Inversion Principle

```java
// ❌ Module cấp cao phụ thuộc module cấp thấp
class ReportGenerator {
    MySQLRepository repo = new MySQLRepository(); // cứng!
}

// ✅ Cả hai phụ thuộc abstraction
interface EmployeeRepository { List<Employee> findAll(); }
class MySQLRepository implements EmployeeRepository { ... }
class MongoDBRepository implements EmployeeRepository { ... }

class ReportGenerator {
    EmployeeRepository repo; // Inject từ bên ngoài (DI)
    ReportGenerator(EmployeeRepository repo) { this.repo = repo; }
}
```

> **DIP ≠ DI**: DIP là nguyên lý, Dependency Injection là kỹ thuật thực hiện DIP.

---

## SESSION 14: DESIGN PATTERNS

### 1. Khái niệm

- **Design Pattern** = Giải pháp tổng thể đã được tối ưu cho các vấn đề thiết kế thường gặp.
- Không phải code copy-paste mà là **khuôn mẫu tư duy**.
- **23 pattern kinh điển** (GoF) chia 3 nhóm:

| Nhóm | Mục đích | Ví dụ |
|------|----------|-------|
| **Creational** (Tạo lập) | Khởi tạo đối tượng linh hoạt | Singleton, Factory Method |
| **Structural** (Cấu trúc) | Kết hợp class/object thành cấu trúc lớn | Adapter, Facade |
| **Behavioral** (Hành vi) | Phân chia trách nhiệm, giao tiếp | Observer, Strategy |

### 2. Singleton Pattern

**Mục đích**: Đảm bảo class chỉ có **duy nhất 1 instance** + cung cấp điểm truy cập toàn cục.

```java
public class Logger {
    private static Logger instance;
    private Logger() {} // Constructor private!

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }
    public void log(String msg) { System.out.println(msg); }
}
// Gọi: Logger.getInstance().log("Hello");
```

> ⚠️ Phiên bản trên **không thread-safe**. Trong đa luồng cần dùng `synchronized` hoặc `enum`.

### 3. Factory Method Pattern

**Mục đích**: Ủy thác việc khởi tạo đối tượng cho lớp con, tuân thủ OCP.

```java
// Interface sản phẩm
interface Bill { void print(); }
class DineInBill implements Bill { ... }
class TakeawayBill implements Bill { ... }

// Factory
interface BillFactory { Bill createBill(); }
class DineInBillFactory implements BillFactory {
    public Bill createBill() { return new DineInBill(); }
}
// Thêm loại mới → tạo class + factory mới, KHÔNG sửa code cũ
```

### 4. Adapter Pattern

**Mục đích**: Cho phép các class có interface **khác nhau** giao tiếp được với nhau ("cục chuyển đổi").

```java
// Interface mới
interface IModernReporter { ReportData generate(String input); }

// Class cũ (không sửa được)
class OldCalculator { int[] calculate() { ... } }

// Adapter - chuyển đổi interface cũ → mới
class CalculatorAdapter implements IModernReporter {
    private OldCalculator old = new OldCalculator();
    public ReportData generate(String input) {
        int[] data = old.calculate();  // Gọi hệ thống cũ
        return convertToReportData(data); // Chuyển đổi
    }
}
```

### 5. Facade Pattern

**Mục đích**: Cung cấp **interface đơn giản** cho hệ thống con phức tạp.

```java
class ReportFacade {
    private IModernReporter reporter;
    private OldReporter exporter;

    public void generateFullReport() {
        ReportData data = reporter.generate("input"); // Bước 1
        exporter.export(data);                         // Bước 2
        // Client chỉ cần gọi 1 method duy nhất!
    }
}
```

---

## SESSION 15-16: JDBC (Java Database Connectivity)

### 1. JDBC là gì?

Bộ API chuẩn của Java để kết nối và thực thi SQL tới cơ sở dữ liệu.

**Thành phần cốt lõi**:

| Thành phần | Vai trò |
|------------|--------|
| `DriverManager` | Quản lý Driver, thiết lập kết nối |
| `Connection` | Đại diện kết nối vật lý đến DB |
| `Statement` | Gửi câu lệnh SQL tĩnh |
| `PreparedStatement` | SQL biên dịch trước + tham số hóa (chống SQL Injection) |
| `CallableStatement` | Gọi Stored Procedure |
| `ResultSet` | Lưu trữ dữ liệu trả về từ SELECT |

### 2. Kết nối Database

```java
String url = "jdbc:mysql://localhost:3306/pharmacy_db";
String user = "root";
String pass = "password";

try (Connection conn = DriverManager.getConnection(url, user, pass)) {
    System.out.println("Kết nối thành công!");
} catch (SQLException e) {
    e.printStackTrace();
}
```

> ⚠️ Cần thêm file `mysql-connector-j.jar` vào classpath. Luôn đóng `Connection`.

### 3. Statement - Truy vấn và CRUD

| Phương thức | Dùng cho | Trả về |
|-------------|----------|--------|
| `executeQuery(sql)` | SELECT | `ResultSet` |
| `executeUpdate(sql)` | INSERT, UPDATE, DELETE | `int` (số dòng bị tác động) |

```java
// SELECT
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery("SELECT * FROM drugs");
while (rs.next()) {  // Di chuyển cursor + kiểm tra còn dữ liệu
    String name = rs.getString("name");
    int qty = rs.getInt("quantity");
}

// INSERT / UPDATE / DELETE
int rows = stmt.executeUpdate("INSERT INTO drugs VALUES (...)");
if (rows > 0) System.out.println("Thành công!");
```

### 4. PreparedStatement (✅ KHUYÊN DÙNG)

**Ưu điểm**: Chống **SQL Injection** 100%, hiệu năng tốt hơn khi lặp lại.

```java
String sql = "SELECT * FROM drugs WHERE name = ? AND price < ?";
try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.setString(1, drugName);   // ? thứ 1 (index từ 1)
    pstmt.setDouble(2, maxPrice);   // ? thứ 2
    ResultSet rs = pstmt.executeQuery(); // KHÔNG truyền sql vào đây!
}
```

**Các phương thức set phổ biến**:

| Phương thức | Kiểu Java | Kiểu SQL |
|-------------|-----------|----------|
| `setString(i, val)` | String | VARCHAR |
| `setInt(i, val)` | int | INTEGER |
| `setDouble(i, val)` | double | DECIMAL |
| `setDate(i, val)` | java.sql.Date | DATE |

### 5. CallableStatement - Gọi Stored Procedure

```java
String sql = "{call get_drug_statistics(?, ?)}";
try (CallableStatement cstmt = conn.prepareCall(sql)) {
    cstmt.setInt(1, drugId);                          // Tham số IN
    cstmt.registerOutParameter(2, Types.INTEGER);     // Đăng ký OUT
    cstmt.execute();
    int totalSold = cstmt.getInt(2);                  // Nhận OUT
}
```

**Quy trình**: Đăng ký OUT (`registerOutParameter`) → Execute → Get kết quả.

> ⚠️ Quên `registerOutParameter` → lỗi. Index vẫn đếm từ **1**.

---

## SESSION 17: TRANSACTION TRONG JDBC

### 1. Transaction là gì?

Một đơn vị công việc logic gồm **nhiều thao tác DB** → hoàn thành toàn bộ hoặc không gì cả.

### 2. Tính chất ACID

| Tính chất | Ý nghĩa |
|-----------|--------|
| **A**tomicity | Tất cả thành công HOẶC tất cả rollback |
| **C**onsistency | DB luôn ở trạng thái hợp lệ sau transaction |
| **I**solation | Các transaction độc lập, không ảnh hưởng nhau |
| **D**urability | Sau commit → thay đổi lưu vĩnh viễn |

### 3. Quản lý Transaction trong JDBC

```java
try {
    conn.setAutoCommit(false);  // Tắt auto-commit

    // Thao tác 1: Trừ tiền tài khoản A
    pstmt1.executeUpdate();
    // Thao tác 2: Cộng tiền tài khoản B
    pstmt2.executeUpdate();

    conn.commit();              // Tất cả OK → Commit
} catch (SQLException e) {
    conn.rollback();            // Có lỗi → Rollback toàn bộ
} finally {
    conn.setAutoCommit(true);   // Khôi phục mặc định
}
```

### 4. Rollback

- **Rollback** = Hủy bỏ tất cả thay đổi kể từ lần commit cuối.
- Dùng khi: Giao dịch thất bại, vi phạm toàn vẹn dữ liệu, có exception.
- Thứ tự: Luôn **commit/rollback trước khi đóng connection**.

### 5. Transaction Isolation Levels

| Mức độ | Dirty Read | Non-Repeatable | Phantom |
|--------|-----------|----------------|----------|
| `READ_UNCOMMITTED` | ✅ Có | ✅ Có | ✅ Có |
| `READ_COMMITTED` | ❌ Không | ✅ Có | ✅ Có |
| `REPEATABLE_READ` | ❌ Không | ❌ Không | ✅ Có |
| `SERIALIZABLE` | ❌ Không | ❌ Không | ❌ Không |

```java
// Thiết lập mức cô lập
conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
```

- **Dirty Read**: Đọc dữ liệu chưa commit của transaction khác.
- **Non-Repeatable Read**: Đọc cùng dữ liệu 2 lần → kết quả khác nhau.
- **Phantom Read**: Đọc tập dữ liệu 2 lần → số hàng thay đổi.

> ⚠️ Mức cao hơn → an toàn hơn nhưng **hiệu suất thấp hơn**.

---

## 🗂️ BẢNG TỔNG HỢP CÁC KHÁI NIỆM ĐÃ HỌC

| STT | Khái niệm | Thuộc Session | Ghi chú |
|-----|-----------|---------------|---------|
| 1 | Try-Catch-Finally | 1 | Xử lý ngoại lệ cơ bản |
| 2 | Checked / Unchecked Exception | 2 | Compiler có/không bắt buộc xử lý |
| 3 | throw / throws | 3 | Ném lỗi / Khai báo khả năng lỗi |
| 4 | Default & Static methods | 4 | Mở rộng Interface không phá code cũ |
| 5 | Functional Interface | 4 | Interface 1 abstract method → target cho Lambda |
| 6 | Lambda Expression | 4 | `(params) -> { body }` |
| 7 | Method Reference | 4 | `Class::method` - rút gọn Lambda |
| 8 | Stream API | 4 | Pipeline: Source → Intermediate → Terminal |
| 9 | Optional\<T\> | 4 | Tránh NullPointerException |
| 10 | DateTime API (java.time) | 4 | Immutable, thread-safe thay Date/Calendar |
| 11 | Records, Sealed, Pattern Matching | 4 | Java 17 - giảm boilerplate |
| 12 | Unit Test & JUnit 5 | 5-7 | AAA pattern, Annotations, Assertions |
| 13 | Thread & Multithreading | 8-12 | Process/Thread, extends/implements |
| 14 | sleep, join, yield | 8-12 | Điều phối thread |
| 15 | wait, notify, notifyAll | 8-12 | Giao tiếp thread, Producer-Consumer |
| 16 | Deadlock | 8-12 | 4 điều kiện, Lock Ordering |
| 17 | OOAD | 13 | Phân tích & Thiết kế hướng đối tượng |
| 18 | SOLID (S, O, L, I, D) | 13 | 5 nguyên lý thiết kế OOP |
| 19 | Singleton Pattern | 14 | 1 instance duy nhất, điểm truy cập toàn cục |
| 20 | Factory Method Pattern | 14 | Ủy thác khởi tạo cho lớp con |
| 21 | Adapter Pattern | 14 | Chuyển đổi interface không tương thích |
| 22 | Facade Pattern | 14 | Đơn giản hóa hệ thống phức tạp |
| 23 | JDBC & Connection | 15-16 | API kết nối Java ↔ Database |
| 24 | Statement & ResultSet | 15-16 | Truy vấn SQL tĩnh |
| 25 | PreparedStatement | 15-16 | SQL tham số hóa, chống SQL Injection |
| 26 | CallableStatement | 15-16 | Gọi Stored Procedure |
| 27 | Transaction (ACID) | 17 | Commit / Rollback |
| 28 | Transaction Isolation | 17 | 4 mức cô lập giao dịch |

---

> ✅ **Đã hoàn thành tóm tắt toàn bộ 14 session bài giảng Java Advanced.**
