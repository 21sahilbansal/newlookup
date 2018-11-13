package com.loconav.lookup.network.rest;

import com.loconav.lookup.Repair;
import com.loconav.lookup.login.model.Creds;
import com.loconav.lookup.login.model.LoginResponse;
import com.loconav.lookup.model.Attachments;
import com.loconav.lookup.model.Client;
import com.loconav.lookup.model.CoordinateRequest;
import com.loconav.lookup.model.Coordinates;
import com.loconav.lookup.model.FastagsList;
import com.loconav.lookup.model.InstallationDetails;
import com.loconav.lookup.model.InstallationRequirements;
import com.loconav.lookup.model.InstallationResponse;
import com.loconav.lookup.model.Installs;
import com.loconav.lookup.model.LookupResponse;
import com.loconav.lookup.model.NewInstall;
import com.loconav.lookup.model.NewInstallDataandCount;
import com.loconav.lookup.model.ReasonResponse;
import com.loconav.lookup.model.RepairDetail;
import com.loconav.lookup.model.RepairRequirements;
import com.loconav.lookup.model.RepairResponse;
import com.loconav.lookup.model.Repairs;
import com.loconav.lookup.model.Repairsdataandcount;
import com.loconav.lookup.model.VehiclesList;
import com.loconav.lookup.model.VersionResponse;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
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

    @GET("api/installers/repairs/{user_id}")
    Call<RepairDetail> getRepairDetail(@Path(value = "user_id", encoded = true) int userId);

    @GET("api/installers/repairs")
    Call<Repairsdataandcount> getRepairLogs(@Query("start_index") int start, @Query("end_index") int end);

    @POST("api/installers/installations/device_installation")
    Call<ResponseBody>  addNewInstall(@Body NewInstall newInstall);

    @GET("api/installers/installations/get_installations")
    Call<NewInstallDataandCount> getInstallLogs(@Query("start_index") int start, @Query("end_index") int end);

    @GET("api/installers/installations/{install_id}")
    Call<InstallationDetails> getInstallDetail(@Path(value = "install_id", encoded = true) int installId);
}