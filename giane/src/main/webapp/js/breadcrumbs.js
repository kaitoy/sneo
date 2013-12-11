$(document).ready( function() {
  $.subscribe("configMainPaneCompleted", function(event, data) {
    $(".next-breadcrumb")
      .html("")
      .removeClass("next-breadcrumb")
      .addClass("last-breadcrumb")
      .append($(".breadcrumb-label").children());
    $("#breadcrumbs").append('<li class="next-breadcrumb" />');
  });
  
  $.subscribe("configMainPaneGoingForward_before", function(event, data) {
    $(".last-breadcrumb").addClass("update-needed");
  });
  
  $.subscribe("configMainPaneGoingForward_after", function(event, data) {
    var last = $(".last-breadcrumb.update-needed");
    if (last) {
      last.removeClass("last-breadcrumb update-needed").html("")
        .append($(".breadcrumb-link").children());
    }
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
