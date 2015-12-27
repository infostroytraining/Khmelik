//Form validation
function validateRegisterForm() {
    $.validator.addMethod(
        'regex',
        function (value, element, regexp) {
            var re = new RegExp(regexp, "g");
            return this.optional(element) || re.test(value);
        },
        "Please check your input."
    );

    //Registration
    $('#registration-form').validate({
        rules: {
            name: {
                required: true,
                minlength: 2,
                regex: "^\\w{2,}$"
            },
            surname: {
                required: true,
                minlength: 2,
                regex: "^\\w{2,}$"
            },
            email: {
                required: true,
                email: true
            },
            registrationPassword: {
                required: true,
                minlength: 8,
                maxlength: 8,
                regex: "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$"
            },
            registrationConfirmed: {
                required: true,
                equalTo: "#registrationPassword"
            },
            image: {
                required: false
            },
            simpleCaptchaAnswer: {
                required: true
            }
        }
    });

    $('#login-form').validate({
        rules: {
            email: {
                required: true,
                email: true
            },
            password: {
                required: true
            }
        }
    });
};

// Adding UI elements after user is logged in.
function addLoggedUserInterfaceElements(jsonUser) {
    $('.btn-signreg').remove();
    $('#bs-example-navbar-collapse-1 li:last')
        .after(
        "<li>" +
        "<a class=\"page-scroll\" href=\"#user\">User</a>" +
        "</li>"
        +
        "<li id=\"logout\">" +
        "<a id=\"btn-logout\" class=\"page-scroll\" href=\"logout\">Logout</a>" +
        "</li>");

    var image;
    if (!jsonUser.hasOwnProperty('image')) {
        image = "resources/img/default.jpg"
    } else {
        image = "../images/" + jsonUser.image;
    }

    $('#user').removeClass('hidden');
    $('#userInfo').append("Name: " + jsonUser.name + " " + jsonUser.surname + ", email: " + jsonUser.email);
    $('#userInfo').after("<img src=\"" + image + "\" class=\"img-responsive\" alt=\"\">");
}

// Adding UI elements after registration error received.
function addFieldExceptionUIElements(jsonErrors) {
    $.each(jsonErrors.fieldExceptions, function (i, item) {
        $('#form-group-' + item.field.toLowerCase() + ' input').removeClass('valid');
        $('#form-group-' + item.field.toLowerCase() + ' input').addClass('error');
        $('#form-group-' + item.field.toLowerCase())
            .append("<span class='help-inline error-span'>" +
            item.message + "</span>");
    });
};

// Refreshing SimpleCaptcha
function refreshCaptcha() {
    $.get('simpleCaptcha.png',
        {},
        function () {
            $('#simpleCaptchaImage').attr("src", "simpleCaptcha.png");
            $('#simpleCaptchaAnswerInput').val('');
        }
    );
};

// Removing UI login error elements
function removeErrors(formElement) {
    formElement.find('.credentials').removeClass("has-error");
    formElement.find('.error').remove();
    formElement.find('.error-span').remove();
};


jQuery(document).ready(function () {

    // LOGGING FORM SUBMISSION WITH AJAX
    $(document).on('submit', '#login-form', function (event) {
        event.preventDefault();
        removeErrors($('#login-form'));

        $.post('login', $('#login-form').serialize())
            .done(function (jsonUser) {
                addLoggedUserInterfaceElements(jsonUser);
                $('#signInModal').modal("hide");
            })
            .fail(function (error) {
                console.log(error);
                $('#signInModal').find('.credentials').addClass("has-error");
                $('#btn-login-submit').before("<span class='help-inline error-span'>" + error.responseText + "</span>");
                $('#form-login-password-input').val('');
                $('#signInModal').modal("show");
            });
    });

    // REGISTRATION FORM SUBMISSION WITH AJAX
    $(document).on('submit', '#registration-form', function (event) {
        event.preventDefault();
        removeErrors($('#registration-form'));

        var formData = new FormData($('#registration-form')[0]);
        $.ajax({
            url: 'registration',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function (jsonUser) {
                addLoggedUserInterfaceElements(jsonUser);
                $('#registerModal').modal("hide");
            },
            error: function (error) {
                console.log(error);
                if (error.responseJSON.hasOwnProperty("fieldExceptions")) {
                    addFieldExceptionUIElements(error.responseJSON);
                } else if (error.responseJSON.hasOwnProperty("captchaApiProvider")) {
                    $('#form-group-' + error.responseJSON.captchaApiProvider)
                        .append("<span class='help-inline error-span'>Bad captcha</span>");
                }
                grecaptcha.reset();
                refreshCaptcha();
                $('#registerModal').find('[type="password"]').val('');
                $('#registerModal').modal("show");
            }
        });
    });

    // PERMANENT FORM VALIDATION
    $().ready(function () {
        validateRegisterForm();
    })
});
