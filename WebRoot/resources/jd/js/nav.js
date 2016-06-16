//悬浮固定
$.fn.floatNav = function floatNav( options ) {
        var settings = $.extend({
            start:             null,                // 触发浮动高度
            end:             null,                // 浮云结束高度
            fixedClass:     null,        // 浮动状态class
            anchor:         null,                // 导航浮动状态内部链接锚点
            targetEle:         null,                // 页面滑动到目标元素时隐藏浮动导航
            range:            0                   // 浮动消失相应范围
        }, options);

        var that = $(this),
            navHeight = that.height(),
            navWidth = that.width(),
            wrapDiv = $('<div class="sift-tab"/>');

        // 防止抖动
        that.css({
            height: navHeight,
            width: navWidth
        });
        if ( !that.parent().hasClass('sift-tab') ) {
            that.wrap( wrapDiv.css('height', navHeight) );
        }

        $(window).bind('scroll', function() {
            var docTop = $(document).scrollTop(),
                start = settings.start || that.parent('.sift-tab').offset().top,
                targetTop = settings.targetEle ? $(settings.targetEle).offset().top : 10000;
            if ( docTop > start && docTop < ( settings.end || targetTop ) - settings.range ) {
                 that.addClass(settings.fixedClass);
            } else {
                that.removeClass(settings.fixedClass);
            }
        });
        return this;
    }