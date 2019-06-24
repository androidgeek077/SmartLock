package app.techsol.smartlock.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

import app.techsol.smartlock.Models.UserModel;
import app.techsol.smartlock.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewUsersFragment extends Fragment {


    //    int images[] = {R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4};
    private ViewFlipper simpleViewFlipper;
    private ArrayList<String> mCategories = new ArrayList<>();
    RotateLoading loading;

    int countInt, incrementalCount;
    DatabaseReference CustomerReference;
    //    CustomerProfileAdapter mProductAdapter;
    RecyclerView mCustomerRecycVw;
    String count;

    public ViewUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_products, container, false);

        getActivity().setTitle("Users");

        loading = view.findViewById(R.id.rotateloading);
        loading.setVisibility(View.VISIBLE);
        loading.start();
        mCustomerRecycVw = view.findViewById(R.id.main_recycler_vw);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mCustomerRecycVw.setLayoutManager(mLayoutManager);

        return view;

    }


    @Override
    public void onStart() {
        super.onStart();
        CustomerReference = FirebaseDatabase.getInstance().getReference().child("Users");

        FirebaseRecyclerOptions<UserModel> options = new FirebaseRecyclerOptions.Builder<UserModel>()
                .setQuery(CustomerReference, UserModel.class)
                .build();

        final FirebaseRecyclerAdapter<UserModel, CustomersViewHolder> adapter = new FirebaseRecyclerAdapter<UserModel, ViewUsersFragment.CustomersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewUsersFragment.CustomersViewHolder holder, final int position, @NonNull final UserModel model) {


                DisplayMetrics displaymetrics = new DisplayMetrics();
                (getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                //if you need three fix imageview in width

                holder.mName.setText(model.getName());
                holder.mPhone.setText(model.getPhone());
                holder.mEmail.setText(model.getEmail());
                holder.mProductKey.setText(model.getProductkey());
                holder.mAddress.setText(model.getAddress());


//                holder.mMinusBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        count=holder.mTotalCount.getText().toString();
//                        countInt=Integer.parseInt(count);
//                        incrementalCount=countInt--;
//
//                        holder.mTotalCount.setText(countInt+"");
//                    }
//                });


            }

            @NonNull
            @Override
            public ViewUsersFragment.CustomersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_dummy_item_layout, viewGroup, false);
                ViewUsersFragment.CustomersViewHolder customersViewHolder = new ViewUsersFragment.CustomersViewHolder(view);
                return customersViewHolder;
            }
        };

        mCustomerRecycVw.setAdapter(adapter);
        adapter.startListening();
        loading.setVisibility(View.GONE);

    }


    public static class CustomersViewHolder extends RecyclerView.ViewHolder {


        ImageView postImage;
        TextView mName, mPhone, mEmail, mProductKey, mAddress;


        public CustomersViewHolder(@NonNull View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.UserNameTV);
            mPhone = itemView.findViewById(R.id.UserPhoneTV);
            mEmail = itemView.findViewById(R.id.EmailTV);
            mProductKey = itemView.findViewById(R.id.ProductKeyTV);
            mAddress = itemView.findViewById(R.id.AddressTV);

        }
    }

}
