var ajaxUrl = "ajax/meals/";
var datatableApi;

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    });
    makeEditable();

    $('#dateTime').datetimepicker({
        format: 'Y-m-d H:i'
    });

    $('#startDate').datetimepicker({
        timepicker: false,
        format: 'Y-m-d'
    });

    $('#endDate').datetimepicker({
        timepicker: false,
        format: 'Y-m-d'
    });

    $('#startTime').datetimepicker({
        datepicker: false,
        format: 'H:i'
    });

    $('#endTime').datetimepicker({
        datepicker: false,
        format: 'H:i'
    });
});

function filter() {
    var form = $("#filterForm");

    $.ajax({
        type: "POST",
        url: ajaxUrl + 'filter',
        data: form.serialize(),
        success: function (data) {
            datatableApi.clear().rows.add(data).draw();
            successNoty("Filtered");
        }
    });
}