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
        "__comment1": "Nếu bạn dùng CSDL PostgreSQL local, hãy dùng cấu hình sau:",
        "__comment2": "DATABASE_URL: jdbc:postgresql://localhost:5432/your_database_name",
        "__comment3": "DATABASE_USERNAME: postgres",
        "__comment4": "DATABASE_PASSWORD: postgres",
        
        "__comment5": "Nếu bạn dùng CSDL online, gán URL vào dòng bên dưới:",
        "DATABASE_URL": ".....",

        "JWT_ACCESS_EXPIRATION": 3600000,
        "JWT_REFRESH_EXPIRATION": 604800000,

        "__comment6": "Random key có thể tạo tại: https://generate-random.org/string-generator?count=1&length=32&has_lowercase=1&has_uppercase=0&has_symbols=0&has_numbers=1&is_pronounceable=0",
        "JWT_REFRESH_SECRET_KEY": "....",
        "JWT_SECRET_KEY": "...",

        "__comment7": "Đăng ký tài khoản Redis (ví dụ: upstash) và cấu hình:",
        "REDIS_HOST": "****",
        "REDIS_PORT": 6379,
        "REDIS_PASSWORD": "****",
        "REDIS_USERNAME": "default",

        "__comment8": "Tạo mật khẩu ứng dụng Gmail bằng tài khoản đã bật xác thực 2 bước:",
        "ADMIN_EMAIL": "....",
        "ADMIN_EMAIL_PASSWORD": "***",

        "ADMIN_ACCOUNT_EMAIL": "admin@gmail.com",
        "ADMIN_ACCOUNT_PASSWORD": "123456"
      }
    }
  ]
}

