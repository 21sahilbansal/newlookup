package com.loconav.lookup.login;

import com.loconav.lookup.application.BaseView;
import com.loconav.lookup.login.model.LoginResponse;

/**
 * Created by prateek on 12/06/18.
 */

public interface LoginView extends BaseView{
    void onLoginSuccess(LoginResponse loginResponse);
    void onLoginFailure();
    void showSigningInDialog();
    void hideSigningInDialog();
    boolean isUserOnline();
}
