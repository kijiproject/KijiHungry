<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page contentType="text/html;charset=UTF-8"%>
<!doctype html>
<html>
  <head>
    <META http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <title>WibiHungry</title>
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
      <c:if test="${fn:length(login) eq 0}">
        <div class="hero-unit food-background">
          <div class="well">
            <h2>Are you hungry yet?</h2>
            <p>
              Browse your favorite foods, discover new dishes, and share
              them with your friends. WibiHungry will recommend content
              for you in real time as you explore.
            </p>
            <div class="pull-right" style="width:50%">
              <div class="btn-group">
                <a class="btn btn-large btn-primary" href="/createuser">Create an account</a>
                &nbsp;
                <a class="btn btn-large btn-success" href="/signin">Sign in</a>
              </div>
            </div>
          </div>
        </div>
      </c:if>

      <c:if test="${fn:length(login) gt 0}">
        <div class="row">
          <div class="span8">
            <div class="page-header">
              <h2>Dishes <small>recommended for you</small></h2>
            </div>
            <c:choose>
              <c:when test="${fn:length(dishRecommendations) gt 0}">
                <div class="row">
                  <c:forEach var="dish" items="${dishRecommendations}">
                    <div class="span4">
                      <div class="well dish" onclick="location = '/dish?id=${dish.id}'"
                           title="View ${fn:toLowerCase(dish.name)} details">
                        <h4>${dish.name}</h4>
                        <div class="thumbnail" style="float:right">
                          <img src="${dish.thumbnail}"/>
                        </div>
                        <p class="description">
                          ${dish.description}
                        </p>
                      </div>
                    </div>
                  </c:forEach>
                </div>
              </c:when>
              <c:otherwise>
                <p>Rate a few more dishes below to start seeing personalization recommendations.</p>
              </c:otherwise>
            </c:choose>
          </div>
          <div class="span4">
            <c:if test="${fn:length(recentRatings) gt 0}">
              <div class="alert alert-info">
                <h3>Your recent ratings</h3>
                <ol>
                  <c:forEach var="rating" items="${recentRatings}">
                    <li>
                      ${rating.dishName} &mdash;
                      <c:choose>
                        <c:when test="${rating.value eq 1}">
                          <span class="yum">Yum!</span>
                        </c:when>
                        <c:when test="${rating.value eq 0}">
                          <span class="meh">Meh.</span>
                        </c:when>
                        <c:when test="${rating.value eq -1}">
                          <span class="blech">Blech!</span>
                        </c:when>
                      </c:choose>
                    </li>
                  </c:forEach>
                </ol>
              </div>
            </c:if>
            <c:if test="${fn:length(favoriteIngredients) gt 0}">
              <div class="alert alert-success">
                <h3>Your favorite ingredients</h3>
                <ol>
                  <c:forEach var="ingredient" items="${favoriteIngredients}">
                    <li>${ingredient.name}</li>
                  </c:forEach>
                </ol>
              </div>
            </c:if>
          </div>
        </div>
      </c:if>

      <div class="page-header">
        <h2>Browse <small>dishes by cuisine</small></h2>
      </div>
      <c:forEach var="cuisine" items="${cuisines}">
        <c:if test="${fn:length(cuisine.dishes) ge 3}">
          <h3 class="cuisine-name">${cuisine.name}</h3>
          <div class="row cuisine" style="clear:both">
            <c:forEach var="dish" items="${cuisine.dishes}">
              <div class="span4">
                <div class="well dish" onclick="location = '/dish?id=${dish.id}'"
                     title="View ${fn:toLowerCase(dish.name)} details">
                  <h4>${dish.name}</h4>
                  <div class="thumbnail" style="float:right"><img src="${dish.thumbnail}"/></div>
                  <p class="description">
                    ${dish.description}
                  </p>
                </div>
              </div>
            </c:forEach>
          </div>
        </c:if>
      </c:forEach>
    </div>
  </body>
</html>
