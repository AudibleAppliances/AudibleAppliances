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

var allowed_colours = ["orange", "blue", "purple"]

function mouse_down(e) {
    var start_x = e.pageX;
    var start_y = e.pageY;
    var box = document.createElement("div");
    box.className = "square";
    box.style.borderColor = allowed_colours[Math.floor(Math.random() * allowed_colours.length)];
    var resize_box = function(e) {
        update_box(box, start_x, start_y, e)
    };
    var commit_box = function() {
        var type = "speed";
        var arg = JSON.stringify({
            type: type,
            corner: [
                (($(box).position().left - $("#canvas").position().left) / $("#canvas").width()),
                (($(box).position().top - $("#canvas").position().top) / $("#canvas").height())
            ],
            width: $(box).width() / $("#canvas").width(),
            height: $(box).height() / $("#canvas").height()
        });
        $.ajax({
            type: "POST",
            url: "/new_box",
            data: arg,
            dataType: "application/json"
        });
    }
    document.getElementById('canvas').addEventListener("mousemove", resize_box);
    var mouse_up_and_clean_up = function() {
        document.getElementById('canvas').removeEventListener("mousemove", resize_box);
        document.getElementById('canvas').removeEventListener("mouseup", mouse_up_and_clean_up);
        commit_box();
    }
    document.getElementById('canvas').addEventListener("mouseup", mouse_up_and_clean_up);
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
