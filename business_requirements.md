# Hệ thống TransitBooking – Tài liệu Yêu cầu Nghiệp vụ (Business Requirement Document)

Tài liệu này mô tả **logic nghiệp vụ**, **chức năng**, và **mô hình dữ liệu** của dự án **TransitBooking**.  
Mục đích là dùng làm **prompt** để hướng dẫn một AI tái tạo ứng dụng tương tự bằng **một công nghệ khác**.

---

## 1. Tổng quan dự án

**TransitBooking** là một nền tảng kết nối **Hành khách (User)** với **Nhà xe (Seller)**.

- **Hành khách** có thể tìm kiếm chuyến xe, xem chi tiết và đặt vé.
- **Nhà xe (Seller)** quản lý **Cửa hàng (Store/Brand)** của mình, tạo chuyến xe (Product), quản lý điểm dừng và xử lý đơn hàng.
- **Admin** có quyền quan sát và quản lý toàn bộ hệ thống.

---

## 2. Đối tượng & Vai trò (Actors & Roles)

### Guest (Khách)
- Xem trang Home.
- Tìm kiếm chuyến xe theo địa điểm và thời gian.
- Xem chi tiết chuyến xe.
- Phải đăng nhập/đăng ký để đặt vé.

### User (Hành khách)
- Có tất cả quyền của Guest.
- **Đặt vé (Booking)**: Tạo đơn hàng cho một chuyến xe cụ thể.
- **Đơn hàng của tôi (My Orders)**: Xem lịch sử đặt vé cá nhân.
- **Hồ sơ (Profile)**: Quản lý thông tin cá nhân (Tên, Số điện thoại).

### Seller (Nhà xe)
- **Quản lý Store**: Tên, mô tả, số điện thoại.
- **Quản lý Product (Chuyến xe)**:
    - Tạo / Cập nhật / Xóa mềm (Soft delete).
    - Ẩn / Hiện chuyến xe.
- Mỗi chuyến xe bao gồm:
    - Giá vé, lịch trình, thông tin xe.
    - Danh sách **Stops** (điểm đón/trả).
- **Quản lý đơn hàng**:
    - Xem các đơn đặt vé cho chuyến xe của mình.
    - Thao tác: Xác nhận, Hủy, Hoàn thành.
- **Thông báo (Notices)**:
    - Đăng thông báo (trễ giờ, khuyến mãi…) gắn với chuyến xe.
- **Theo dõi trực tiếp (Live Tracking)**:
    - Đánh dấu một Stop là **Right Now** (vị trí hiện tại) để cập nhật cho hành khách.

### Admin
- Dashboard tổng.
- Xem toàn bộ:
    - Users
    - Stores
    - Products
    - Orders
    - Thống kê hệ thống

---

## 3. Chức năng chính & Logic nghiệp vụ

### 3.1. Xác thực (Authentication)

- **Cơ chế**: JWT (JSON Web Token).
- **Lưu trữ**:
    - Token (Authorization) và Role được lưu trong **HTTP Cookies**.
- **Luồng xử lý**:
    - **Login**: Email/Password → Trả về JWT.
    - **Signup (User)**: Đăng ký tài khoản hành khách.
    - **Signup (Seller)**:
        - Đăng ký tài khoản nhà xe.
        - Tự động tạo entity **Store** tương ứng.

---

### 3.2. Công cụ tìm kiếm (Search Engine)

**Tiêu chí tìm kiếm**
- Điểm đi (From).
- Điểm đến (To).
- Thời gian khởi hành.

**Logic**
- Tìm các chuyến xe có:
    - Start Address **và** End Address khớp.
- Thời gian nằm trong khoảng:
    - `[Thời gian nhập] → [Thời gian nhập + 1 giờ]`.
- Ngoài ra hỗ trợ:
    - **Tìm kiếm theo từ khóa** (Tên chuyến xe hoặc mô tả).

**Caching**
- Kết quả tìm kiếm (đặc biệt là danh sách **All Products**) được cache bằng **Redis** để tăng hiệu năng.

---

### 3.3. Hệ thống đặt vé (Booking / Order Flow)

**Yêu cầu đặt vé**
User chọn một **Product (Chuyến xe)** và nhập:
- Địa điểm đón.
- Địa điểm trả.
- Thời gian đón mong muốn.
- Số lượng ghế.
- Ghi chú.

