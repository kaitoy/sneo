$(document).ready( function() {
  $.subscribe("rowSelected", function(event, data) {
    $.each($("#" + event.originalEvent.grid[0].id).jqGrid("getRowData", event.originalEvent.id), function(index, value) {
      var id = event.originalEvent.grid[0].id;
      id = id.substring(0, id.lastIndexOf("grid"));
      id += index;
      
      var obj = $("*[id='" + id + "']");
      if (obj.size() != 0) {
        var tagName = obj.get(0).tagName.toLowerCase();
        switch (tagName) {
          case "input":
            obj.attr("value", value).addClass("filled");
            break;
          case "select":
            obj.find("option:contains('" + value + "')").attr("selected", "true").addClass("selectedOption");
            break;
          case "textarea":
            obj.attr("value", value).addClass("filled");
            break;
          default:
            break;
        }
      }
    });
  });

  $.subscribe("gridCompleted", function(event, data) {
    $(".filled").html("").removeAttr("value").removeClass("filled");
    $(".selectedOption").removeAttr("selected").removeClass("selectedOption");
  });
});
