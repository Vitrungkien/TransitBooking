const ToastService = {

    success(message, title = "Thành công") {
        toastr.success(message, title);
    },

    error(message, title = "Lỗi") {
        toastr.error(message, title);
    },

    warning(message, title = "Cảnh báo") {
        toastr.warning(message, title);
    },

    info(message, title = "Thông tin") {
        toastr.info(message, title);
    }
};

toastr.options = {
    closeButton: true,          // ✅ Hiện nút X
    progressBar: true,
    positionClass: "toast-top-right",
    timeOut: "3000",
    extendedTimeOut: "1000",
    showDuration: "300",
    hideDuration: "1000",
    showMethod: "fadeIn",
    hideMethod: "fadeOut"
};

