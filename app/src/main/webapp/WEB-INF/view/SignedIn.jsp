<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page contentType="text/html;charset=UTF-8"%>
<!doctype html>
<html>
  <head>
    <META http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <title>WibiHungry - Signed in</title>
    <link rel="stylesheet" type="text/css" href="/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/hungry.css"/>
  </head>
  <body>
    <div class="container">
      <div class="page-header">
        <h1 class="logo-prefix"><a href="/">WibiHungry</a> <small>powered by WibiData</small></h1>
      </div>
      <div class="hero-unit">
        <h1>Signed in</h1>
        <p>Welcome back, ${name}!</p>
        <p>
          <a href="/" class="btn btn-large btn-success">Get Started</a>
        </p>
      </div>
    </div>
  </body>
</html>
