$.getJSON("/get_voice", function(data) {
    switch (data.voice) {
        case "scottish":
            button = document.getElementById("scottish");
            break;
        case "female_american":
            button = document.getElementById("female_american");
            break;
        case "male_american":
            button = document.getElementById("male_american");
            break;
        default:
    }
    set_voice(button, data.voice);
});

function get_background_foreground_colors() {
    return {foreground : $("body").css("color"),
            background : $("body").css("background-color")};
}

function set_voice(button, voice_choice) {
    colors = get_background_foreground_colors();
    $("#voice_options").children("div").each(function () {
        this.classList.remove("selected");
        this.classList.add("unselected");
        $(this).css("box-shadow", "none");
    })
    button.classList.remove("unselected");
    button.classList.add("selected");
    $(".selected").css("box-shadow", "0 0 0 3px " + colors.background + " inset");
    var arg = JSON.stringify({
        voice : voice_choice
    });
    $.ajax({
        type: "POST",
        url: "/set_voice",
        data: arg,
        dataType: "application/json"
    });
}
