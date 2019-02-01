$(document).ready(function () {
    $('.closeSubItem').on('click', function () {
        var factory_id = $(this).attr('factory_id');
        var accident_id = $(this).attr('accident_id');
        $(".chemSubItemBody").each(function () {
            var factory_sub_id = $(this).attr('factory_id');
            var accident_sub_id = $(this).attr('accident_id');
            console.log(factory_sub_id)
            if (accident_id == accident_sub_id && accident_sub_id != null) {
                $(this).slideToggle("slow");
            }
            if (factory_id == factory_sub_id && factory_sub_id != null) {
                $(this).slideToggle("slow");
            }
        })
    })

    var activeMenu = $('body').attr('page');
    $('.menu > div').each(function () {
        if (activeMenu == $(this).attr('page')) {
            $(this).addClass('active');
        }
    })

})