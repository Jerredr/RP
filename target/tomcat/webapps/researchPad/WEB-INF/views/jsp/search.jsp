<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<title>ResearchPad</title>
<meta name="viewport"
	content="width=device-width, minimum-scale=1, maximum-scale=1">
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/jquery.mobile-1.0.min.css" />" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/style.css"/>" />
<script type="text/javascript"
	src="<c:url value="/resources/js/jquery-1.7.1.min.js" />"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/jquery.mobile-1.0.min.js" />"></script>

<script type="text/javascript">
$(document).ready(function() {
	//cache all list-items
	var $listItems = $('#my-listview').children();

	//loop through each list-item
	$
			.each(
					$listItems,
					function(index, element) {

						//cache this specific list-item for later use (in the callback)
						var $listItem = $listItems.eq(index);
						$
								.ajax({
									url : '/researchPad/library/gettype',
									data : {
										itemId : $(this).attr('data-id')
									},
									type : 'post',
									success : function(response) {
										//if the server returns valid HTML, we can just append it to the list-item
										$listItem.find('img').replaceWith("<img src='../../resources/icons/status" + response + ".png' width='20px' height='20px' border='0' />");
									},
									error : function(jqXHR, textStatus,
											errorThrown) {
										/*Don't forget to handle errors*/
										$listItem
												.find('img').replaceWith('<span>An error occured, please try again later.</span>');
									}
								});
					});
});
</script>

</head>

<body>
	<div data-role="page" id="search">

		<div data-role="header">
			<h1>ResearchPad</h1>
			<a href="${pageContext.request.contextPath}/library/home"
				data-icon="grid" class="ui-btn-right ui-state-persist"
				rel="external">Library</a>
		</div>
		<!-- /header -->

		<div data-role="content">
			<form action="" method="post" data-ajax="false">
				<div data-role="fieldcontain">
					<label for="searchTitle">Search by Title:</label> <input
						type="search" name="title" id="searchTitle" value="" />
				</div>
			</form>
			<c:if test="${found == true}">
				<ul data-role="listview" id="my-listview">
					<c:forEach items="${results}" var="doc">
						<li data-id="${doc.doi}"><a
							href="${pageContext.request.contextPath}/document/doi/${doc.doi}"
							rel="external">
								<h3>${doc.name}</h3>
								<p>
									<strong> <c:forEach items="${doc.authors}"
											var="author">
													${author.firstName} ${author.lastName}; 
													</c:forEach>
									</strong><img src="<c:url value="/resources/icons/ajax-loader.gif" />" />
								</p>
								<p class="ui-li-aside">
									<strong>${doc.publicationYear}</strong>
								</p>

						</a></li>
					</c:forEach>
				</ul>
			</c:if>
		</div>
		<!-- /content -->

	</div>
	<!-- /page -->
</body>
</html>