package com.example.basictest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GithubService {

    @POST("api/app/user/SimpleInfo")
    Call<UserBean> listRepos(@Body String user);

}
