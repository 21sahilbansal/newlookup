package com.loconav.lookup.network.rest;

import com.loconav.lookup.ignitontest.model.dataClass.IgnitionTestData;
import com.loconav.lookup.login.model.Creds;
import com.loconav.lookup.login.model.LoginResponse;
import com.loconav.lookup.model.ApiException;
import com.loconav.lookup.model.AttachmentList;
import com.loconav.lookup.model.Client;
import com.loconav.lookup.model.CoordinateRequest;
import com.loconav.lookup.model.FastTagResponse;
import com.loconav.lookup.model.FastagsList;
import com.loconav.lookup.model.InstallDatandTotalInstallCount;
import com.loconav.lookup.model.InstallationDetails;
import com.loconav.lookup.model.InstallationRequirements;
import com.loconav.lookup.model.InstallationResponse;
import com.loconav.lookup.model.LookupResponse;
import com.loconav.lookup.model.NewInstall;
import com.loconav.lookup.model.RepairDetail;
import com.loconav.lookup.model.RepairRequirements;
import com.loconav.lookup.model.RepairResponse;
import com.loconav.lookup.model.RepairsDataandTotalRepairCount;
import com.loconav.lookup.model.VehiclesList;
import com.loconav.lookup.model.VersionResponse;
import com.loconav.lookup.newfastag.model.VehicleDetails;
import com.loconav.lookup.tutorial.model.dataClass.TutorialObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by prateek on 5/3/18.
 */

public interface ApiInterface {
    /**
     * This GET method is used to get device details when we pass device_id as parameter in it.
     *
     * @param deviceId This is the serial number of the device of which we have to get details
     * @return
     */
    @GET("api/v2/device_lookup")
    Call<LookupResponse> getDeviceLookup(@Query("device_id") String deviceId);

    /**
     * This GET method return the client names against a client_id
     *
     * @param clientId This is the client id as param
     * @return
     */
    @GET("api/v2/client_lookup")
    Call<List<Client>> getClients(@Query("client_id") String clientId);

    /**
     * This POST method validates the credentials(username and password) entered by user
     *
     * @param creds This is username and password entered by user
     * @return
     */
    @POST("api/installers/login")
    Call<LoginResponse> validateUser(@Body Creds creds);

    /**
     * This GET method is used to get vehicles list for fasttag
     *
     * @return
     */
    @GET("api/installers/install/approved_vehicles")
    Call<List<VehiclesList>> getVehicles();

    /**
     * This GET method is used to validate the truckNo or fasttag no
     *
     * @param truckNumber This is truck number entered by the user
     */

    /**
     * This GET method is used to validate the truckNo or fasttag no
     *
     * @param truckNumberOrFastagNumber
     * @return
     */
    @GET("api/installers/fastag_installations/search")
    Call<FastTagResponse> validateTruckNumberOrFastagNumber(@Query("truck_number_or_fastag_serial_number") String truckNumberOrFastagNumber);


    /**
     * This GET method return the list of Fastags for a particular truck id
     *
     * @param truckId
     * @return
     */
    @GET("api/installers/install/compatible_fastags")
    Call<List<FastagsList>> getFastags(@Query("truck_id") int truckId);

    /**
     * This POST method is used to create new installations
     *
     * @param installerCreds
     * @return
     */
    @POST("api/installers/installations")
    Call<InstallationResponse> createInstallation(@Body InstallationRequirements installerCreds);

    /**
     * This GET method gives the reason for what you want to post repair request(sim change,device change etc.)
     *
     * @return
     */
    @GET("api/installers/repairs/reasons")
    Call<ResponseBody> getReasons();

    /**
     * This POST method is used to send the repair of the user with repair details as params
     *
     * @param repairRequirements This is the details of the repair
     * @return
     */
    @POST("api/installers/repairs/")
    Call<RepairResponse> addRepairs(@Body RepairRequirements repairRequirements);

