$.getJSON("/get_boxes", function(data) {
    $.each(data.boxes, function(key, val) {
        var box = document.createElement("div");
        box.className = "square";
        box.style.borderColor = allowed_colours[Math.floor(Math.random() * allowed_colours.length)];
        $(box).css({
            "left": $("#canvas").position().left + val.corner[0] * $("#canvas").width(),
            "top": $("#canvas").position().top + val.corner[1] * $("#canvas").height(),
            "width": val.width * $("#canvas").width(),
            "height": val.height * $("#canvas").height()
        });
        $("#canvas").append(box);
    });
});

var allowed_colours = ["orange", "blue", "purple"];

mouse_down = function() {}

function do_select(component_name) {
    mouse_down = function(e) {
        var start_x = e.pageX;
        var start_y = e.pageY;
        var box = document.createElement("div");
        box.className = "square";
        box.style.borderColor = allowed_colours[Math.floor(Math.random() * allowed_colours.length)];
        var resize_box = function(e) {
            update_box(box, start_x, start_y, e);
        };
        var commit_box = function(speak) {
            var type = component_name;
            var arg = JSON.stringify({
                spoken: speak,
                box: {
                    type: type,
                    corner: [
                        (($(box).position().left - $("#canvas").position().left) / $("#canvas").width()),
                        (($(box).position().top - $("#canvas").position().top) / $("#canvas").height())
                    ],
                    width: $(box).width() / $("#canvas").width(),
                    height: $(box).height() / $("#canvas").height()
                }
            });
            $.ajax({
                type: "POST",
                url: "/new_box",
                data: arg,
                dataType: "application/json"
            });
        };
        document.getElementById('canvas').addEventListener("mousemove", resize_box);
        var mouse_up_and_clean_up = function() {
            document.getElementById('canvas').removeEventListener("mousemove", resize_box);
            document.getElementById('canvas').removeEventListener("mouseup", mouse_up_and_clean_up);
            speek(commit_box);
        };
        document.getElementById('canvas').addEventListener("mouseup", mouse_up_and_clean_up);
    }
    $("#first_screen").toggle();
    $("#select_pane").toggle();
}

function speek(commit_box) {
    var mask = document.createElement("div");
    mask.classList.add("mask");

    var picker = document.createElement("div");
    picker.classList.add("spoken_choice");

    var do_speak = document.createElement("div");
    do_speak.classList.add("speak");
    do_speak.style.backgroundColor = "black";
    do_speak.style.left = "30vw";
    do_speak.innerHTML = "Hear this";
    do_speak.addEventListener("click", function() {
        commit_box(true);
        mask.parentElement.removeChild(mask);
        picker.parentElement.removeChild(picker);
        location.reload();
    });
    picker.appendChild(do_speak);

    var no_speak = document.createElement("div");
    no_speak.classList.add("speak");
    no_speak.style.backgroundColor = "black";
    no_speak.innerHTML = "Don't hear this";
    no_speak.style.right = "30vw";
    no_speak.addEventListener("click", function() {
        commit_box(false);
        mask.parentElement.removeChild(mask);
        picker.parentElement.removeChild(picker);
        location.reload();
    });
    picker.appendChild(no_speak);
document.body.appendChild(mask);
document.body.appendChild(picker);
}

function update_box(box, top_x_coord, top_y_coord, e) {
    $(box).css({
        "left": top_x_coord,
        "top": top_y_coord,
        "width": (e.pageX - top_x_coord),
        "height": (e.pageY - top_y_coord)
    });
    $("#canvas").append(box);
}
