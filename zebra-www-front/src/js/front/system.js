$(document).ready(function() {
    var wHeight = window.innerHeight;
    $('#firstPage').height(wHeight);
    $('#imgHolder').lightSlider({
        loop:false,
        keyPress:true,
        slideMove: 3,
        item:3,
        slideMargin: 0,
        controls: false,
        galleryMargin: 34,
        onSliderLoad: function() {
            $('#image-gallery').removeClass('cS-hidden');
        }  
    });
    $('#books').lightSlider({
        loop:false,
        keyPress:true,
        slideMove: 3,
        item:3,
        controls: false,
        slideMargin: 46,
        galleryMargin: 42,
        onSliderLoad: function() {
            $('#image-gallery').removeClass('cS-hidden');
        }  
    });
    if ($('#tab').length > 0) {
        var height = $('.tab-holder li.active').height();
        $('#tab').height(height);
        $(window).on('resize', function (e) {
            var height = $('.tab-holder li.active').height();
            $('#tab').height(height);
        });
        $('.tab-title', '#tab').find('a').on('click', function (e) {
            $('.tab-title').find('.active').removeClass('active');
            $(this).addClass('active');
            var index = parseInt($(this).attr('data-index'), 10);
            $('.active', '.tab-holder').fadeOut();
            setTimeout(function () {
               $('li', '.tab-holder').eq(index).fadeIn().css("display","inline-block");
               $('li', '.tab-holder').eq(index).addClass('active');
               var newheight = $('li', '.tab-holder').eq(index).height();
                $('#tab').height(height);
            }, 400);
        });
    }
});