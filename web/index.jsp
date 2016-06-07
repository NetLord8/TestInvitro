<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Загрузка данных Инвитро</title>
  <script src="js/jquery.js"></script>
  <link href="js/jquery-ui.css" rel="stylesheet">
</head>
<body>
<form action="upload"
      enctype="multipart/form-data" method="post" id="invForm">
  <p>Загрузите файл </p>
  <p><input type="file" name="excel"></p>
  <p><input type="submit" value="Загрузить"  class="ui-button"></p>
</form>
<div id="result" class="ui-state-highlight ui-corner-all" style="height:600px; overflow:scroll; width:1000px; border:2px ;margin-top: 20px; padding: 0 .7em;"></div>

<script>
  $("#invForm").submit(function(e)
  {

    var formObj = $(this);
    var formURL = formObj.attr("action");
    var formData = new FormData(this);
    $.ajax({
      url: formURL,
      type: 'POST',
      data:  formData,
      mimeType:"multipart/form-data",
      contentType: false,
      cache: false,
      processData:false,
      success: function(data, textStatus, jqXHR)
      {
        $("#result").append(data)
      },
      error: function(jqXHR, textStatus, errorThrown)
      {
      }
    });
    e.preventDefault(); //Prevent Default action.
    e.unbind();
  });
  $("#invForm").submit(); //Submit the form
</script>
</body>
</html>