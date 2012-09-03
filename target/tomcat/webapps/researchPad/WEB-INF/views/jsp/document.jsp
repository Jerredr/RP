<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- <link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/jquery.mobile-1.0.css" />" />
<script type="text/javascript"
	src="<c:url value="/resources/js/jquery-1.6.4.min.js" />"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/jquery.mobile-1.0.min.js" />"></script> -->
<script type="text/javascript">
	/**
	 * Submit notes
	 **/
	function link() {
		$.post("/researchPad/document/link", {
			documentId : '<c:out value="${document.id}"/>'
		}, function(data) {

		});
	}
</script>
<script src="<c:url value="/resources/js/jquery.nicescroll.js" />" type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		$(window).resize(function(){
			$(".view").niceScroll({touchbehavior:true,bouncescroll:false,boxzoom:true,gesturezoom:true,dblclickzoom:true,cursoropacitymax:0.6,cursorwidth:8});
			
			//$("#boxscroll").css("height", screen.availHeight - $("#header2").outerHeight());
			/*$("#boxscroll").css("width", '100%');
			$("#boxscroll").niceScroll({touchbehavior:false,bouncescroll:true,boxzoom:true,cursorcolor:"#0000FF",cursoropacitymax:0.6,cursorwidth:8})*/
		});
	});
	$(document).ready(function() {
		//alert($("#header2").outerHeight());
		//alert(screen.availHeight);
		$(".view").css("height", screen.availHeight + 10);
		$(".view").niceScroll({touchbehavior:true,bouncescroll:false,boxzoom:true,gesturezoom:true,dblclickzoom:true,cursoropacitymax:0.6,cursorwidth:8});
		
		//$("#boxscroll2").css("height", screen.availHeight - $("#header2").outerHeight() + 10);
		//$("#boxscroll2").niceScroll({touchbehavior:false,bouncescroll:true,boxzoom:true,cursorcolor:"#0000FF",cursoropacitymax:0.6,cursorwidth:8});
	});
</script>
<c:choose>
	<c:when test="${found == true}">
		<c:choose>
			<c:when test="${document.type == 0}">
				<h2>Document not found</h2>
			</c:when>
			<c:when test="${document.type == 1}">
				<h2>${document.name}</h2>
				<br>
				<span onclick="link()" data-role="button" data-inline="true"
					class="ui-btn ui-btn-inline ui-btn-corner-all ui-shadow ui-btn-hover-c ui-btn-up-c">Add
					document to your library</span>
				<br>
				<p>${document.content}</p>
			</c:when>
			<c:when test="${document.type == 2}">
				<br>
				This document is only available as PDF: <a href="${document.url}" target="_blank">Open PDF</a>
			</c:when>
		</c:choose>
	</c:when>
	<c:otherwise>
		<h2>Document not found</h2>
	</c:otherwise>
</c:choose>
