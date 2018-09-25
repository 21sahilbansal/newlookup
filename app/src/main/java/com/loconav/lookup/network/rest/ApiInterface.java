package com.loconav.lookup.network.rest;

import com.loconav.lookup.login.model.Creds;
import com.loconav.lookup.login.model.LoginResponse;
import com.loconav.lookup.model.Client;
import com.loconav.lookup.model.CoordinateRequest;
import com.loconav.lookup.model.Coordinates;
import com.loconav.lookup.model.FastagsList;
import com.loconav.lookup.model.InstallationRequirements;
import com.loconav.lookup.model.InstallationResponse;
import com.loconav.lookup.model.LookupResponse;
import com.loconav.lookup.model.ReasonResponse;
import com.loconav.lookup.model.RepairRequirements;
import com.loconav.lookup.model.RepairResponse;
import com.loconav.lookup.model.VehiclesList;
import com.loconav.lookup.model.VersionResponse;

import org.json.JSONArray;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by prateek on 5/3/18.
 */

public interface ApiInterface {

    @GET("api/v2/device_lookup")
    Call<LookupResponse> getDeviceLookup(@Query("device_id") String deviceId);

    @GET("api/v2/client_lookup")
    Call<List<Client>> getClients(@Query("client_id") String clientId);

    @POST("api/installers/login")
    Call<LoginResponse> validateUser(@Body Creds creds);

    @GET("api/installers/install/approved_vehicles")
    Call<List<VehiclesList>> getVehicles();

    @GET("api/installers/install/compatible_fastags")
    Call<List<FastagsList>> getFastags(@Query("truck_id")int truckId);

    @POST("api/installers/installations")
    Call<InstallationResponse> createInstallation(@Body InstallationRequirements installerCreds);

    @GET("api/installers/repairs/reasons")
    Call<ResponseBody> getReasons();

    @POST("api/installers/repairs/")
    Call<RepairResponse> addRepairs(@Body RepairRequirements repairRequirements);

    @GET("api/v2/version_info/lookup_app")
    Call<VersionResponse> getVersion(@Query("current_version") int versionNum);

    @POST("api/installers/locations")
    Call<ResponseBody> addCoordinates(@Body CoordinateRequest coordinateRequest);

}