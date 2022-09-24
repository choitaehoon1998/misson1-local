<%@ page import="com.zerobase.misson1.service.WifiService" %>
<%@ page import="com.zerobase.misson1.domain.WifiInfo" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: 82103
  Date: 2022-09-24
  Time: 오후 9:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>와이파이 불러오기</title>
</head>
<body>
<%
    List<WifiInfo> wifiInfoList = WifiService.getWifiAllInfo();
    if (wifiInfoList.size() == 0) {
        int totalCount = WifiService.getInfoFromApi();
        out.write("<h1>" + totalCount + "개의 WIFI 정보를 정상적으로 저장하였습니다.</h1>");
    } else {
        out.write("<h1> 이미 WIFI 정보가 저장되어있습니다.</h1>");
    }
    out.write("<a href='/'>홈으로 돌아가기</a>");
%>
</body>
</html>
