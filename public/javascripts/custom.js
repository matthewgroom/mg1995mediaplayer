function togglePlayStatus() {
    var button = document.getElementById("play-btn");
    var xhr = new XMLHttpRequest();
    if (button.className == "fa fa-pause") {
        button.classList.remove("fa-pause");
        button.classList.add("fa-play");
        xhr.open("POST", "/myscalaapp/play", true);
    } else {
        button.classList.remove("fa-play");
        button.classList.add("fa-pause");
        xhr.open("POST", "/myscalaapp/pause", true);
    }
    xhr.send()
}

function audioPlayer() {
    var currentSong = 0;
    $("#audioPlayer")[0].src = $(".song td a")[0].href;
    $("#audioPlayer")[0].play();
    $(".song td a").click(function (e) {
        e.preventDefault();
        $("#audioPlayer")[0].src = this.href;
        $("#audioPlayer")[0].play();
        $(".song td").removeClass("current-song");
        currentSong = $(this).parent().index();
        $(this).parents().addClass("current-song");
    });

    $("#audioPlayer")[0].addEventListener("ended", function () {
        currentSong++;
        if (currentSong == $(".song td a").length)
            currentSong = 0;
        $(".song td").removeClass("current-song")
        $(".song td:eq(" + currentSong + ")").addClass("current-song");
        $("#audioPlayer")[0].src = $(".song td a")[currentSong].href;
        $("#audioPlayer")[0].play();
    });

    function sleepMode() {
        setTimeout(function () {
            $("body").animate({opacity: 0.20}, 2000, function() { });
            sleepMode();
        }, 30000);
    }

    $("body").mousemove(function(event) {
        $("body").animate({opacity: 1}, 0, function() {});
    });

    sleepMode();
}




// function shuffle(a) {
//   for (let i = a.length - 1; i > 0; i--) {
//     const j = Math.floor(Math.random() * (i + 1));
//     [a[i], a[j]] = [a[j], a[i]];
//   }
//   return a;
// }
// val test = $(".song td a").map(function(v,i) { console.log(i.href); return i.href; }).get();
// let test = $(".song td a").map(function(v,i) { console.log(i.href); return i.href; }).get();