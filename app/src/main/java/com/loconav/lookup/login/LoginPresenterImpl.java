package com.loconav.lookup.login;

import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.login.model.Creds;
import com.loconav.lookup.login.model.LoginResponse;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.network.rest.LoginApiClient;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by prateek on 12/06/18.
 */

public class LoginPresenterImpl implements LoginPresenter{
    private LoginView loginView;
    private ApiInterface apiService = LoginApiClient.getClient().create(ApiInterface.class);


    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void validateUser(Creds creds) {
        if (creds.getPhoneNumber() == null || creds.getPhoneNumber().isEmpty()) {
            loginView.showToast("Username Can't Be Empty");
        }else if(creds.getPassword() == null || creds.getPassword().isEmpty()) {
            loginView.showToast("Password Can't Be Empty");
        } else {
            loginView.showSigningInDialog();
            apiService.validateUser(creds).enqueue(new RetrofitCallback<LoginResponse>() {
                @Override
                public void handleSuccess(Call<LoginResponse> call, Response<LoginResponse> response) {
                    loginView.showToast(response.body().getUser().getName());
                    loginView.hideSigningInDialog();
                    loginView.onLoginSuccess(response.body());
//                    SharedPrefHelper.saveUser(response.body().getUser());
                }

                @Override
                public void handleFailure(Call<LoginResponse> call, Throwable t) {
                    loginView.showToast(t.getMessage());
                    loginView.hideSigningInDialog();
                    loginView.onLoginFailure();
                }
            });
        }

    }

}