**Tạo đơn**
- Tính **Total Price** = `Giá vé * Số lượng`.
- **Kiểm tra tồn kho**:
    - Lấy `Product.RemainSeat`.
- **Trừ ghế**:
    - `RemainSeat = RemainSeat - Quantity`.
    - *Lưu ý: logic hiện tại chưa chặn khi số ghế < 0, nhưng logic hợp lệ nên có kiểm tra.*
- **Trạng thái ban đầu**:
    - `Chờ xác nhận`.

**Quản lý**
- Seller xem đơn hàng trong dashboard.
- Seller cập nhật trạng thái:
    - `Đã xác nhận`
    - `Hủy`
    - `Đã hoàn thành`

---

### 3.4. Cấu trúc Product (Chuyến xe)

Một **Product** đại diện cho một chuyến xe có lịch trình cố định.

**Thông tin chính**
- Tên, hình ảnh, mô tả.
- Chính sách, tiện ích (Wifi, Nước).

**Thông tin xe**
- Biển số xe.
- Số ghế còn lại.
- Loại xe (Giường nằm / Ghế ngồi).

**Lịch trình**
- Thời gian bắt đầu.
- Thời gian kết thúc.

**Tuyến đường**
- Điểm đi.
- Điểm đến.

**Stops**
- Danh sách các điểm dừng trung gian.
- Mỗi Stop gồm:
    - Thời gian.
    - Địa chỉ.
    - `RightNow` (Boolean – vị trí hiện tại).

**Trạng thái hiển thị**
- `Display`: Hiện / Ẩn.
- `Deleted`: Xóa mềm.

---

### 3.5. Notices (Thông báo)

- Seller có thể tạo **Notice** gắn với một Product cụ thể.
- Thuộc tính:
    - Tiêu đề.
    - Nội dung.
    - Trạng thái hết hạn (`Expired`).
- Dùng để thông báo cho hành khách về thay đổi của chuyến xe.

---

## 4. Chiến lược mô hình dữ liệu (Quan hệ – Relational)

### User
- `ID`
- `Username` (Unique)
- `Password`
- `Role`
- `Phone`
- `Name`

**Quan hệ**
- One-to-One với **Store** (nếu là Seller).
- One-to-Many với **Order** (vai trò người mua).

---

### Store
- `ID`
- `Name`
- `Phone`
- `Introduce`

**Quan hệ**
- Thuộc về một **User**.
- One-to-Many với **Product**.

---

### Product
- `ID`
- `Name`
- `Price`
- `StartTime`, `EndTime`
- `StartAddress`, `EndAddress`
- `RemainSeat`
- `LicensePlate`

**Quan hệ**
- Thuộc về một **Store**.
- One-to-Many với:
    - Stop
    - Notice
    - Order

---

### Stop
- `ID`
- `Time`
- `Address`
- `RightNow`

**Quan hệ**
- Thuộc về một **Product**.

---

### Order
- `ID`
- `TotalPrice`
- `Quantity`
- `Status`
- `PickUpAddress`
- `DestinationAddress`

**Quan hệ**
- Thuộc về một **User** (Buyer).
- Thuộc về một **Product**.

---

## 5. Yêu cầu kỹ thuật khi tái triển khai

- **API**: RESTful.
- **Bảo mật**: Stateless Authentication (JWT).
- **Database**: SQL (khuyến nghị do dữ liệu có cấu trúc).
- **Frontend**:
    - SPA hoặc MVC (tùy lựa chọn).
- **Caching**:
    - Áp dụng cho các API đọc nhiều (ví dụ: danh sách Product).

---

## 6. Yêu cầu giao diện người dùng (UI)

### Trang Home
- Thanh tìm kiếm (Ngày/Giờ/Địa điểm).
- Danh sách chuyến xe nổi bật.

### Kết quả tìm kiếm
- Danh sách chuyến xe.
- Bộ lọc.

### Chi tiết chuyến xe
- Thông tin đầy đủ.
- Danh sách Stops.
- Form **Book Now**.

### Lịch sử đặt vé
- Danh sách đơn hàng (quá khứ / hiện tại).
- Trạng thái đơn.

### Seller Dashboard
- Tabs:
    - Thống kê
    - Products
    - Orders
    - Notices
- **Form Product**:
    - Form phức tạp.
    - Thêm chuyến xe + danh sách Stops động.
- **Bảng Orders**:
    - Nút hành động: Xác nhận / Hủy.

---
