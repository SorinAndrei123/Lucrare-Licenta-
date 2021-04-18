package com.example.aplicatielicenta.student;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicatielicenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import utilitare.general.Intrebare;
import utilitare.general.Scor;


public class QuizStudentFragment extends Fragment {
    String numeMaterie;
    String numeStudent;
    TextView scor,nrIntrebare,intrebare,countdown;
    Button confirm;
    RadioGroup radioGroup;
    FirebaseFirestore firebaseFirestore;
    List<Intrebare> listIntrebari=new ArrayList<>();
    private ColorStateList textColorDefaultRb;
    private int questionCounter;
    private int questionCountTotal;
    private int score;
    private boolean answered;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Intrebare intrebareCurenta;
private static final long COUNTDOWN_IN_MILIS=30000;
private ColorStateList textColorDefaultCounter;
private CountDownTimer countDownTimer;
private long timeLeftInMilis;
View view1;



    public QuizStudentFragment() {
        // Required empty public constructor
    }



    public static QuizStudentFragment newInstance(String numeMaterie,String numeStudent) {
        QuizStudentFragment fragment = new QuizStudentFragment();
        Bundle args = new Bundle();
        args.putString("numeMaterie",numeMaterie);
        args.putString("numeStudent",numeStudent);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_quiz_student, container, false);
        view1=view;
        initView(view);
        return view;
    }

    private void initView(View view) {
        numeMaterie=getArguments().getString("numeMaterie");
        numeStudent=getArguments().getString("numeStudent");
        scor=view.findViewById(R.id.textViewScore);
        nrIntrebare=view.findViewById(R.id.textView_question_count);
        countdown=view.findViewById(R.id.textViewCountDown);
        intrebare=view.findViewById(R.id.textViewIntrebare);
        confirm=view.findViewById(R.id.button_conform_next);
        radioGroup=view.findViewById(R.id.radioGroupAlegereRaspuns);
        rb1=view.findViewById(R.id.radioButtonOptiunea1);
        rb2=view.findViewById(R.id.radioButtonOptiunea2);
        rb3=view.findViewById(R.id.radioButtonOptiunea3);
        rb4=view.findViewById(R.id.radioButtonOptiunea4);
        textColorDefaultRb=rb1.getTextColors();
        textColorDefaultCounter=countdown.getTextColors();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Intrebari").whereEqualTo("materie",numeMaterie).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                        Intrebare intrebare=queryDocumentSnapshot.toObject(Intrebare.class);
                        listIntrebari.add(intrebare);
                    }
                    Toast.makeText(getContext().getApplicationContext(), "Size lista intrebari: "+listIntrebari.size(), Toast.LENGTH_SHORT).show();
                    questionCountTotal=listIntrebari.size();
                    Collections.shuffle(listIntrebari);
                    showNextQuestion();
                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!answered){
                                if(rb1.isChecked()||rb2.isChecked()||rb3.isChecked()||rb4.isChecked()){
                                    checkAnswer(view);
                                }
                                else{
                                    Toast.makeText(getContext().getApplicationContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                showNextQuestion();
                            }
                        }
                    });
                }
            }
        });
    }

    private void checkAnswer(View view) {
        answered=true;
        countDownTimer.cancel();
        RadioButton selectedRB=view.findViewById(radioGroup.getCheckedRadioButtonId());
        int answerNr=radioGroup.indexOfChild(selectedRB);
        if(answerNr==intrebareCurenta.getRaspunsCorect()){
            score++;
            scor.setText("Score: "+score);
        }
        showSolution();

    }

    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);
        switch (intrebareCurenta.getRaspunsCorect()){
            case 0:
                rb1.setTextColor(Color.GREEN);
                intrebare.setText("Answer 1 is correct");
                break;
            case 1:
                rb2.setTextColor(Color.GREEN);
                intrebare.setText("Answer 2 is correct");
                break;
            case 2:
                rb3.setTextColor(Color.GREEN);
                intrebare.setText("Answer 3 is correct");
                break;
            case 3:
                rb4.setTextColor(Color.GREEN);
                intrebare.setText("Answer 4 is correct");
                break;

        }
        if(questionCounter<questionCountTotal){
            confirm.setText("Next");
        }
        else{
            confirm.setText("Finish");
        }


    }

    private void showNextQuestion() {

        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rb4.setTextColor(textColorDefaultRb);
        radioGroup.clearCheck();
        if(questionCounter<questionCountTotal){
            intrebareCurenta=listIntrebari.get(questionCounter);
            intrebare.setText(intrebareCurenta.getCerinta());
            rb1.setText(intrebareCurenta.getVarianteRaspuns().get(0));
            rb2.setText(intrebareCurenta.getVarianteRaspuns().get(1));
            rb3.setText(intrebareCurenta.getVarianteRaspuns().get(2));
            rb4.setText(intrebareCurenta.getVarianteRaspuns().get(3));
            questionCounter++;
            nrIntrebare.setText("Question: "+questionCounter+"/"+questionCountTotal);
            answered=false;
            confirm.setText("Confirm");
            timeLeftInMilis=COUNTDOWN_IN_MILIS;
            startCountDown();

        }
        else{
            finishQuiz();
        }
    }

    private void startCountDown() {
        countDownTimer=new CountDownTimer(timeLeftInMilis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilis=millisUntilFinished;
                updateCountDownText();

            }

            @Override
            public void onFinish() {
                timeLeftInMilis=0;
                updateCountDownText();
               checkAnswer(view1);

            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes=(int)(timeLeftInMilis/1000)/60;
        int seconds=(int)(timeLeftInMilis/1000)%60;
        String timeFormatted=String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        countdown.setText(timeFormatted);
        if(timeLeftInMilis<10000){
            countdown.setTextColor(Color.RED);
        }
        else{
            countdown.setTextColor(textColorDefaultCounter);
        }
    }

    private void finishQuiz() {
       // getActivity().finish();
        firebaseFirestore.collection("Scor").whereEqualTo("numeStudent",numeStudent).whereEqualTo("numeMaterie",numeMaterie).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult().size()>0){
                    for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                        String id=task.getResult().getDocuments().get(0).getId();
                        Scor scor= queryDocumentSnapshot.toObject(Scor.class);
                        if(score>scor.getHighscore()){
                            firebaseFirestore.collection("Scor").document(id).update("highscore",score);

                        }
                    }

                }
                else{
                    Scor scor1=new Scor(score,numeStudent,numeMaterie);
                    firebaseFirestore.collection("Scor").document().set(scor1);

                }

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(countDownTimer!=null){
            countDownTimer.cancel();
        }
    }
}