package cn.gdou.service;

import cn.gdou.domain.PageBean;
import cn.gdou.domain.Route;

import java.util.List;
import java.util.Map;


public interface RouteService {

    PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String name);


    Route findOneRoute(int rid);

    Map<String,String> isFavorite(int rid, int uid);

    int addFavorite(int rid,int uid);

    PageBean<Route> favoritePage(int uid,int currentPage,int pageSize);

    List<Route> favoriteFourRank();

    List<Route> theNewFour();

    int findTotalRoute();

    List<Route> findRandFourRoute(int one,int two,int three,int four);

}
