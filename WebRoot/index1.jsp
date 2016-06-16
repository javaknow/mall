<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index1.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script>
		function abbreviate(value,length,suffix){
			if(value!=null){
				var result="";
				var length1 = value.length;
				if(length>=length1){
					result = value;
				}else{
					result = value.substring(0,length);
					if(suffix!=null){
						result = result+suffix;
					}
				}
				
				return result;
			}
		}
		
		alert(abbreviate("我是中文可以正常截取吗？",3,"..."));
		alert(abbreviate("我是中文可以正常截取吗？",3));
		alert(abbreviate("我是中文可以正常截取吗？"));
	</script>
  </head>
  
  <body>
    This is my JSP page. <br>
  </body>
</html>
