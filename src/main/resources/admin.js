(function ($) { // this closure helps us keep our variables to ourselves.
// This pattern is known as an "iife" - immediately invoked function expression

    // form the URL
    var url = AJS.contextPath() + "/rest/passport/1.0/";

    // wait for the DOM (i.e., document "skeleton") to load. This likely isn't necessary for the current case,
    // but may be helpful for AJAX that provides secondary content.
    $(document).ready(function() {
        // request the config information from the server
        function updateConfig() {
            AJS.$.ajax({
                url: baseUrl + "/rest/passport/1.0/",
                type: "PUT",
                contentType: "application/json",
                data: '{ "passportFrontend": "' + AJS.$("#passport-frontend").attr("value") + '", "passportBackend": ' +  AJS.$("#passport-backend").attr("value") + ' }',
                processData: false
            });
        }

        $.ajax({
            url: url,
            dataType: "json"
        }).done(function(config) { // when the configuration is returned...
            // ...populate the form.
            $("#name").val(config.passportFrontend);
            $("#time").val(config.passportBackend);
        });

        function updateConfig() {
            AJS.$.ajax({
                url: baseUrl + "/rest/passport/1.0/",
                type: "PUT",
                contentType: "application/json",
                data: '{ "passportFrontend": "' + AJS.$("#passport-frontend").attr("value") + '", "passportBackend": ' +  AJS.$("#passport-backend").attr("value") + ' }',
                processData: false
            });
        }

        AJS.$("#admin").submit(function(e) {
            e.preventDefault();
            updateConfig();
        });
    });

})(AJS.$ || jQuery);