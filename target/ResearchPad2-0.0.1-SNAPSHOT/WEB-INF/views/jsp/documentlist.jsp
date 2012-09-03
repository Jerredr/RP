<%@ taglib prefix="c" 		 uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	 uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html> 
<html> 
<head> 
	<title>ResearchPad</title> 
	<meta name="viewport" content="width=device-width, minimum-scale=1, maximum-scale=1">
	<link rel="stylesheet" href="<c:url value="/resources/css/jquery.mobile-1.0.css" />" />
	<link rel="stylesheet" href="<c:url value="/resources/css/jquery.mobile.grids.collapsible.css" />" />
	<link rel="stylesheet"  href="<c:url value="/resources/css/jquery.mobile.scrollview.css" />" />
	<script type="text/javascript" src="<c:url value="/resources/js/jquery-1.6.4.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/jquery.mobile.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/jquery.easing.1.3.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/jquery.mobile.scrollview.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/scrollview.js"/>"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/jRating.jquery.css" />" />
	<script type="text/javascript" src="<c:url value="/resources/js/jRating.jquery.js" />"></script>
	<script type="text/javascript">
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

    </div><!-- panel main -->


  </body>
</html>
