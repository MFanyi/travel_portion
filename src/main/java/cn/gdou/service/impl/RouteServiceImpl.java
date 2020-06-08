package cn.gdou.service.impl;

import cn.gdou.mapper.RouteMapper;
import cn.gdou.domain.*;
import cn.gdou.service.RouteService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("routeService")
public class RouteServiceImpl implements RouteService {


    @Autowired
    private RouteMapper routeMapper;

    /*
    * 根据信息分页查询
    * */
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String cname)  {

        if (("null".equals(cname)||StringUtils.isBlank(cname))&&cid!=0){
            int totalCount = routeMapper.findTotalCount(cid);
            //封装PageBean
            PageBean<Route> pageBean=new PageBean<>();
            //设置当前页码
            pageBean.setCurrentPage(currentPage);
            //设置每页显示条数
            pageBean.setPageSize(pageSize);
            //设置总记录数
//        int totalCount = routeDao.findTotalCount(cid);
            pageBean.setTotalCount(totalCount);
            //设置当页显示的数据集合
            int start=pageSize*(currentPage-1);//每页的首条记录索引

            List<Route> byPage= routeMapper.findByPage(cid, start, pageSize);

            pageBean.setList(byPage);
            //设置总页数
            int totalPage=totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1;
            pageBean.setTotalPage(totalPage);

            return pageBean;
        }else {
            int totalCount;
            if (cid != 0){
                totalCount=routeMapper.findTotalCount4(cid,"%"+cname+"%");
            }else if (cname!=null&&cname.length()>0){
                totalCount=routeMapper.findTotalCount3("%"+cname+"%");
            }else {
                totalCount=routeMapper.findTotalCount1();
            }

            //封装PageBean
            PageBean<Route> pageBean=new PageBean<>();
            //设置当前页码
            pageBean.setCurrentPage(currentPage);
            //设置每页显示条数
            pageBean.setPageSize(pageSize);
            //设置总记录数
//        int totalCount = routeDao.findTotalCount(cid);
            pageBean.setTotalCount(totalCount);
            //设置当页显示的数据集合
            int start=pageSize*(currentPage-1);//每页的首条记录索引

            List<Route> byPage;
            if (cid != 0){
                byPage = routeMapper.findByPage4(cid, start, pageSize,"%"+cname+"%");
            }else if (cname!=null&&cname.length()>0){
                byPage = routeMapper.findByPage3(start, pageSize,"%"+cname+"%");
            }else {
                byPage = routeMapper.findByPage1(start, pageSize);
            }

            pageBean.setList(byPage);

            //设置总页数
            int totalPage=totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1;
            pageBean.setTotalPage(totalPage);
            return pageBean;
        }

    }

    /*
    * 通过rid查询Route
    * */
    @Override
    public Route findOneRoute(int rid) {

        Route oneRoute = routeMapper.findOneRoute(rid);
        int favoriteCount = routeMapper.findFavoriteCountByRid(rid);
        List<RouteImg> routeImg = routeMapper.findRouteImg(rid);
        Seller seller = routeMapper.findSeller(oneRoute.getSid());

        oneRoute.setCount(favoriteCount);
        oneRoute.setSeller(seller);
        oneRoute.setRouteImgList(routeImg);


        return oneRoute;
    }

    /*
    * 查看是否已经收藏
    * */
    @Override
    public Map<String,String> isFavorite(int rid, int uid) {

        Favorite oneFavorite = routeMapper.findOneFavorite(rid, uid);
        int favoriteCount=routeMapper.findFavoriteCountByRid(rid);
        /*if (oneFavorite==null){
            return false;
        }else {
            return true;
        }*/
        Map<String,String> favoriteMap=new HashMap<>();
        boolean isFavorite=oneFavorite!=null;
        favoriteMap.put("isFavorite",String.valueOf(isFavorite));

        return favoriteMap;
    }

    /*
    * 用户收藏列表
    * */
    @Override
    public PageBean<Route> favoritePage(int uid,int currentPage,int pageSize){

        //收藏的总数量
        int totalCount=routeMapper.findFavoriteCount(uid);

        //封装PageBean
        PageBean<Route> pageBean=new PageBean<>();
        //设置当前页码
        pageBean.setCurrentPage(currentPage);
        //设置每页显示条数
        pageBean.setPageSize(pageSize);
        //设置总记录数
        pageBean.setTotalCount(totalCount);
        //设置当页显示的数据集合
        int start=pageSize*(currentPage-1);//每页的首条记录索引

        List<Favorite> favoriteList=routeMapper.findFavoriteList(uid,start,pageSize);
        List<Route> favoriteRouteList=new ArrayList<>();
        for (Favorite favorite:favoriteList){
            Route oneRoute = findOneRoute(favorite.getRid());
            favoriteRouteList.add(oneRoute);
        }

        pageBean.setList(favoriteRouteList);

        //设置总页数
        int totalPage=totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1;
        pageBean.setTotalPage(totalPage);

        return pageBean;

    }

    @Override
    public int addFavorite(int rid, int uid) {

        routeMapper.addFavorite(rid,uid);
        return routeMapper.findFavoriteCountByRid(rid);
    }

    /*
    * 收藏最多的 4个Route
    * */
    @Override
    public List<Route> favoriteFourRank() {
        return routeMapper.findFavoriteFourRank();
    }

    /*
    * 日期最新的4个Route
    * */
    @Override
    public List<Route> theNewFour() {
        return routeMapper.findNewFour();
    }

    /*
    * 获得总记录数
    * */
    @Override
    public int findTotalRoute() {
        return routeMapper.findTotalCount1();
    }

    /*
    * 随机获取 4个Route
    * */
    @Override
    public List<Route> findRandFourRoute(int one,int two,int three,int four) {
        return routeMapper.findRandFourRoute(one,two,three,four);
    }
}
