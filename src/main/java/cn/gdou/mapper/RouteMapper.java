package cn.gdou.mapper;

import cn.gdou.domain.Favorite;
import cn.gdou.domain.Route;
import cn.gdou.domain.RouteImg;
import cn.gdou.domain.Seller;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RouteMapper {


    @Select("select count(*) from tab_route")
    int findTotalCount1();

    @Select("select count(*) from tab_route where cid=#{cid}")
    int findTotalCount(int cid);

    @Select("select count(*) from tab_route where rname like #{cname}")
    int findTotalCount3(String cname);

    @Select("select count(*) from tab_route where cid=#{cid} and rname like #{cname}")
    int findTotalCount4(@Param("cid") int cid, @Param("cname") String cname);


    @Select("select * from tab_route limit #{start},#{pageSize}")
    List<Route> findByPage1(@Param("start") int start, @Param("pageSize") int pageSize);

    @Select("select * from tab_route where cid=#{cid} limit #{start},#{pageSize}")
    List<Route> findByPage(@Param("cid") int cid,@Param("start") int start,@Param("pageSize") int pageSize);

    @Select("select * from tab_route where rname like #{cname}  limit #{start},#{pageSize}")
    List<Route> findByPage3(@Param("start") int start,@Param("pageSize") int pageSize,@Param("cname") String cname);

    @Select("select * from tab_route where cid=#{cid} and rname like #{cname}  limit #{start},#{pageSize}")
    List<Route> findByPage4(@Param("cid") int cid,@Param("start") int start,@Param("pageSize") int pageSize,@Param("cname") String cname);

    /*
     * 根据rid查询单个Route信息
     * */
    @Select("select * from tab_route where rid=#{rid}")
    Route findOneRoute(int rid);

    /*
     * 根据rid查询图片信息
     * */
    @Select("select * from tab_route_img where rid=#{rid}")
    List<RouteImg> findRouteImg(int rid);


    /*
     * 根据sid查询商家信息
     * */
    @Select("select * from tab_seller where sid=#{sid}")
    Seller findSeller(int sid);


    /*
     * 查询收藏
     * */
    @Select("select * from tab_favorite where rid=#{rid} and uid=#{uid}")
    Favorite findOneFavorite(@Param("rid") int rid,@Param("uid") int uid);

    /*
     * 根据rid查询某个商品的收藏人数
     * */
    @Select("select count(*) from tab_favorite where rid=#{rid}")
    int findFavoriteCountByRid(int rid);

    /*
     * 添加收藏
     * */
    @Insert("insert into tab_favorite(rid,uid) values(#{rid},#{uid})")
    void addFavorite(@Param("rid") int rid,@Param("uid") int uid);

    /*
     * 我的收藏
     * */
    @Select("select * from tab_favorite where uid=#{uid} limit #{start},#{pageSize}")
    List<Favorite> findFavoriteList(@Param("uid") int uid, @Param("start") int start, @Param("pageSize") int pageSize);


    /*
     * 收藏总数
     * */
    @Select("select count(*) from tab_favorite where uid=#{uid}")
    int findFavoriteCount(int uid);

    /*
     * 人气旅游，筛选出收藏人数最多的4条
     * */
    @Select("select * from tab_route where rid in (select t.rid from " +
            "(select rid from tab_favorite group by rid order by count(*) desc limit 4) as t) ")
    List<Route> findFavoriteFourRank();

    /*
     * 最新旅游，筛选出最新的4条
     * */
    @Select("SELECT * FROM `tab_route` order by rdate desc limit 4")
    List<Route> findNewFour();


    @Select("select * from tab_route where rid in (#{one},#{two},#{three},#{four})")
    List<Route> findRandFourRoute(@Param("one") int one,@Param("two") int two,@Param("three") int three,@Param("four") int four);

}
