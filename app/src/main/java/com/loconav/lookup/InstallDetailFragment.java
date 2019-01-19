package com.loconav.lookup;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loconav.lookup.adapter.ImageSetterAdapter;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.customcamera.Callback;
import com.loconav.lookup.dialog.FullImageDialog;
import com.loconav.lookup.model.AttachmentsDetails;
import com.loconav.lookup.model.InstallationDetails;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.databinding.*;
import com.loconav.lookup.network.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.loconav.lookup.Constants.FRAGMENT_NAME;

public class InstallDetailFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    FragmentInstallDetailsBinding installDetailsBinding;
    InstallationDetails installs=new InstallationDetails();
    int installId;
    @Override
    public int setViewId() {
        return R.layout.fragment_install_details;
    }

    @Override
    public void onFragmentCreated() {
        Bundle bundle = this.getArguments();
        installId = bundle.getInt("id");
        installDetailsBinding.swipeRefresh.setOnRefreshListener(this);
        loadInstallDetail();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.share, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent intent =new Intent(getActivity(), BaseNavigationActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString(FRAGMENT_NAME,getString(R.string.screenshot_fragment));
                bundle.putParcelable(getString(R.string.installation_details),installs);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void bindView(View view) {
        installDetailsBinding= DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        installDetailsBinding.unbind();
    }

    @Override
    public void onRefresh() {
        loadInstallDetail();
    }

    public void loadInstallDetail()
    {
        apiService.getInstallDetail(installId).enqueue(new RetrofitCallback<InstallationDetails>() {
            @Override
            public void handleSuccess(Call<InstallationDetails> call, Response<InstallationDetails> response) {
                installDetailsBinding.swipeRefresh.setRefreshing(false);
                 installs=response.body();
                if(installs!=null) {
                    installDetailsBinding.setInstalls(installs);
                    List<AttachmentsDetails> attachmentsList;
                    attachmentsList = installs.getAttachments();
                    List<String> deviceimages = new ArrayList<>(), truckimages = new ArrayList<>(), connectionimages = new ArrayList<>(), fittingimages = new ArrayList<>(), accessories = new ArrayList<>(),earthconnectionimages=new ArrayList<>();
                    for (AttachmentsDetails attachmentsDetails : attachmentsList) {
                        if(getActivity()!=null) {
                            if (attachmentsDetails.getTag() != null) {
                                if (attachmentsDetails.getTag().equals(getResources().getString(R.string.truck_image_tag)))
                                    truckimages.add(attachmentsDetails.getUrls().getOriginal());
                                else if (attachmentsDetails.getTag().equals(getResources().getString(R.string.device_image_tag)))
                                    deviceimages.add(attachmentsDetails.getUrls().getOriginal());
                                else if (attachmentsDetails.getTag().equals(getResources().getString(R.string.wire_connection_tag)))
                                    connectionimages.add(attachmentsDetails.getUrls().getOriginal());
                                else if (attachmentsDetails.getTag().equals(getResources().getString(R.string.device_fitting_tag)))
                                    fittingimages.add(attachmentsDetails.getUrls().getOriginal());
                                else if (attachmentsDetails.getTag().equals(getResources().getString(R.string.accessories_tag)))
                                    accessories.add(attachmentsDetails.getUrls().getOriginal());
                                else if (attachmentsDetails.getTag().equals(getResources().getString(R.string.earth_wire_connection_tag)))
                                    earthconnectionimages.add(attachmentsDetails.getUrls().getOriginal());
                            }
                        }
                    }
                    ImageSetterAdapter deviceAdapter, truckAdapter, connectionAdapter,earthConnectionAdapter, fittingAdapter, accessoriesAdapter;
                    LinearLayoutManager truckLayoutManager = new LinearLayoutManager(getContext());
                    truckLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayoutManager deviceLayoutManager = new LinearLayoutManager(getContext());
                    deviceLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayoutManager connectionLayoutManger = new LinearLayoutManager(getContext());
                    connectionLayoutManger.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayoutManager fittingLayoutManger = new LinearLayoutManager(getContext());
                    fittingLayoutManger.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayoutManager accessoriesLayoutManger = new LinearLayoutManager(getContext());
                    accessoriesLayoutManger.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayoutManager earthConnectionManager=new LinearLayoutManager(getContext());
                    accessoriesLayoutManger.setOrientation(LinearLayoutManager.HORIZONTAL);

                    truckAdapter = new ImageSetterAdapter(truckimages, new Callback() {
                        @Override
                        public void onEventDone(Object object) {
                            if(getActivity()!=null) {
                                FullImageDialog fullImageDialog = FullImageDialog.newInstance((String) object);
                                fullImageDialog.show(getActivity().getSupportFragmentManager(),getClass().getSimpleName());
                            }
                        }
                    });
                    installDetailsBinding.truckimages.setLayoutManager(truckLayoutManager);
                    installDetailsBinding.truckimages.setAdapter(truckAdapter);

                    deviceAdapter = new ImageSetterAdapter(deviceimages, new Callback() {
                        @Override
                        public void onEventDone(Object object) {
                            if(getActivity()!=null) {
                                FullImageDialog fullImageDialog = FullImageDialog.newInstance((String) object);
                                fullImageDialog.show(getActivity().getSupportFragmentManager(),getClass().getSimpleName());
                            }
                        }
                    });
                    installDetailsBinding.deviceimages.setLayoutManager(deviceLayoutManager);
                    installDetailsBinding.deviceimages.setAdapter(deviceAdapter);

                    connectionAdapter = new ImageSetterAdapter(connectionimages, new Callback() {
                        @Override
                        public void onEventDone(Object object) {
                            if(getActivity()!=null) {
                                FullImageDialog fullImageDialog = FullImageDialog.newInstance((String) object);
                                fullImageDialog.show(getActivity().getSupportFragmentManager(),getClass().getSimpleName());
                            }
                        }
                    });
                    installDetailsBinding.wireconnection.setLayoutManager(connectionLayoutManger);
                    installDetailsBinding.wireconnection.setAdapter(connectionAdapter);

                    truckAdapter = new ImageSetterAdapter(truckimages, new Callback() {
                        @Override
                        public void onEventDone(Object object) {
                            if(getActivity()!=null) {
                                FullImageDialog fullImageDialog = FullImageDialog.newInstance((String) object);
                                fullImageDialog.show(getActivity().getSupportFragmentManager(),getClass().getSimpleName());
                            }
                        }
                    });
                    installDetailsBinding.truckimages.setLayoutManager(truckLayoutManager);
                    installDetailsBinding.truckimages.setAdapter(truckAdapter);

                    earthConnectionAdapter = new ImageSetterAdapter(earthconnectionimages, new Callback() {
                        @Override
                        public void onEventDone(Object object) {
                            if(getActivity()!=null) {
                                FullImageDialog fullImageDialog = FullImageDialog.newInstance((String) object);
                                fullImageDialog.show(getActivity().getSupportFragmentManager(),getClass().getSimpleName());
                            }
                        }
                    });
                    installDetailsBinding.earthwireconnection.setLayoutManager(earthConnectionManager);
                    installDetailsBinding.earthwireconnection.setAdapter(earthConnectionAdapter);

                    fittingAdapter = new ImageSetterAdapter(fittingimages, new Callback() {
                        @Override
                        public void onEventDone(Object object) {
                            if(getActivity()!=null) {
                                FullImageDialog fullImageDialog = FullImageDialog.newInstance((String) object);
                                fullImageDialog.show(getActivity().getSupportFragmentManager(),getClass().getSimpleName());
                            }
                        }
                    });
                    installDetailsBinding.devicefitting.setLayoutManager(fittingLayoutManger);
                    installDetailsBinding.devicefitting.setAdapter(fittingAdapter);

                    accessoriesAdapter = new ImageSetterAdapter(accessories, new Callback() {
                        @Override
                        public void onEventDone(Object object) {
                            if(getActivity()!=null) {
                                FullImageDialog fullImageDialog = FullImageDialog.newInstance((String) object);
                                fullImageDialog.show(getActivity().getSupportFragmentManager(),getClass().getSimpleName());
                            }
                        }
                    });
                    installDetailsBinding.accessories.setLayoutManager(accessoriesLayoutManger);
                    installDetailsBinding.accessories.setAdapter(accessoriesAdapter);
                }
                else{
                    if(getContext()!=null)
                        Toaster.makeToast(getString(R.string.swipe_to_refresh));
                }
            }
            @Override
            public void handleFailure(Call<InstallationDetails> call, Throwable t) {
                installDetailsBinding.swipeRefresh.setRefreshing(false);
                if(getContext()!=null)
                    Toaster.makeToast(t.getMessage());
            }
        });
    }


}
