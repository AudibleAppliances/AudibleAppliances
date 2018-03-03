$.getJSON("/get_voice", function(data) {
    switch (data.voice) {
        case "scottish":
            button = document.getElementById("scottish");
            break;
        case "english":
            button = document.getElementById("english");
            break;
        case "american":
            button = document.getElementById("american");
            break;
        case "male":
            button = document.getElementById("male");
            break;
        default:
    }
    set_voice(button, data.voice);
});

$.getJSON("/get_frequency", function(data) {
    switch (data.frequency) {
        case 15:
            button = document.getElementById("15secs");
            break;
        case 30:
            button = document.getElementById("30secs");
            break;
        case 45:
            button = document.getElementById("45secs");
            break;
        case 60:
            button = document.getElementById("60secs");
            break;
        default:
    }
    set_frequency(button, data.frequency);
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

function set_frequency(button, frequency_choice) {
    colors = get_background_foreground_colors();
    $("#frequency_options").children("div").each(function () {
        this.classList.remove("selected");
        this.classList.add("unselected");
        $(this).css("box-shadow", "none");
    })
    button.classList.remove("unselected");
    button.classList.add("selected");
    $(".selected").css("box-shadow", "0 0 0 3px " + colors.background + " inset");
    var arg = JSON.stringify({
        frequency : frequency_choice
    });
    $.ajax({
        type: "POST",
        url: "/set_frequency",
        data: arg,
        dataType: "application/json"
    });
}
