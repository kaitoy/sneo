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
            if (obj.attr("type").toLowerCase() === "checkbox") {
              if (value === "true") {
                obj.val([obj.attr("value")]);
              }
              else {
                obj.val([]);
              }
              obj.addClass("checked");
            }
            else {
              obj.val(value).addClass("filled");
            }
            break;
          case "select":
            if (!value) {
              obj.children("option").removeAttr("selected");
            }
            else {
              obj.find("option:contains('" + value + "')")
                .attr("selected", "true").addClass("selectedOption");
            }
            break;
          case "textarea":
            obj.val(value).addClass("filled");
            break;
          default:
            break;
        }
      }
    });
  });

  $.subscribe("gridCompleted", function(event, data) {
    $(".filled").val("").removeClass("filled");
    $(".checked").val([]).removeClass("checked");
    $(".selectedOption").removeAttr("selected").removeClass("selectedOption");
  });
});

// formatter
function oneLine(cellvalue, options, rowObject) {
  if (!options.colModel.unformat) {
    options.colModel.unformat = function(c, o, r) {
      var clone = $(r).clone();
      clone.children("span").text("\n");
      return clone.text();
    };
  }
  
  return cellvalue.replace(/\n/g, "<span/>");
}

