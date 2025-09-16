# Hệ thống thiết kế và quản lý dịch vụ in ấn biển quảng cáo

## Giới thiệu dự án

Đây là một dự án nhóm, một hệ thống toàn diện để tự động hóa quy trình báo giá, thiết kế và quản lý đơn hàng cho một công ty in ấn. Hệ thống cho phép khách hàng nhận báo giá tức thì, yêu cầu thiết kế riêng hoặc sử dụng AI để tạo mẫu, sau đó theo dõi toàn bộ quá trình từ đặt hàng đến giao hàng.

## Vai trò trong dự án

* **Backend Developer & Team Lead:** Chịu trách nhiệm quản lý dự án, phân công nhiệm vụ, theo dõi tiến độ và đảm bảo chất lượng sản phẩm.
* **Phát triển backend:** Xây dựng toàn bộ hệ thống API, tích hợp các tính năng phức tạp và tối ưu hóa hiệu suất.

## Công nghệ sử dụng

* **Backend:** Java 21, Spring Boot, Spring Security, Spring AI, Spring Event, Spring Cloud (OpenFeign)
* **Database:** PostgreSQL, Redis (Caching)
* **AI & ML:** OpenAI API (fine-tuning), Stable Diffusion (tự host trên VAST AI)
* **Deployment & DevOps:** Docker, GitHub Actions, AWS EC2, AWS S3, Nginx
* **Giao diện:** Thymeleaf
* **Real-time:** Netty Socket.IO
* **Payment Gateway:** PayOS (sử dụng webhook)
* **Email Service:** Brevo

## Các tính năng nổi bật

* **Quy trình CI/CD tự động:** Triển khai **GitHub Actions** và **Docker** để tự động build, test và deploy lên **AWS EC2**, giúp quy trình phát triển nhóm trở nên hiệu quả.
* **Hệ thống AI đa năng:** Tích hợp **Spring AI** và **OpenFeign** để kết nối với các mô hình AI, bao gồm cả **OpenAI API** (cho chatbot và fine-tuning) và **Stable Diffusion** (đã tự host) để tạo hình ảnh.
* **Logic kinh doanh linh hoạt:** Sử dụng **Spring Expression Language (SpEL)** để tính toán các công thức báo giá và cấu hình động, cho phép dễ dàng tùy chỉnh mà không cần thay đổi code.
* **Thông báo theo thời gian thực:** Áp dụng **Netty Socket.IO** để gửi thông báo tức thì đến từng vai trò (Sale, Staff, Designer, Admin) về trạng thái đơn hàng và các cập nhật quan trọng.
* **Quản lý file:** Tích hợp **AWS S3** để lưu trữ các tệp thiết kế và tài liệu, đảm bảo tính bền vững và khả năng mở rộng.
* **Phân quyền và bảo mật:** Hỗ trợ 5 vai trò (Customer, Sale, Staff, Designer, Admin) với quyền hạn rõ ràng, được quản lý chặt chẽ bởi **Spring Security**.

* **Trang web đã triển khai:** [https://songtaoads.io.vn](https://songtaoads.io.vn)
