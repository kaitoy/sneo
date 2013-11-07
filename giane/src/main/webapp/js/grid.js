$(document).ready( function() {
  $.subscribe("rowSelected", function(event, data) {
    $.each($("#" + event.originalEvent.grid[0].id).jqGrid("getRowData", event.originalEvent.id), function(index, value) {
      var fieldId = event.originalEvent.grid[0].id;
      fieldId = fieldId.substring(0, fieldId.lastIndexOf("grid"));
      fieldId += index;
      
      var obj = $("*[id='" + fieldId + "']");
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

// formatter
function oneLine(cellvalue, options, rowObject) {
  options.colModel.unformat = function () {
    return cellvalue;
  };
  return cellvalue.replace(/\n|\r/g, " ");
}
