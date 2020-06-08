package cn.gdou.controller;

import cn.gdou.domain.PageBean;
import cn.gdou.domain.Route;
import cn.gdou.domain.User;
import cn.gdou.service.RouteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
public class RouteController {

    @Autowired
    private RouteService routeService;


    @GetMapping("pageQuery")
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {


        //接收参数
        String currentPageStr=request.getParameter("currentPage");
        String pageSizeStr=request.getParameter("pageSize");
        String cidStr=request.getParameter("cid");
        String cname=request.getParameter("cname");



        int cid=0;
        if (StringUtils.isNotBlank(cidStr)){
            cid=Integer.parseInt(cidStr);
        }

        int pageSize=0;
        if (StringUtils.isNotBlank(pageSizeStr)){
            pageSize=Integer.parseInt(pageSizeStr);
        }else {
            pageSize=5;
        }

        int currentPage=0;
        if (StringUtils.isNotBlank(currentPageStr)){
            currentPage=Integer.parseInt(currentPageStr);
        }else {
            currentPage=1;
        }


        PageBean<Route> routePageBean = routeService.pageQuery(cid, currentPage, pageSize,cname);

        myWriteValue(response,routePageBean);
    }

    @GetMapping("findOneRoute")
    public void findOneRoute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String rid = request.getParameter("rid");
        Route oneRoute = routeService.findOneRoute(Integer.parseInt(rid));

        myWriteValue(response,oneRoute);

    }

    /*
    * 判断用户是否收藏
    * */
    @GetMapping("isFavorite")
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rid=request.getParameter("rid");
        User user=(User) request.getSession().getAttribute("user");
        int uid;
        if (user==null){
            //未登录
            uid=0;
        }else {
            uid=user.getUid();
        }
        Map<String, String> favorite = routeService.isFavorite(Integer.parseInt(rid), uid);
        favorite.put("uid",String.valueOf(uid));
        myWriteValue(response,favorite);

    }

    /*
    * 添加收藏
    * */
    @GetMapping("addFavorite")
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uid = request.getParameter("uid");
        String rid = request.getParameter("rid");
        int count = routeService.addFavorite(Integer.parseInt(rid), Integer.parseInt(uid));

        myWriteValue(response,count);
    }

    @GetMapping("favoritedList")
    public void favoriteList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        int uid=user.getUid();
        //接收参数
        String currentPageStr=request.getParameter("currentPage");
        String pageSizeStr=request.getParameter("pageSize");


        int pageSize=0;
        if (pageSizeStr!=null&&pageSizeStr.length()!=0){
            pageSize=Integer.parseInt(pageSizeStr);
        }else {
            pageSize=12;
        }

        int currentPage=0;
        if (currentPageStr!=null&&currentPageStr.length()!=0){
            currentPage=Integer.parseInt(currentPageStr);
        }else {
            currentPage=1;
        }


        PageBean<Route> favoritePageBean = routeService.favoritePage(uid,currentPage,pageSize);

        myWriteValue(response,favoritePageBean);
    }

    @GetMapping("favoriteFourRank")
    public void favoriteFourRank(HttpServletResponse response) throws IOException {
        List<Route> fourRouteList = routeService.favoriteFourRank();
        myWriteValue(response,fourRouteList);
    }

    @GetMapping("theNewFour")
    public void theNewFour(HttpServletResponse response) throws IOException {
        List<Route> fourRouteList = routeService.theNewFour();
        myWriteValue(response,fourRouteList);
    }

    @GetMapping("randRoute")
    public void randRoute(HttpServletResponse response) throws IOException {

        int totalCount= routeService.findTotalRoute();

        //保证4个数不一样，因为Set不能存储相同的数
        Set<Integer> randSet=new HashSet<>();
        while (randSet.size()<=4){
            randSet.add((int)Math.ceil(Math.random()*totalCount));
        }
        List<Integer> list = new ArrayList<>(randSet);

        List<Route> randRoute = routeService.findRandFourRoute(list.get(0),list.get(1),list.get(2),list.get(3));
        myWriteValue(response,randRoute);
    }






    /*
    * 获取服务器名字以及端口通用方法
    * */
    @GetMapping("getServerName")
    public void getServerName(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //http://localhost:8080/travel_war_exploded
        String serverName = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        myWriteValue(response, serverName);
    }

    private void myWriteValue(HttpServletResponse response, Object object) throws IOException {
        ObjectMapper mapper=new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),object);
        
    }



}
