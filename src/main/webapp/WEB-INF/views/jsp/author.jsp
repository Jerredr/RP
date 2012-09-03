<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html>
<html>
<head>

<script src="<c:url value="/resources/js/jquery.nicescroll.js" />" type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		$(window).resize(function(){
			$(".view").niceScroll({touchbehavior:true,bouncescroll:true,boxzoom:true,gesturezoom:true,dblclickzoom:true,cursoropacitymax:0.6,cursorwidth:8});
			
			//$("#boxscroll").css("height", screen.availHeight - $("#header2").outerHeight());
			/*$("#boxscroll").css("width", '100%');
			$("#boxscroll").niceScroll({touchbehavior:false,bouncescroll:true,boxzoom:true,cursorcolor:"#0000FF",cursoropacitymax:0.6,cursorwidth:8})*/
		});
	});
	$(document).ready(function() {
		//alert($("#header2").outerHeight());
		//alert(screen.availHeight);
		$(".view").css("height", screen.availHeight + 10);
		$(".view").niceScroll({touchbehavior:true,bouncescroll:true,boxzoom:true,gesturezoom:true,dblclickzoom:true,cursoropacitymax:0.6,cursorwidth:8});
		
		//$("#boxscroll2").css("height", screen.availHeight - $("#header2").outerHeight() + 10);
		//$("#boxscroll2").niceScroll({touchbehavior:false,bouncescroll:true,boxzoom:true,cursorcolor:"#0000FF",cursoropacitymax:0.6,cursorwidth:8});
	});
</script>
</head>

<body>
<h2>${author.firstName} ${author.lastName}</h2>
<p>
	<em>${author.department} ${author.university}</em></i><br> <em>${author.address}</em><br>
	<em><a href="mailto:${author.email}" class="ui-link">${author.email}</a></em>
</p>
<h3 class="ui-li-heading">Publications in the ResearchPad repository</h3>
<ul data-role="listview" style="margin-top: -11px; margin-bottom: -3px;" class="ui-listview">
	<c:forEach items="${author.documents}" var="document">
		<li style="margin-left: 15px; margin-right: 15px;" class="ui-li ui-li-static ui-body-c"><a
			class="ref" id="/ref/document/${document.id}"
			data-inline="true">
				<p class="ui-li-aside ui-li-desc">
					<strong>${document.publicationYear}</strong>
				</p>
				<h3>${document.name}</h3>
				<p class="ui-li-desc">
					<strong>
						<c:forEach items="${document.authors}" var="author2">
							${author2.firstName} ${author2.lastName}; 
						</c:forEach>
					</strong>
				</p>

		</a></li>
	</c:forEach>
</ul>
<br><br>
<h3 class="ui-li-heading">Externally found publications</h3>
<ul data-role="listview" style="margin-top: -11px; margin-bottom: -3px;" class="ui-listview">
	<c:forEach items="${foundDocs}" var="document">
		<li style="margin-left: 15px; margin-right: 15px;" class="ui-li ui-li-static ui-body-c"><a
			class="ref" id="/ref/document/${document.id}"
			data-inline="true" onclick="clickFunction(${document.id})">
				<p class="ui-li-aside ui-li-desc">
					<strong>${document.publicationYear}</strong>
				</p>
				<h3>${document.name}</h3>
				<p class="ui-li-desc">
					<strong>
						<c:forEach items="${document.authors}" var="author2">
							${author2.firstName} ${author2.lastName}; 
						</c:forEach>
					</strong>
				</p>

		</a></li>
	</c:forEach>
</ul>
</body>
</html>