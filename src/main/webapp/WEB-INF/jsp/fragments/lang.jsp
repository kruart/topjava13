<%@page contentType="text/html" pageEncoding="UTF-8" %>

<li class="nav-item dropdown">
    <a class="dropdown-toggle nav-link my-1 ml-2" data-toggle="dropdown">${pageContext.response.locale}</a>
    <div class="dropdown-menu">
        <a class="dropdown-item" onclick="show('en')">English</a>
        <a class="dropdown-item" onclick="show('ru')">Русский</a>
    </div>
</li>
<script type="text/javascript">
    function show(lang) {
        window.location.href = window.location.href.split('?')[0] + '?lang=' + lang;
    }
</script>