<%@ taglib prefix="c" 		 uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	 uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html> 
<html> 
<head> 
	<title>ResearchPad</title> 
	<meta name="viewport" content="width=device-width, minimum-scale=1, maximum-scale=1">
	<link rel="stylesheet" href="<c:url value="/resources/css/jquery.mobile-1.0.css" />" />
	<link rel="stylesheet" href="<c:url value="/resources/css/jquery.mobile.splitview.css" />" />
	<link rel="stylesheet" href="<c:url value="/resources/css/jquery.mobile.grids.collapsible.css" />" />
	<link rel="stylesheet"  href="<c:url value="/resources/css/jquery.mobile.scrollview.css" />" />
	<script type="text/javascript" src="<c:url value="/resources/js/jquery-1.6.4.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/jquery.mobile.splitview.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/jquery.mobile.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/jquery.easing.1.3.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/jquery.mobile.scrollview.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/scrollview.js"/>"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/jRating.jquery.css" />" />
	<script type="text/javascript" src="<c:url value="/resources/js/jRating.jquery.js" />"></script>
	<script type="text/javascript">
	$('div[id="authorList"] ul[data-role="listview"] a').live("click", function() {
	    var dataurl = $(this).attr("data-url");
	    alert(dataurl);
	    if (dataurl != null)
	        $.mobile.changePage("articles.htm" + dataurl);
	});	
	$('.yearUrl').live("click", function() {  
        var dataurl = $(this).attr("data-url");  
        alert(dataurl);
        if (dataurl != null)  
            $.mobile.loadPage("/library/year/" + dataurl,{
                pageContainer:$("#recentreferences"),
                data:dataurl, 
                transition: "slideup"
            }); 
    }); 
	
	$(".basic").jRating({
		 step: true,
		 phpPath : '/researchPad/document/submitrating',
		 bigStarsPath : '/researchPad/resources/icons/stars.png',
		 showRateInfo: false,
		 isDisabled: true
	 });
	</script>
</head> 

 <body> 
    <div data-role="panel" data-id="menu" data-hash="crumbs" data-context="a#default">
      <!-- Start of first page -->
      <div data-role="page" id="main" data-hash="false">

      	<div data-role="header">
      		<h1>Menu</h1>
      	</div><!-- /header -->

      	<div data-role="content">	
      		<ul data-role="listview" data-inset="true">
      		<li data-icon="search"><a href="${pageContext.request.contextPath}/library/search/" data-panel="main" rel="external">Search</a></li>
			<li><a href="${pageContext.request.contextPath}/library/all/" data-panel="main">All references</a></li>
			<li><a href="${pageContext.request.contextPath}/library/recent/" data-panel="main">Recent references</a></li>
            <li>By author
              <ul>
              	<c:forEach items="${authors}" var="author">
              		<li><a href="${pageContext.request.contextPath}/library/author/${author.id}" data-panel="main">${author.firstName}  ${author.lastName}</a></li> 
				</c:forEach>
              </ul>
            </li>
            <li>By publication year
              <ul>
				<c:forEach items="${years}" var="year">
					<!-- <li><a class="yearUrl" href="#year${year}" data-url='${year}' data-panel="main">${year}</a></li> -->
					<li><a href="${pageContext.request.contextPath}/library/year/${year}" data-panel="main">${year}</a></li>
				</c:forEach>
              </ul>
            </li>
            <!-- 
			<li>By rating
              <ul>
                  <li><a href="#5stars" data-panel="main">5 stars</a></li>
                  <li><a href="#4stars" data-panel="main">4 stars</a></li>
                  <li><a href="#3stars" data-panel="main">3 stars</a></li>
                  <li><a href="#2stars" data-panel="main">2 stars</a></li>
                  <li><a href="#1star" data-panel="main">1 star</a></li>
              </ul>
            </li>
          </ul>-->
      	</div><!-- /content -->
      </div><!-- /page -->

    </div><!-- panel menu -->

    <div data-role="panel" data-id="main">
      <!-- Start of 6th page -->
      <div data-role="page" id="allreferences">

        <div data-role="header">
          <h1>ResearchPad</h1>
        </div><!-- /header -->

        <div data-role="content"> 
			<ul data-role="listview">
				<c:forEach items="${documents}" var="document">
					<li><a href="${pageContext.request.contextPath}/document/view/${document.document.id}" rel="external">
						<h3>${document.document.name}</h3>
						<p><strong>
						<c:forEach items="${document.document.authors}" var="author">
							${author.firstName} ${author.lastName}; 
						</c:forEach>
						</strong></p>
						<!-- <p>${document.document.introduction}</p> -->
						<p class="ui-li-aside"><div class="basic" id="rating_${document.rating}_${document.id}"></div></p>
				</a></li>	
				</c:forEach>
			</ul>
		</div><!-- /content -->
		
      </div><!-- /page -->
      
      <div data-role="page" id="bar">

        <div data-role="header" data-backbtn="false">
          <h1>Bar</h1>
        </div><!-- /header -->

        <div data-role="content">
        	<form action="#" method="get">
        		<div data-role="fieldcontain">
                   <label for="search">Search Input:</label>
                   <input type="search" name="password" id="search" value=""  />
              	</div>
        	</form>
        </div>
    </div>

    </div><!-- panel main -->


  </body>
</html>
