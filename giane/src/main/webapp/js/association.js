$(document).ready(function() {
  $.subscribe("association", function(event, data) {
    var gridId = event.originalEvent.delegateTarget.id.slice("remove_from_".length);
    var otherGridId;
    var associateButtonClicked;

    if (gridId.indexOf("unassociated") != -1) {
      associateButtonClicked = true;
      otherGridId = gridId.substring(0, gridId.indexOf("unassociated")) + "associated";
      otherGridId = otherGridId + gridId.slice(otherGridId.length + 2);
    }
    else {
      associateButtonClicked = false;
      otherGridId = gridId.substring(0, gridId.indexOf("associated")) + "unassociated";
      otherGridId = otherGridId + gridId.slice(otherGridId.length - 2);
    }

    var grid = $("#" + gridId);
    var otherGrid = $("#" + otherGridId);

    var data = new Array();
    var delRowNums = new Array();
    $.each(grid.jqGrid("getGridParam", "selarrrow"), function(index, value) {
      data.push(grid.jqGrid("getRowData", value));
      delRowNums.push(value);
    });

    otherGrid.jqGrid("addRowData", "id", data, "last");

    for (var i = 0; i < delRowNums.length; i ++) {
      grid.jqGrid("delRowData", delRowNums[i]);
    }

    var associatedGridId;
    var associatedGrid;
    if (associateButtonClicked) {
      associatedGridId = otherGridId;
      associatedGrid = otherGrid;
    }
    else {
      associatedGridId = gridId;
      associatedGrid = grid;
    }

    var associatedGridRowIds = new Array();
    $.each(associatedGrid.jqGrid("getDataIDs"), function(index, value) {
      associatedGridRowIds.push(associatedGrid.jqGrid("getRowData", value)["id"]);
    });

    $("#" + associatedGridId + "_id_list").attr("value", associatedGridRowIds.join(","));
  });
});

