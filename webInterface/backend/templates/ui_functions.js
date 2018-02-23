function change_font_size(by) {
    var fontSize = parseInt($('body').css('font-size'), 10);
    $("body").css("font-size", fontSize + by);
}

function reset_font_size() {
    $("body").css("font-size", 16);
}

function change_page_color(background, text) {
    change_text_color(text);
    change_background_color(background);
}

function change_text_color(color) {
    $("body").css("color", color);
    $(".container").css("border-color", color);
    $("#text_color_chooser").css("background-color", color);
    $(".button").css("background-color", color);
    $(".button").css("border", "2px solid " + color);
}

function change_background_color(color) {
    $("body").css("background-color", color);
    $("#background_chooser").css("background-color", color);
    $(".button").css("color", color);
    $(".selected").css("box-shadow", "0 0 0 3px " + color + " inset");
}

function choose_color(isText) {
    var colors = ["white", "gray", "black", "red", "maroon",
        "yellow", "olive", "lime", "green", "aqua", "teal",
        "blue", "navy", "fuchsia", "purple"
    ];

    var mask = document.createElement("div");
    mask.classList.add("mask");

    var picker = document.createElement("div");
    picker.classList.add("picker");
    colors.forEach(function(color) {
        var color_choice = document.createElement("div");
        color_choice.classList.add("color_choice");
        color_choice.style.backgroundColor = color;
        mask.addEventListener("click", function() {
            mask.parentElement.removeChild(mask);
            picker.parentElement.removeChild(picker);
        });
        if (isText) {
            color_choice.addEventListener("click", function() {
                change_text_color(color);
                mask.parentElement.removeChild(mask);
                picker.parentElement.removeChild(picker);
            });
        } else {
            color_choice.addEventListener("click", function() {
                change_background_color(color);
                mask.parentElement.removeChild(mask);
                picker.parentElement.removeChild(picker);
            });
        }
        picker.appendChild(color_choice);
    });
    document.body.appendChild(mask);
    document.body.appendChild(picker);
}
