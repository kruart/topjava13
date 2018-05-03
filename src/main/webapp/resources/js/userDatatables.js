var ajaxUrl = "ajax/admin/users/";
var datatableApi;

function isEnabled($obj) {
    var isEnabled = $obj.is(':checked');
    var id = $obj.closest('tr').attr('id');

    $obj.closest('tr').css('color', isEnabled ? 'black' : 'red');

    $.ajax({
        url: ajaxUrl + id,
        type: 'POST',
        data: 'enabled=' + isEnabled,
        success: function () {
            successNoty(isEnabled ? 'Enabled' : 'Disabled');
        }
    })
}

function check() {
    $(':checkbox').each(function () {
        $(this).closest('tr').css('color', $(this).is(":checked") ? 'black' : 'red');
    })
}

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
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
    check();
});