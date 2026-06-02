package com.shrazavi.dadmehr;


import com.shrazavi.dadmehr.DataClass.CallLog;
import com.shrazavi.dadmehr.DataClass.Cash;
import com.shrazavi.dadmehr.DataClass.Date;
import com.shrazavi.dadmehr.DataClass.DayReserve;
import com.shrazavi.dadmehr.DataClass.Help;
import com.shrazavi.dadmehr.DataClass.Key;
import com.shrazavi.dadmehr.DataClass.Law;
import com.shrazavi.dadmehr.DataClass.Lawyer;
import com.shrazavi.dadmehr.DataClass.Like;
import com.shrazavi.dadmehr.DataClass.MessageLogin;
import com.shrazavi.dadmehr.DataClass.MessageSignup;
import com.shrazavi.dadmehr.DataClass.Nobat;
import com.shrazavi.dadmehr.DataClass.Ostan;
import com.shrazavi.dadmehr.DataClass.Post;
import com.shrazavi.dadmehr.DataClass.Price;
import com.shrazavi.dadmehr.DataClass.Rating;
import com.shrazavi.dadmehr.DataClass.Room;
import com.shrazavi.dadmehr.DataClass.Shahrestan;
import com.shrazavi.dadmehr.DataClass.Ticket;
import com.shrazavi.dadmehr.DataClass.Transaction;
import com.shrazavi.dadmehr.DataClass.User;
import com.shrazavi.dadmehr.DataClass.Vakil;
import com.shrazavi.dadmehr.core.model.UserInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Retrofitinformation {

    @FormUrlEncoded
    @POST("/addpost")
    Call<MessageSignup> addpost(@Header("Content") String contenttype,
                                @Field("username") String username,
                                @Field("img") String imgurl,
                                @Field("text") String text);

    @FormUrlEncoded
    @POST("/like")
    Call<MessageSignup> like(@Header("Content") String contenttype,
                             @Field("postid") String postid,
                             @Field("vakile") String vakile,
                             @Field("user") String user,
                             @Field("like") Boolean like,
                             @Field("id") String id);

    @FormUrlEncoded
    @POST("/getrtc")
    Call<UserInfo> getrtc(@Field("username") String username);

    @FormUrlEncoded
    @POST("/getcall")
    Call<CallLog> getcall(@Field("callid") String callid);

    @FormUrlEncoded
    @POST("/getlike")
    Call<Like> getlike(@Header("Content") String contenttype,
                       @Field("postid") String postid,
                       @Field("user") String user);

    @FormUrlEncoded
    @POST("/getpost")
    Call<ArrayList<Post>> getpost(@Field("page") int page);

    @FormUrlEncoded
    @POST("/getpostlike")
    Call<ArrayList<Like>> getlikepost(@Field("postid") String postid);

    @FormUrlEncoded
    @POST("/getcalllog")
    Call<ArrayList<CallLog>> getcalllog(@Header("Content") String contenttype,
                                        @Field("username") String username,
                                        @Field("id") String id);
    @FormUrlEncoded
    @POST("/getlaw")
    Call<ArrayList<Law>> getlaw(@Field("type") String type);
    @FormUrlEncoded
    @POST("/gethelp")
    Call<ArrayList<Help>> gethelp(@Field("type") String type);
    @FormUrlEncoded
    @POST("/getuser")
    Call<User> getuser(@Header("Content") String contenttype,
                       @Field("username") String username,
                       @Field("id") String id);

    @FormUrlEncoded
    @POST("/getvakil")
    Call<Vakil> getvakil(@Header("Content") String contenttype,
                         @Field("username") String username,
                         @Field("id") String id);

    @FormUrlEncoded
    @POST("/getvokala")
    Call<ArrayList<Lawyer>> getvokala(@Header("Content") String contenttype,
                                      @Field("id") String id);

    @FormUrlEncoded
    @POST("/getspecialty")
    Call<ArrayList<Lawyer>> getspecialty(@Header("Content") String contenttype,
                                         @Field("specialty") String specialty,
                                         @Field("id") String id);

    @FormUrlEncoded
    @POST("/getdayreserve")
    Call<ArrayList<DayReserve>> getday(@Header("Content") String contenttype,
                                       @Field("username") String username,
                                       @Field("id") String id);

    @FormUrlEncoded
    @POST("/getreserve")
    Call<ArrayList<Nobat>> getreserve(@Header("Content") String contenttype,@Field("username") String username);

    @FormUrlEncoded
    @POST("/getnobat")
    Call<Nobat> getnobat(@Header("Content") String contenttype,
                         @Field("user") String user,
                         @Field("vakile") String vakile,
                         @Field("id") String id);

    @POST("/getprice")
    Call<Price> getprice();

    @POST("/getdate")
    Call<Date> getdate();

    @POST("getostan.php")
    Call<ArrayList<Ostan>> getostan();

    @FormUrlEncoded
    @POST("getshahr.php")
    Call<ArrayList<Shahrestan>> getshahr(@Field("id") int id_ostan);

    @FormUrlEncoded
    @POST("getidshahr.php")
    Call<Shahrestan> getidshahr(@Field("name") String name_shahr);


    @FormUrlEncoded
    @POST("getnameostan.php")
    Call<Ostan> getnameostan(@Field("id") int id);

    @FormUrlEncoded
    @POST("/changepassword")
    Call<MessageSignup> changepassword(@Header("Content") String contenttype,
                                       @Field("username") String username,
                                       @Field("oldpass") String oldpass,
                                       @Field("newpass") String newpass,
                                       @Field("vu") String vu,
                                       @Field("id") String id);
    @FormUrlEncoded
    @POST("/checkuser")
    Call<MessageSignup> cheackuser(@Header("Content") String contenttype,
                                 @Field("melli") String melli,
                                   @Field("vu") String vu,
                                 @Field("username") String username);
    @FormUrlEncoded
    @POST("/signupvl")
    Call<MessageSignup> signupvl(@Header("Content") String contenttype,
                                 @Field("data") String data);

    @FormUrlEncoded
    @POST("/signupuser")
    Call<MessageSignup> signupuser(@Header("Content") String contenttype,
                                   @Field("data") String data);

    @FormUrlEncoded
    @POST("/upgradeprofilevl")
    Call<MessageSignup> upgradeprofilevl(@Header("Content") String contenttype,
                                         @Field("username") String username,
                                         @Field("name") String name,
                                         @Field("experience") String experience,
                                         @Field("specialty") String specialty,
                                         @Field("degree") String degree,
                                         @Field("education") String education,
                                         @Field("shaba") String shaba,
                                         @Field("cart") String cart,
                                         @Field("email") String email,
                                         @Field("bio") String bio,
                                         @Field("id") String id);

    @FormUrlEncoded
    @POST("/upgradelocation")
    Call<MessageSignup> upgradelocation(@Header("Content") String contenttype,
                                        @Field("username") String username,
                                        @Field("ostan") String ostan,
                                        @Field("shahr") String shahr,
                                        @Field("address") String address,
                                        @Field("id") String id);

    @FormUrlEncoded
    @POST("/upgradetariff")
    Call<MessageSignup> upgradetariff(@Header("Content") String contenttype,
                                      @Field("username") String username,
                                      @Field("chattariff") int chattariff,
                                      @Field("voisetariff") int voisetariff,
                                      @Field("videotariff") int videotariff,
                                      @Field("id") String id);

    @FormUrlEncoded
    @POST("/upgradeblack")
    Call<MessageSignup> upgradeblack(@Header("Content") String contenttype,
                                     @Field("blacklist") String blacklist,
                                     @Field("id") String id);

    @FormUrlEncoded
    @POST("/upgradeprofileuser")
    Call<MessageSignup> upgradeprofileuser(@Header("Content") String contenttype,
                                           @Field("username") String username,
                                           @Field("name") String name,
                                           @Field("email") String email,
                                           @Field("bio") String bio,
                                           @Field("id") String id);

    @FormUrlEncoded
    @POST("/checkaccount")
    Call<Cash> checkaccount(@Header("Content") String contenttype,
                               @Field("username") String username,
                               @Field("ia") Boolean ia,
                            @Field("ic") Boolean ic,
                            @Field("uservl") String uservl);

    @FormUrlEncoded
    @POST("/increaseaccount")
    Call<Cash> increaseaccount(@Header("Content") String contenttype,
                               @Field("username") String username,
                               @Field("price") Long price,
                               @Field("code") String code,
                               @Field("detail") String detail);

    @FormUrlEncoded
    @POST("/settimevl")
    Call<MessageSignup> settimevl(@Header("Content") String contenttype,
                                  @Field("username") String username,
                                  @Field("reserve") int reserve,
                                  @Field("starttime") String starttime,
                                  @Field("endtime") String endtime,
                                  @Field("visitdate") ArrayList<String> visitdate,
                                  @Field("arraytime") ArrayList<String> arraytime,
                                  @Field("activedate") String activedate,
                                  @Field("id") String id);

    @FormUrlEncoded
    @POST("/setconditioncall")
    Call<MessageSignup> setconditioncall(@Header("Content") String contenttype,
                                         @Field("callid") String callid,
                                         @Field("condition") String condition,
                                         @Field("id") String id);

    @FormUrlEncoded
    @POST("/upgradetime")
    Call<MessageSignup> upgradetime(@Header("Content") String contenttype,
                                    @Field("username") String username,
                                    @Field("arraytime") ArrayList<String> arraytime,
                                    @Field("day") String day,
                                    @Field("id") String id);

    @FormUrlEncoded
    @POST("/insertreserve")
    Call<MessageSignup> insertreserve(@Header("Content") String contenttype,
                                      @Field("user") String user,
                                      @Field("vakile") String vakile,
                                      @Field("time") String time,
                                      @Field("day") String day);

//    @FormUrlEncoded
//    @POST("/upgradeincome")
//    Call<Cash> upgradeincome(@Header("Content") String contenttype,
//                             @Field("username") String username,
//                             @Field("price") Long price,
//                             @Field("id") String id);

    @FormUrlEncoded
    @POST("/senddata")
    Call<MessageSignup> senddata(@Header("Content") String contenttype,
                               @Field("data") String data,
                               @Field("D1") String D1,
                               @Field("D2") String D2,
                               @Field("D3") String D3,
                               @Field("D4") String D4,
                               @Field("D5") String D5);

    @FormUrlEncoded
    @POST("/loginvl")
    Call<MessageLogin> loginvl(@Header("Content") String contenttype,
                               @Field("data") String data,
                               @Field("D1") String D1,
                               @Field("D2") String D2,
                               @Field("D3") String D3,
                               @Field("D4") String D4,
                               @Field("D5") String D5);

    @FormUrlEncoded
    @POST("/loginuser")
    Call<MessageLogin> loginuser(@Header("Content") String contenttype,
                                 @Field("data") String data,
                                 @Field("D1") String D1,
                                 @Field("D2") String D2,
                                 @Field("D3") String D3,
                                 @Field("D4") String D4,
                                 @Field("D5") String D5);

    @FormUrlEncoded
    @POST("/creatroom")
    Call<Room> creatroom(@Header("Content") String contenttype,
                         @Field("userA") String userA,
                         @Field("userB") String userB,
                         @Field("key") String key,
                         @Field("id") String id);

    @FormUrlEncoded
    @POST("/getkey")
    Call<Key> getkey(@Header("Content") String contenttype,
                     @Field("roomid") String roomid,
                     @Field("id") String id);
    @FormUrlEncoded
    @POST("/getaccesschat")
    Call<MessageSignup> getaccesschat(@Header("Content") String contenttype,
                     @Field("roomid") String roomid,
                     @Field("id") String id);
    @FormUrlEncoded
    @POST("/insertticket")
    Call<MessageSignup> insertticket(@Header("Content") String contenttype,
                                     @Field("user") String user,
                                     @Field("vu") String vu,
                                     @Field("su") String su,
                                     @Field("type") String type,
                                     @Field("title") String title,
                                     @Field("desc") String desc,
                                     @Field("key") String key,
                                     @Field("id") String id);
    @FormUrlEncoded
    @POST("/insertreport")
    Call<MessageSignup> insertreport(@Header("Content") String contenttype,
                                     @Field("from") String from,
                                     @Field("to") String to,
                                     @Field("vu") String vu,
                                     @Field("desc") String desc,
                                     @Field("callid") String callid,
                                     @Field("id") String id);
    @FormUrlEncoded
    @POST("/getticket")
    Call<ArrayList<Ticket>> getticket(@Header("Content") String contenttype,
                                      @Field("user") String user,
                                      @Field("id") String id);

    @FormUrlEncoded
    @POST("/getroomvl")
    Call<ArrayList<Room>> getroomvl(@Field("userB") String userB);

    @FormUrlEncoded
    @POST("/getroomuser")
    Call<ArrayList<Room>> getroomuser(@Field("userA") String userA);

    @FormUrlEncoded
    @POST("/index.php")
    Call validation(@Field("number") String number);

    @FormUrlEncoded
    @POST("/addtransaction")
    Call<MessageSignup> AddTransaction(@Header("Content") String contenttype,
                                       @Field("user") String userid,
                                       @Field("code")  int code,
                                       @Field("price") String price,
                                       @Field("status") String status,
                                       @Field("detail") String detail);

    @FormUrlEncoded
    @POST("/uptransaction")
    Call<MessageSignup> UpTransaction(@Header("Content") String contenttype,
                                      @Field("id") String id,
                                      @Field("idtrans") String idtrans);

    @FormUrlEncoded
    @POST("/checktimer")
    Call<MessageSignup> checktimer(@Header("Content") String contenttype,
                                   @Field("time") long time,
                                   @Field("id") String id);

    @FormUrlEncoded
    @POST("/gettransaction")
    Call<ArrayList<Transaction>> getTransaction(@Header("Content") String contenttype,
                                                @Field("username") String username);

    @FormUrlEncoded
    @POST("/rating")
    Call<MessageSignup> Rating(@Header("Content") String contenttype,
                               @Field("vakil") String vakil,
                               @Field("rate") int rate,
                               @Field("id") String id);

    @FormUrlEncoded
    @POST("/getrating")
    Call<Rating> getRating(@Field("username") String username);
}


