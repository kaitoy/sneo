$(document).ready( function() {
  $.subscribe("mainPaneCompleted", function(event, data) {
    var root = $(data).parents(".workspace-root");
    var label = root.children(".giane-main").children(".breadcrumb-label").children();
    root.children(".breadcrumbs").children(".next-breadcrumb")
      .html("")
      .removeClass("next-breadcrumb")
      .addClass("last-breadcrumb")
      .append(label);
    root.children(".breadcrumbs")
      .append('<li class="next-breadcrumb" />');
  });
  
  $.subscribe("mainPaneGoingForward_before", function(event, data) {
    $(data).parents(".workspace-root").children(".breadcrumbs").children(".last-breadcrumb")
      .addClass("update-needed");
  });
  
  $.subscribe("mainPaneGoingForward_after", function(event, data) {
    var root = $(data).parents(".workspace-root");
    var link = root.children(".giane-main").children(".breadcrumb-link").children();
    root.children(".breadcrumbs").children(".last-breadcrumb.update-needed")
      .removeClass("last-breadcrumb update-needed").html("")
      .append(link);
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
