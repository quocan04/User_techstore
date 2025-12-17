// Có thể thêm sau: validate form, toast, v.v.
document.addEventListener('DOMContentLoaded', function () {
    console.log('TechStore loaded successfully.');

    // Ví dụ: Tự động đóng alert sau 5 giây (thường dùng cho thông báo thành công)
    setTimeout(function() {
        const alert = document.querySelector('.alert-dismissible');
        if (alert) {
            new bootstrap.Alert(alert).close();
        }
    }, 5000);
});