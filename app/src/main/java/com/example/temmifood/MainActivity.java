package com.example.temmifood;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.listeners.OnMovementStatusChangedListener;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private Robot robot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Temi SDK 초기화
        robot = Robot.getInstance();
        DatabaseReference serveRef = firebaseDatabase.getReference("table_loc");
        Button returnButton = (Button) findViewById(R.id.ReturnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serveRef.setValue(0);
            }
        });


        serveRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int status = (int) snapshot.getValue(Integer.class);
                // 0 : 주방으로 1~6 : 각 테이블로 서빙
                switch (status) {
                    case 0:
                        robot.goTo("a");
                        break;

                    case 1:
                        robot.goTo("b");
                        break;

                    case 2:
                        robot.goTo("2");
                        break;

                    case 3:
                        robot.goTo("3");
                        break;

                    case 4:
                        robot.goTo("4");
                        break;

                    case 5:
                        robot.goTo("5");
                        break;

                    case 6:
                        robot.goTo("6");
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        robot.addOnMovementStatusChangedListener(new OnMovementStatusChangedListener() {
            @Override
            public void onMovementStatusChanged(@NonNull String status, @NonNull String type) {
                if (status.equals(OnMovementStatusChangedListener.STATUS_COMPLETE)) {
                    // 로봇의 이동이 완료되었을 때 실행할 작업을 여기에 구현합니다.
                    // 예를 들어, 이동 완료 메시지 출력
                    System.out.println("로봇의 이동이 완료되었습니다.");
                    robot.speak(TtsRequest.create("음식이 도착했습니다."));
                }
            }
            protected void onDestroy() {
                // Remove the listener when the activity is destroyed
                robot.removeOnMovementStatusChangedListener(this);

//                super.onDestroy();
            }

        });

    }

        // This method will be called whenever the movement status of the robot changes
}