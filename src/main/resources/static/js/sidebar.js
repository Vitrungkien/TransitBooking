
$(document).ready(function () {
    // Toggle Sidebar
    $(".sidebar-burger").on("click", function () {
        $(".sidebar ul").toggleClass("hide");
        $(".sidebar").toggleClass("hide");
    });

    // Menu Items Click
    $(".sidebar ul > li > button").on("click", function () {
        var button = $(this);
        var subMenu = button.next(".sub-menu");
        var allSubMenus = $(".sub-menu");
        var allButtons = $(".sidebar ul button");

        // Check if this submenu is currently open (before we reset)
        // We check style directly or height.
        var isAlreadyOpen = subMenu.length > 0 && subMenu[0].style.height !== "0px" && subMenu[0].style.height !== "";
        // Note: original code set height to "0px". Initially it might be empty string (css default).

        // Reset all
        allSubMenus.css("height", "0px");
        allButtons.removeClass("active");

        if (subMenu.length === 0) {
            button.addClass("active");
            return;
        }

        if (!isAlreadyOpen) {
            var ul = subMenu.find("ul");
            subMenu.css("height", ul.outerHeight() + "px");
            button.addClass("active");
        }
    });
});