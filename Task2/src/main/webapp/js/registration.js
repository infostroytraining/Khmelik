function refreshCaptcha(){
    $.get('simpleCaptcha.png',
        {},
        function(){
            $('#simpleCaptchaImage').attr("src", "simpleCaptcha.png")
        }
    );
}