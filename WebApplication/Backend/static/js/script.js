$(document).ready(function () {
    $('.closeSubItem').on('click', function () {
        var factory_id = $(this).attr('factory_id');
        var accident_id = $(this).attr('accident_id');
        $(".chemSubItemBody").each(function () {
            var factory_sub_id = $(this).attr('factory_id');
            var accident_sub_id = $(this).attr('accident_id');
            if (accident_id == accident_sub_id && accident_sub_id != null) {
                $(this).slideToggle("slow");
            }
            if (factory_id == factory_sub_id && factory_sub_id != null) {
                $(this).slideToggle("slow");
            }
        })
    })

    var activeMenu = $('#homepage').attr('page');
    $('.menu > div').each(function () {
        if (activeMenu == $(this).attr('page')) {
            $(this).addClass('active');
        }
    })

    $('.reccomendAccidentChemical > ul > li').each(function(){
        $(this).on('click', function(){
            value = $(this).text();
            factory_id = $(this).parent().parent().attr("factory_id");
            $('.accidentChemical').each(function(){
                if(factory_id == $(this).attr('factory_id')){
                    $(this).val(function(){
                        return this.value + value +", ";
                    });
                }
            })
        })
    })
    console.log(('.item').length)
    if($('.item').length == 0){
        $('.noneResult').css({"display":"block"})
    }else{
        $('.noneResult').css({"display":"none"})
    }

})