package com.example.shivam.project1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
public class fragmentSafetyAudit extends Fragment {
    private TextView mTextView;
    public static final String TAG = fragmentSafetyAudit.class.getName();
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private DatabaseReference databaseReference;
    String userID;
    String postalcode;
    public fragmentSafetyAudit()
    {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragement_safety_audit,container,false);
        View addRatingsView = view.findViewById(R.id.addRatingView);
        View ratingView = view.findViewById(R.id.ratingView);
        final ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getUid();
//        databaseReference = FirebaseDatabase.getInstance().getReference();
        Bundle bundle = getArguments();
        postalcode = "144008";
//        Sphagetty for now will be corrected later
        if(bundle == null) {
//            User can only see the rating here
            addRatingsView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            ratingView.setVisibility(View.VISIBLE);
            final  RatingBar securityRatingBar = view.findViewById(R.id.securityRating1);
            final RatingBar lightRatingBar = view.findViewById(R.id.lightRating1);
            final RatingBar transportRatingBar = view.findViewById(R.id.transportRating1);
            final RatingBar walkPathRatingBar = view.findViewById(R.id.walkRating1);
            final TextView totalUsersTextView = view.findViewById(R.id.total_users_text_view);
            fStore.collection("ratings")
                    .document(postalcode).collection("all")
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    float size = queryDocumentSnapshots.size();
                    float securityRatings = 0;
                    float lightRatings = 0;
                    float transportRatings = 0;
                    float walkpathRatings = 0;
                    for (DocumentSnapshot snapshot: queryDocumentSnapshots){
                        Map<String,Object> data = snapshot.getData();
                        for (String key:data.keySet()){
                            switch (key){
                                case "security":
                                    securityRatings += Double.parseDouble(data.get(key).toString());
                                    break;
                                case "lights":
                                    lightRatings += Double.parseDouble(data.get(key).toString());
                                    break;
                                case "transport":
                                    transportRatings += Double.parseDouble(data.get(key).toString());
                                    break;
                                case "walkpath":
                                    walkpathRatings += Double.parseDouble(data.get(key).toString());
                                    break;
                            }
                        }
                    }
                    float averageSecurityRatings = securityRatings/size;
                    float averageLightRatings = lightRatings/size;
                    float averageTransportRatings = transportRatings/size;
                    float averageWalkPathRatings = walkpathRatings/size;
                    securityRatingBar.setRating(averageSecurityRatings);
                    lightRatingBar.setRating(averageLightRatings);
                    transportRatingBar.setRating(averageTransportRatings);
                    walkPathRatingBar.setRating(averageWalkPathRatings);
                    totalUsersTextView.setText(size+"");
                    progressBar.setVisibility(View.GONE);
                    Log.i(TAG,averageSecurityRatings+"");
                    Log.i(TAG,averageLightRatings+"");

                    Log.i(TAG,averageTransportRatings+"");

                    Log.i(TAG,averageWalkPathRatings+"");


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG,"Failed");
                }
            });

        } else{
            progressBar.setVisibility(View.GONE);
            addRatingsView.setVisibility(View.VISIBLE);
            ratingView.setVisibility(View.GONE);
            final RatingBar secureRating = view.findViewById(R.id.securityRating);
            final RatingBar lightRating = view.findViewById(R.id.lightRating);
            final RatingBar transportRating = view.findViewById(R.id.walkRating);
            final RatingBar walkPath = view.findViewById(R.id.walkRating);
            Button submit = view.findViewById(R.id.ratebutton);
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

//String str = "metadata=SnapshotMetadata{hasPendingWrites=false, isFromCache=false}," +
//        " doc=Document{key=ratings/144008/all/OowLBU5XHogjUDyAAYKNtCRPepk1," +
//        " data=ArraySortedMap{(lights=>4.0), (security=>5.0), (transport=>4.0), " +
//        "(walkpath=>4.0)};, " +
//        "version=SnapshotVersion(seconds=1580909539, nanos=102635000)," +
//        " documentState=SYNCED}}," +
//        " DocumentSnapshot{key=ratings/144008/all/QLFv5XTicdMcraEWLNHHNKU6aqa2," +
//        " metadata=SnapshotMetadata{hasPendingWrites=false, isFromCache=false}, " +
//        "doc=Document{key=ratings/144008/all/QLFv5XTicdMcraEWLNHHNKU6aqa2," +
//        " data=ArraySortedMap{(lights=>4.0), (security=>4.5), (transport=>4.0)," +
//        " (walkpath=>4.0)};, version=SnapshotVersion(seconds=1580909705, nanos=46765000)," +
//        " documentState=SYNCED}}]";