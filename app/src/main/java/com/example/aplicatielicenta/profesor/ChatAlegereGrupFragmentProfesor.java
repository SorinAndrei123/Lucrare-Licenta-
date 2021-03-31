package com.example.aplicatielicenta.profesor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicatielicenta.R;
import com.example.aplicatielicenta.general.ChatAlegereGrupFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import utilitare.general.AdaptorPreviewConversatie;
import utilitare.general.ChatMessage;
import utilitare.general.User;

public class ChatAlegereGrupFragmentProfesor extends Fragment {
    User user;
    RecyclerView grupuri;
    static final String KEY="cheita";
    DatabaseReference databaseReference;
    List<String> listaNumeGrupuri=new ArrayList<>();
    List<ChatMessage>listaUltimulMesaj=new ArrayList<>();
    StorageReference storageReference;
    AdaptorPreviewConversatie adaptorPreviewConversatie;

    public ChatAlegereGrupFragmentProfesor() {

    }


    public static ChatAlegereGrupFragmentProfesor newInstance(User user) {
        ChatAlegereGrupFragmentProfesor fragment = new ChatAlegereGrupFragmentProfesor();
        Bundle args = new Bundle();
        args.putSerializable(KEY,user);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_chat_alegere_grup, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        user= (User) getArguments().getSerializable(KEY);

    }
}
