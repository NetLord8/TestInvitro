<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="sql" uri="http://java.sun.com/jstl/sql" %>




<html>
<head>
  <title>Загрузка данных Инвитро</title>
    <script src="js/jquery.js"></script>
  <script src="js/jquery-ui.js"></script>
    <script src="js/jquery.blockUI.js"></script>
    <script src="js/jquery.formstyler.js"></script>
    <link href="js/jquery.formstyler.css" rel="stylesheet" />
  <link href="js/jquery-ui.css" rel="stylesheet">
    <link href="js/jquery-ui.structure.css" rel="stylesheet">
    <link href="js/jquery-ui.theme.css" rel="stylesheet">
    <script>
        $(function() {
            $(document).ajaxStart($.blockUI).ajaxStop($.unblockUI);
            $('input').styler();
            $( "#mc" ).selectmenu();
            $( "#from" ).datepicker({
                showOn: "button",
                buttonImage: "js/images/calendar.gif",
                buttonImageOnly: true,
                buttonText: "Select date",
                defaultDate: "-1m",
                changeMonth: true,
                dateFormat: 'dd.mm.yy',

                onClose: function( selectedDate ) {
                    $( "#to" ).datepicker( "option", "minDate", selectedDate );
                }
            });
            $( "#to" ).datepicker({
                showOn: "button",
                buttonImage: "js/images/calendar.gif",
                buttonImageOnly: true,
                buttonText: "Select date",
                defaultDate: "-1m",
                dateFormat: 'dd.mm.yy',
                changeMonth: true,

                onClose: function( selectedDate ) {
                    $( "#from" ).datepicker( "option", "maxDate", selectedDate );
                }
            });
            $( "#accordion" ).accordion();
            $("#invForm").submit(function(e)
            {

                var formObj = $(this);

                var formData = new FormData(this);
                $.ajax({
                    url: "/uploadinvitro",
                    type: 'POST',
                    data:  formData,
                    mimeType:"multipart/form-data",
                    contentType: false,
                    cache: false,
                    processData:false,
                    success: function(data, textStatus, jqXHR)
                    {
                        $("#result").prepend(data);
                        $("#accordion").accordion( 'option', 'active', 1);

                    },
                    error: function(jqXHR, textStatus, errorThrown)
                    {
                        $("#result").prepend( textStatus);
                    }
                });
                e.preventDefault(); //Prevent Default action.
                e.unbind();
            });
            $("#updForm").submit(function(e)
            {
                $.ajax({
                    url: "/updateinvitro",
                    type: 'POST',
                    data:  $("#updForm").serialize(),
                    cache: false,
                    processData:false,
                    success: function(data, textStatus, jqXHR)
                    {
                        $("#result").prepend(data);
                        $("#accordion").accordion( 'option', 'active', 2);
                    },
                    error: function(jqXHR, textStatus, errorThrown)
                    {
                        $("#result").prepend( jqXHR);
                    }
                });
                e.preventDefault(); //Prevent Default action.
                e.unbind();
            });
            $('#more').click(function(){
                $("#accordion").accordion( 'option', 'active', 0);
            });
        });
    </script>
</head>
<body>
<sql:query var="result" dataSource="jdbc/medtest">
    SELECT keyid,text FROM dep WHERE KEYID IN('1','5000','6000','7000','8000','27000','43','39000','59','63')
</sql:query>



<div id="accordion">
    <h3>Шаг 1</h3>
    <div>
        <form id="invForm">
            <p>Загрузите файл </p>
            <p><input type="file" name="excel" class="ui-button"></p>
            <p><input type="submit" value="Загрузить файл"  class="ui-button"></p>
        </form>
    </div>
    <h3>Шаг 2</h3>
    <div>
        <form id="updForm">
       <p></p> <label for="mc">Медицинский центр</label>
        <select name="mc" id="mc">
            <c:forEach var="row" items="${result.rows}">
                <option value="<c:out value="${row.keyid}"/>"><c:out value="${row.text}"/></option>
            </c:forEach>
        </select></p>
        <p><label for="from">Начальная дата  </label>
        <input type="text" id="from" name="from" class="">
        <label for="to">Конечная дата</label>
        <input type="text" id="to" name="to"></p>
        <p><input type="submit" value="Обработать данные"  class="ui-button"></p>
        </form>
    </div>
    <h3>Шаг 3</h3>
    <div>
        <p>
        <a href="#" id="more">Загрузить следующий файл</a> или <a href="report/frameset?__report=invitro/double_invitro.rptdesign" target="_blank">открыть "Отчет по задвоенным оплатам"</a>
        </p>

    </div>
</div>
<div id="result"  style="height:40%; overflow:scroll; width:100%; border:2px ;margin-top: 20px; padding: 0 .7em;align:'center'"></div>

<script>

  //$("#invForm").submit(); //Submit the form

</script>
</body>
</html>