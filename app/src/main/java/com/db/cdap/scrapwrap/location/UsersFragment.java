package com.db.cdap.scrapwrap.location;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.db.cdap.scrapwrap.R;
import com.db.cdap.scrapwrap.user.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {

    private Button mLogoutBtn;
    private FirebaseAuth mAuth;

    private RecyclerView mUsersListView;

    private List<UserInformation> usersList;
    private UsersRecyclerAdapter usersRecyclerAdapter;

    private FirebaseFirestore mFirestore;
    private String mUserId;

    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        mFirestore = FirebaseFirestore.getInstance();

        mUsersListView = (RecyclerView)view.findViewById(R.id.users_list_view);

        usersList = new ArrayList<>();
        usersRecyclerAdapter = new UsersRecyclerAdapter(container.getContext(), usersList);

        mUsersListView.setHasFixedSize(true);
        mUsersListView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mUsersListView.setAdapter(usersRecyclerAdapter);

        mAuth = FirebaseAuth.getInstance();

        mLogoutBtn = (Button) view.findViewById(R.id.logout_btn);

        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Map<String, Object> tokenMapRemove = new HashMap<>();
                tokenMapRemove.put("token_id", FieldValue.delete());

                mFirestore.collection("Users").document(mUserId).update(tokenMapRemove).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mAuth.signOut();
                        Intent loginIntent = new Intent(container.getContext(), LoginActivity.class);
                        startActivity(loginIntent);
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        usersList.clear();

        mFirestore.collection("Users").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges())
                {
                    if(doc.getType() == DocumentChange.Type.ADDED)
                    {
                        String user_id = doc.getDocument().getId();

                        UserInformation users = doc.getDocument().toObject(UserInformation.class).withId(user_id);
                        usersList.add(users);

                        usersRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
