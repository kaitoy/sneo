$(document).ready( function() {
  $.subscribe("configMainPaneCompleted", function(event, data) {
    $(".next-breadcrumb")
      .html("")
      .removeClass("next-breadcrumb")
      .addClass("last-breadcrumb")
      .append($(".breadcrumb-label").children());
    $("#breadcrumbs").append('<li class="next-breadcrumb" />');
  });
  
  $.subscribe("configMainPaneGoingForward", function(event, data) {
    $(".last-breadcrumb")
      .removeClass("last-breadcrumb")
      .html("")
      .append($(".breadcrumb-link").children());
  });
  
  $.subscribe("configMainPaneGoingBack", function(event, data) {
    $("#" + event.originalEvent.id)
      .removeAttr("id")
      .parent()
        .addClass("next-breadcrumb")
        .nextAll()
          .remove();
  });
});