    /**
     * This GET method is used to the version of the app.
     *
     * @param versionNum
     * @return
     */
    @GET("api/v2/version_info/lookup_app")
    Call<VersionResponse> getVersion(@Query("current_version") int versionNum);

    /**
     * This POST method post the the location of the user
     *
     * @param coordinateRequest
     * @return
     */
    @POST("api/installers/locations")
    Call<ResponseBody> addCoordinates(@Body CoordinateRequest coordinateRequest);

    /**
     * The GET method is used the repair detail of the repair id(as param)
     *
     * @param repairId It is the id of the repair
     * @return
     */
    @GET("api/installers/repairs/{repair_id}")
    Call<RepairDetail> getRepairDetail(@Path(value = "repair_id") int repairId);

    /**
     * This GET method return the total repairs data and the total no of repairs between start and end index as parameters
     *
     * @param start - The start index of the repairs
     * @param end   - The end index of the repairs
     * @return
     */
    @GET("api/installers/repairs")
    Call<RepairsDataandTotalRepairCount> getRepairLogs(@Query("start_index") int start, @Query("end_index") int end);

    /**
     * This POST method is used to add a new device that is installed on a truck.
     *
     * @param newInstall
     * @return
     */
    @POST("api/installers/installations/device_installation")
    Call<ResponseBody> addNewInstall(@Body NewInstall newInstall);

    /**
     * This post method is used to upload image of fasttag installations
     *
     * @param attachmentList- Photos related to fast tag
     */

    @POST("api/installers/fastag_installation/{id}/images")
    Call<ResponseBody> addFastTagPhotos(@Path("id") Integer id, @Body AttachmentList attachmentList);

    /**
     * This GET method returns the total install data and number of install from start and end index as params.
     *
     * @param start - The start index of the repairs
     * @param end   - The end index of the repairs
     * @return
     */
    @GET("api/installers/installations/get_device_installations")
    Call<InstallDatandTotalInstallCount> getInstallLogs(@Query("start_index") int start, @Query("end_index") int end);

    /**
     * The GET method gives the details of a specific isntallation
     *
     * @param installId This is the id of install for which you want the details
     * @return
     */
    @GET("api/installers/installations/{install_id}")
    Call<InstallationDetails> getInstallDetail(@Path(value = "install_id") String installId);


     @POST("api/v1/android/app_crash_logs")
     Call<ResponseBody> logExceptionToServer(@Body ApiException exceptionThrow);

    /**
     * This GET method gives the details of vehicle on which fastag is going to be pasted
     *
     * @param vehicleNumber This is the id of the vehicle
     * @return
     */
    @GET("api/installers/fastag_installations/search_by_vehicle")
    Call<VehicleDetails> getVehcileFastagDetails(@Query("vehicle_number") String vehicleNumber);

    /**
     * This POST method is used to verify whether the scanned fastag is compatible with the vehicle and if it is
     * compatible then register the fastag with the vehicle
     *
     * @param VehicleID          This is vehicle id on which fastag to be pasted
     * @param fastagSerialNumber This is scanned fastag number
     * @return
     */

    @POST("/api/installers/fastag_installations/install")
    Call<ResponseBody> verifyScannedFastag(@Query("vehicle_id") int VehicleID, @Query("fastag_serial_number") String fastagSerialNumber);

    /**
     * This post meethod is used to do ignition test and to get data of the ignition test
     * @param deviceId       This is the device id related to a vehicle
     * @param testStartTime   This is the epoch time when the ignition Test was started
     * @return
     */
    @GET("/api/v2/device_lookup/ignition_checks")
    Call<IgnitionTestData>  getIgnitionTestData(@Query("device_id") String deviceId , @Query("test_start_ts") String testStartTime );

    /**
     * This get method is used to get data related to tutorials
     *
     * @return
     */
    @GET("/api/installers/installer_tutorials/")
    Call<List<TutorialObject>>  getLearningTutorials();
}