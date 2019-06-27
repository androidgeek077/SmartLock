package app.techsol.smartlock.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import app.techsol.smartlock.Models.ProductModel;
import app.techsol.smartlock.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProductsFragment extends Fragment {


//    int images[] = {R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4};
    private ArrayList<String> mCategories = new ArrayList<>();


    DatabaseReference CustomerReference;
//    CustomerProfileAdapter mProductAdapter;
    RecyclerView mCustomerRecycVw;

    public ViewProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_products, container, false);

        getActivity().setTitle("Products");


        CustomerReference = FirebaseDatabase.getInstance().getReference().child("Categories");
        mCustomerRecycVw = view.findViewById(R.id.main_recycler_vw);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mCustomerRecycVw.setLayoutManager(mLayoutManager);
        return view;

    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<ProductModel> options = new FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(CustomerReference, ProductModel.class)
                .build();

        final FirebaseRecyclerAdapter<ProductModel, CustomersViewHolder> adapter = new FirebaseRecyclerAdapter<ProductModel, ViewProductsFragment.CustomersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewProductsFragment.CustomersViewHolder holder, final int position, @NonNull final ProductModel model) {


                DisplayMetrics displaymetrics = new DisplayMetrics();
                (getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                //if you need three fix imageview in width

                holder.ProductKey.setText(model.getProductkey());

                Glide.with(getActivity().getApplicationContext()).load(model.getImgurl()).into(holder.BarCodeImage);

                holder.mDelCustomerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference key= getRef(position);
                        key.removeValue();
                    }
                });



            }

            @NonNull
            @Override
            public ViewProductsFragment.CustomersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_item_layout, viewGroup, false);
                ViewProductsFragment.CustomersViewHolder customersViewHolder = new ViewProductsFragment.CustomersViewHolder(view);
                return customersViewHolder;
            }
        };

        mCustomerRecycVw.setAdapter(adapter);
        adapter.startListening();

    }


    public static class CustomersViewHolder extends RecyclerView.ViewHolder {


        ImageView BarCodeImage;
        TextView ProductKey;
        Button mDelCustomerBtn;



        public CustomersViewHolder(@NonNull View itemView) {
            super(itemView);

            BarCodeImage = (ImageView) itemView.findViewById(R.id.UserImage);
            ProductKey = (TextView) itemView.findViewById(R.id.userName);
            mDelCustomerBtn=itemView.findViewById(R.id.DeleteBtn);


        }
    }

}
