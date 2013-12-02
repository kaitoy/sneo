$(document).ready( function() {
  $.subscribe("startingMainPane", function(event, data) {
    $("#breadcrumbs")
      .html("")
      .append('<li class="next-breadcrumb" />');
  });
  
  $.subscribe("mainPaneCompleted", function(event, data) {
    $(".next-breadcrumb")
      .html("")
      .removeClass("next-breadcrumb")
      .addClass("last-breadcrumb")
      .append($(".breadcrumb-label").children());
    $("#breadcrumbs").append('<li class="next-breadcrumb" />');
  });
  
  $.subscribe("mainPaneGoingForward", function(event, data) {
    $(".last-breadcrumb")
      .removeClass("last-breadcrumb")
      .html("")
      .append($(".breadcrumb-link").children());
  });
  
  $.subscribe("mainPaneGoingBack", function(event, data) {
    $("#" + event.originalEvent.id)
      .removeAttr("id")
      .parent()
        .addClass("next-breadcrumb")
        .nextAll()
          .remove();
  });
});
