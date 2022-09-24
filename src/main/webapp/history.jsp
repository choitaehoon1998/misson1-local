<%@ page import="com.zerobase.misson1.service.LocationHistoryService" %>
<%@ page import="com.zerobase.misson1.domain.LocationHistory" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: 82103
  Date: 2022-09-24
  Time: 오후 9:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<script>
    const deleteHistory = (seq) => {
        location.href = "/history.jsp?seq=" + seq;
    }
</script>
<head>
    <title>히스토리 조회</title>
</head>
<body>
<h1>위치 히스토리 목록</h1>
<br>
<a href="/">홈</a> |
<a href="history.jsp">위치 히스토리 목록</a> |
<a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a>
<br>
<table style="width: 100%; ">
    <tr style=" color: white; background-color: forestgreen; height: 50px;
               text-align:center;">
        <td>ID</td>
        <td>X좌표</td>
        <td>Y좌표</td>
        <td>조회일자</td>
        <td>비고</td>
    </tr>
    <%
        String seq = request.getParameter("seq");
        if (seq != null && !"".equals(seq)) {
            LocationHistoryService.deleteLocationHistory(Integer.parseInt(seq));
        }

        List<LocationHistory> locationHistoryList = LocationHistoryService.findUseHistory();
        for (LocationHistory locationHistory : locationHistoryList) {
            out.write("<tr style='text-align:center'>");
            out.write("<td>" + locationHistory.SEQ + "</td>");
            out.write("<td>" + locationHistory.LAT + "</td>");
            out.write("<td>" + locationHistory.LNT + "</td>");
            out.write("<td>" + locationHistory.CREATED_AT + "</td>");
            out.write("<td><button onclick='deleteHistory(" + locationHistory.SEQ + ")'>삭제</button></td>");
            out.write("</tr>");

        }
    %>
</table>

</body>
</html>
