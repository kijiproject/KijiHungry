<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page contentType="text/html;charset=UTF-8"%>
<!doctype html>
<html>
  <head>
    <META http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <title>WibiHungry - Create an account</title>
    <link rel="stylesheet" type="text/css" href="/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/hungry.css"/>
  </head>
  <body>
    <div class="container">
      <div class="page-header">
        <h1 class="logo-prefix"><a href="/">WibiHungry</a> <small>Create an account</small></h1>
      </div>

      <p>Please fill out the following form to create a WibiHungry account.</p>

      <form method="post" class="form-horizontal">
        <fieldset>
          <legend>
            Account information
          </legend>
          <div class="control-group">
            <label class="control-label" for="name">Name</label>
            <div class="controls">
              <input type="text" class="input" id="name" name="name" value="${name}"/>
              <p class="help-block">This is what we'll call you.</p>
            </div>
          </div>
          <div class="control-group">
            <label class="control-label" for="login">Login</label>
            <div class="controls">
              <input type="text" class="input" id="login" name="login" value="${login}"/>
              <p class="help-block">This is the username you will use to sign in.</p>
            </div>
          </div>
          <div class="control-group">
            <label class="control-label" for="password">Password</label>
            <div class="controls">
              <input type="password" class="input" id="password" name="password"
                     value="${password}"/>
              <p class="help-block">Your password will be stored securely. But because this
                is a sample app, it will be sent in the clear. Don't reuse one of your existing
                passwords.</p>
            </div>
          </div>
          <c:if test="${fn:length(error) gt 0}">
            <div class="alert alert-error">${error}</div>
          </c:if>
          <div class="form-actions">
            <input type="submit" class="btn btn-primary" value="Create account"/>
            <a href="/" class="btn">Cancel</a>
          </div>
        </fieldset>
      </form>
    </div>
  </body>
</html>
