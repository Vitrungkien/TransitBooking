window.UI = {

    // Toast
    toast: function (message, type = "primary") {
        const toastEl = $("#globalToast");
        toastEl.removeClass().addClass(`toast text-white bg-${type}`);
        toastEl.find(".toast-body").text(message);

        new bootstrap.Toast(toastEl[0]).show();
    },

    // Loader
    showLoader: () => $("#globalLoader").show(),
    hideLoader: () => $("#globalLoader").hide(),

    // Confirm Dialog
    confirm: function (message, onOK) {
        $("#confirmMessage").html(message);
        const modal = new bootstrap.Modal("#globalConfirmModal");
        modal.show();

        $("#confirmOkBtn").off("click").on("click", function () {
            modal.hide();
            if (onOK) onOK();
        });
    },

    // Pagination handler
    gotoPage: function (page) {
        $("#page").val(page);
        $("#searchForm").submit();
    }
};
