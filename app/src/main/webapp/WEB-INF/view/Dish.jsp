<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page contentType="text/html;charset=UTF-8"%>
<!doctype html>
<html>
  <head>
    <META http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <title>WibiHungry - ${dish.name}</title>
    <link rel="stylesheet" type="text/css" href="/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/hungry.css"/>
  </head>
  <body>
    <div class="container">
      <div class="page-header">
        <div class="account-info">
          <c:if test="${fn:length(login) gt 0}">
            <b>${login}</b> | <a href="/signout">Sign out</a>
          </c:if>
        </div>
        <h1 class="logo-prefix"><a href="/">WibiHungry</a> <small>powered by WibiData</small></h1>
      </div>
      <div class="hero-unit dish-large" style="background-image: url(${dish.thumbnail})">
        <div class="well">
          <h1>${dish.name} <small>${dish.cuisine}</small></h1>
          <img src="${dish.thumbnail}" class="thumbnail pull-left"/>
          <c:if test="${fn:length(dish.ingredients) gt 0}">
            <div class="alert alert-success pull-right ingredients">
              <h4>Ingredients</h4>
              <ul>
                <c:forEach var="ingredient" items="${dish.ingredients}">
                  <li>${ingredient.name}</li>
                </c:forEach>
              </ul>
            </div>
          </c:if>
          <p>
            ${dish.description}
          </p>
          <c:if test="${fn:length(login) gt 0}">
            <p class="btn-group">
              <a class="btn btn-large
                        <c:if test="${empty currentRating || currentRating eq 1}">
                          btn-success
                        </c:if>
                        " href="/dish?id=${dish.id}&rate=1">Yum :)</a>
              <a class="btn btn-large
                        <c:if test="${empty currentRating || currentRating eq 0}">
                          btn-warning
                        </c:if>
                        " href="/dish?id=${dish.id}&rate=0">Meh :|</a>
              <a class="btn btn-large
                        <c:if test="${empty currentRating || currentRating eq -1}">
                          btn-danger
                        </c:if>
                        " href="/dish?id=${dish.id}&rate=-1">Blech :(</a>
            </p>
          </c:if>
        </div>
      </div>

      <div class="well">
        <a href="/">Home</a>
      </div>
    </div>
  </body>
</html>
