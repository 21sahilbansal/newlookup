package com.loconav.lookup.login;

import com.loconav.lookup.login.model.Creds;

/**
 * Created by prateek on 12/06/18.
 */

public interface LoginPresenter {
    void validateUser(Creds creds);
}
