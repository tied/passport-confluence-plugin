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
                   data: '{ "passportBackend": "' + AJS.$("#passport-backend").attr("value") +
                   '", "passportFrontend ' +  AJS.$("#passport-frontend").attr("value") +
                   '", "clientId ' +  AJS.$("#client-id").attr("value") +
                   '", "clientSecret ' +  AJS.$("#client-secret").attr("value") +
                   '", "apiKey ' +  AJS.$("#api-key").attr("value") + ' }',
                   processData: false
                 });
    }

    AJS.$("#admin").submit(function(e) {
      e.preventDefault();
      updateConfig();
    });

    $.ajax({
             url: url,
             dataType: "json"
           }).done(function(config) { // when the configuration is returned...
      // ...populate the form.
      $("#passport-backend").val(config.passportBackend);
      $("#passport-frontend").val(config.passportFrontend);
      $("#client-id").val(config.clientId);
      $("#client-secret").val(config.clientSecret);
      $("#api-key").val(config.apiKey);

    });
  });

})(AJS.$ || jQuery);