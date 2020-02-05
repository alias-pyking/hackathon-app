package com.example.shivam.project1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
public class fragmentSafetyAudit extends Fragment {
    private TextView mTextView;
    public static final String TAG = fragmentSafetyAudit.class.getName();
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    String postalcode;
    public fragmentSafetyAudit()
    {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragement_safety_audit,container,false);
        View addRatingsView = view.findViewById(R.id.addRatingView);
        Bundle bundle = getArguments();
        postalcode = "144009";
//        Sphagetty for now will be corrected later
        if(postalcode == null) {
            addRatingsView.setVisibility(View.GONE);
        } else{
            final RatingBar secureRating = view.findViewById(R.id.securityRating);
            final RatingBar lightRating = view.findViewById(R.id.lightRating);
            final RatingBar transportRating = view.findViewById(R.id.walkRating);
            final RatingBar walkPath = view.findViewById(R.id.walkRating);
            Button submit = view.findViewById(R.id.ratebutton);

            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();
            userID = FirebaseAuth.getInstance().getUid();
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float secureRatingRating =secureRating.getRating();
                    float lightRatingRating = lightRating.getRating();
                    float transportRatingRating = transportRating.getRating();
                    float walkPathRating = walkPath.getRating();

//              to add the multiple ratings for a single place by multiple users
                    DocumentReference documentReference = fStore.collection("ratings")
                            .document(postalcode).collection("all").document(userID);
                    Map<String,Object> user  = new HashMap<>();
                    user.put("security",secureRating);
                    user.put("lights",lightRating);
                    user.put("transport",transportRating);
                    user.put("walkpath",walkPathRating);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i(TAG,"on success: ratings added") ;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG,"on failure: Something went wrong");
                        }
                    });

                }
            });
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextView = (TextView) getActivity().findViewById(R.id.pincode);
        Bundle bundle = getArguments();
        if(bundle != null)
        {
//            This code is never running when you directly visit safety audit
            postalcode = bundle.getString("pincode");
            Log.d("bundle",postalcode);
            mTextView.setText(postalcode);
        }
    }
}