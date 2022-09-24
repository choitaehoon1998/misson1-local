<%@ page import="com.zerobase.misson1.service.WifiService" %>
<%@ page import="com.zerobase.misson1.domain.WifiInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="com.zerobase.misson1.service.LocationHistoryService" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<script>
    const callPos = () => {
        navigator.geolocation.getCurrentPosition(
            function (position) {
                document.getElementById('LAT').value = position.coords.latitude;
                document.getElementById('LNT').value = position.coords.longitude;
            }
        )
    }
    const getWifiInfo = () => {
        const LAT = document.getElementById("LAT").value;
        const LNT = document.getElementById("LNT").value;
        if (LAT === "" || LNT === "") {
            alert("위치정보를 입력해주세요");
            return false;
        } else {
            location.href = "/?LAT=" + LAT + "&LNT=" + LNT;
        }
    }
</script>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "와이파이 정보 구하기" %>
</h1>
<a href="/">홈</a> |
<a href="history.jsp">위치 히스토리 목록</a> |
<a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a>
<br>
LAT: <input type="text" id="LAT"> , LNT: <input type="text" id="LNT">
<button onclick="callPos()"> 내 위치 가져오기</button>
<button onclick="getWifiInfo()"> 근처 WIFI 정보 가져오기</button>
<br>

<table style="width: 100%; ">
    <tr style=" color: white; background-color: forestgreen; height: 50px;
               text-align:center;">
        <td>거리(KM)</td>
        <td>관리번호</td>
        <td>자치구</td>
        <td>와이파이명</td>
        <td>도로명주소</td>
        <td>상세주소</td>
        <td>설치위치(층)</td>
        <td>설치유형</td>
        <td>설치기관</td>
        <td>서비스구분</td>
        <td>망종류</td>
        <td>설치년도</td>
        <td>실내외구분</td>
        <td>WIFI접속환경</td>
        <td>X좌표</td>
        <td>Y좌표</td>
        <td>작업일자</td>
    </tr>
    <%
        String LAT = request.getParameter("LAT");
        String LNT = request.getParameter("LNT");
        boolean hasLat;
        boolean hasLnt;
        hasLat = LAT != null && !"".equals(LAT);
        hasLnt = LNT != null && !"".equals(LNT);

        if (hasLat && hasLnt) {
            List<WifiInfo> wifiInfoList = WifiService.getWifiAllInfo();
            if (wifiInfoList.size() == 0) {
                out.write("<tr>");
                out.write("<td colspan='17' style='text-align: center; font-size: 25px;'>");
                out.write("현재 저장된 와이파이 목록이 없습니다.");
                out.write("</td>");
                out.write("</tr>");
            } else {
                List<WifiInfo> nearWifiList = WifiService.getNearWifi(Double.parseDouble(LAT), Double.parseDouble(LNT));
                if (nearWifiList.size() == 0) {
                    out.write("<tr>");
                    out.write("<td colspan='17' style='text-align: center; font-size: 25px;'>");
                    out.write("500M 이내에 와이파이가 없습니다.");
                    out.write("</td>");
                    out.write("</tr>");
                } else {
                    LocationHistoryService.newLocationHistory(Double.parseDouble(LAT), Double.parseDouble(LNT));
                    DecimalFormat df = new DecimalFormat("#.0000");
                    for (WifiInfo wifiInfo : nearWifiList) {
                        out.write("<tr>");
                        out.write("<td>" + df.format(wifiInfo.DISTANCE) + "</td>");
                        out.write("<td>" + wifiInfo.X_SWIFI_MGR_NO + "</td>");
                        out.write("<td>" + wifiInfo.X_SWIFI_WRDOFC + "</td>");
                        out.write("<td>" + wifiInfo.X_SWIFI_MAIN_NM + "</td>");
                        out.write("<td>" + wifiInfo.X_SWIFI_ADRES1 + "</td>");
                        out.write("<td>" + wifiInfo.X_SWIFI_ADRES2 + "</td>");
                        out.write("<td>" + wifiInfo.X_SWIFI_INSTL_FLOOR + "</td>");
                        out.write("<td>" + wifiInfo.X_SWIFI_INSTL_TY + "</td>");
                        out.write("<td>" + wifiInfo.X_SWIFI_INSTL_MBY + "</td>");
                        out.write("<td>" + wifiInfo.X_SWIFI_SVC_SE + "</td>");
                        out.write("<td>" + wifiInfo.X_SWIFI_CMCWR + "</td>");
                        out.write("<td>" + wifiInfo.X_SWIFI_CNSTC_YEAR + "</td>");
                        out.write("<td>" + wifiInfo.X_SWIFI_INOUT_DOOR + "</td>");
                        out.write("<td>" + wifiInfo.X_SWIFI_REMARS3 + "</td>");
                        out.write("<td>" + wifiInfo.LAT + "</td>");
                        out.write("<td>" + wifiInfo.LNT + "</td>");
                        out.write("<td>" + wifiInfo.WORK_DTTM + "</td>");
                        out.write("</tr>");
                    }
                }
            }
        } else {
            out.write("<tr>");
            out.write("<td colspan='17' style='text-align: center; font-size: 25px;'>");
            out.write("위치 정보를 입력한 후에 조회해 주세요.");
            out.write("</td>");
            out.write("</tr>");
        }
    %>
</table>

</body>
</html>