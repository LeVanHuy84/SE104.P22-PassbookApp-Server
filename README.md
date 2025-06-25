# SE104.P22-PassbookApp
## 🚀 Hướng dẫn chạy project bằng Visual Studio Code

Để debug và chạy project dễ dàng, bạn cần cấu hình file `launch.json` bên trong thư mục `.vscode`.
## 📂 Cấu trúc cần thiết
SE104.P22-PassbookApp-Server/
├── .vscode/
│ └── launch.json
├── src/
├── pom.xml
└── README.md

## ⚙️ Nội dung `launch.json`

Tạo file `.vscode/launch.json` và dán nội dung sau:

```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Current File",
      "request": "launch",
      "mainClass": "${file}"
    },
    {
      "type": "java",
      "name": "ServerApplication",
      "request": "launch",
      "mainClass": "com.group3.server.ServerApplication",
      "projectName": "server",
      "env": {
        "DATABASE_URL": ".....",
        "JWT_ACCESS_EXPIRATION": 3600000,
        "JWT_REFRESH_EXPIRATION": 604800000,
        "JWT_REFRESH_SECRET_KEY": "....",
        "JWT_SECRET_KEY": "...",
        "REDIS_HOST": "****",
        "REDIS_PORT": 6379,
        "REDIS_PASSWORD": "****",
        "REDIS_USERNAME": "default",
        "ADMIN_EMAIL": "....",
        "ADMIN_EMAIL_PASSWORD": "***",
        "ADMIN_ACCOUNT_EMAIL": "admin@gmail.com",
        "ADMIN_ACCOUNT_PASSWORD": "123456"
      }
    }
  ]
}
```
## Hướng dẫn cấu hình file `.vscode/launch.json`

### ✅ Nếu bạn dùng **CSDL PostgreSQL local**, hãy:
- Comment `"DATABASE_URL"` online trong `launch.json`
- Thay thế bằng:
  "DATABASE_URL": "jdbc:postgresql://localhost:5432/your_database_name",
  "DATABASE_USERNAME": "postgres",
  "DATABASE_PASSWORD": "postgres",

### JWT Key
Tạo chuỗi ngẫu nhiên tại:
https://generate-random.org/string-generator

### Redis
Đăng ký Upstash Redis hoặc Redis Cloud

### Gmail (Gửi mail hệ thống)
Vào https://myaccount.google.com/
-> Bật xác thực 2 bước
-> Tạo mật khẩu ứng dụng và dùng thay cho ADMIN_EMAIL_PASSWORD
