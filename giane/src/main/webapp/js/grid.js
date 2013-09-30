$(document).ready( function() {
  $.subscribe('rowSelected', function(event, data) {
    $.each($('#' + event.originalEvent.grid[0].id).jqGrid('getRowData', event.originalEvent.id), function(index, value) {
      var span = $('#' + event.originalEvent.grid[0].id + '_selected_' + index + '_span');
      if (span.size() != 0) {
        span.text(value).addClass('filled');
      }

      var input = $('input[id="' + event.originalEvent.grid[0].id + '_selected_' + index + '"]');
      if (input.size() != 0) {
        input.attr('value', value).addClass('filled');
      }

      var select = $('select[id="' + event.originalEvent.grid[0].id + '_selected_' + index + '"]');
      if (select.size() != 0) {
        select.find('option:contains("' + value + '")').attr('selected', 'true').addClass('selectedOption');
      }

      var input = $('textarea[id="' + event.originalEvent.grid[0].id + '_selected_' + index + '"]');
      if (input.size() != 0) {
        input.attr('value', value).addClass('filled');
      }
    });
  });

  $.subscribe('gridCompleted', function(event, data) {
    $('.filled').html('').removeAttr('value').removeClass('filled');
    $('.selectedOption').removeAttr('selected').removeClass('selectedOption');
  });
});
