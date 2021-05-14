package dev.moutamid.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import ir.samanjafari.easycountdowntimer.CountDownInterface;
import ir.samanjafari.easycountdowntimer.EasyCountDownTextview;

public class TimerActivity extends AppCompatActivity {

    private Utils utils = new Utils();
    private EasyCountDownTextview countDownTextview;

    private RelativeLayout parentLayout;
    private long real_time = 0;
    private boolean getTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        parentLayout = findViewById(R.id.parent_layout_timer_activity);
        countDownTextview = findViewById(R.id.easyCountDownTextview);

//        int days = getIntent().getIntExtra("days", 0);
        int hours = getIntent().getIntExtra("hours", 0);
        int minutes = getIntent().getIntExtra("minutes", 0);
//        int seconds = getIntent().getIntExtra("seconds", 0);
        ((TextView) findViewById(R.id.title_name_textVIew))
                .setText(getIntent().getStringExtra("title_name"));

        if (utils.getStoredLong(TimerActivity.this,
                getIntent().getStringExtra("title_name") + "prev_time") == 0) {
            if (hours == 1) {
                countDownTextview.setShowHours(true);
            }
            countDownTextview.setTime(0, hours, minutes, 0);
        } else {

            long milisecond = utils.getStoredLong(
                    TimerActivity.this,
                    getIntent().getStringExtra("title_name") + "prev_time");
//            int milliseconds = Integer.parseInt(String.valueOf(milisecond));

            int prev_seconds = (int) (milisecond / 1000) % 60;
            int prev_minutes = (int) ((milisecond / (1000 * 60)) % 60);
            int prev_hours = (int) ((milisecond / (1000 * 60 * 60)) % 24);

            if (prev_hours == 1) {
                countDownTextview.setShowHours(true);
            }
            countDownTextview.setTime(0, prev_hours, prev_minutes, prev_seconds);

        }

        countDownTextview.startTimer();
        countDownTextview.pause();

        countDownTextview.setOnTick(new CountDownInterface() {
            @Override
            public void onTick(long time) {
//                Toast.makeText(TimerActivity.this, String.valueOf(time), Toast.LENGTH_SHORT).show();

                utils.storeLong(
                        TimerActivity.this,
                        getIntent()
                                .getStringExtra("title_name") + "prev_time",
                        time);

                if (getTime) {
                    real_time = time;
                    getTime = false;
                }

                if (time <= real_time / 2 && time > real_time / 3) {
                    parentLayout.setBackgroundResource(R.color.yellow);
                }
                if (time <= real_time / 3 && time > real_time / 3.5) {
                    parentLayout.setBackgroundResource(R.color.forestGreen);
                }
                if (time <= real_time / 3.5) {
                    parentLayout.setBackgroundResource(R.color.skyBlue);
                }

            }

            @Override
            public void onFinish() {

//                parentLayout.setBackgroundResource(R.color.white);
//                ((TextView) findViewById(R.id.title_name_textVIew))
//                        .setTextColor(getResources().getColor(R.color.black));
//                LinearLayout finishedLayout = findViewById(R.id.finishedLayout);
//                LinearLayout timerLayout = findViewById(R.id.timerLayout);
//                finishedLayout.setVisibility(View.VISIBLE);
//                timerLayout.setVisibility(View.GONE);
                Toast.makeText(TimerActivity.this, "FINISHED!", Toast.LENGTH_SHORT).show();
                utils.storeBoolean(
                        TimerActivity.this,
                        getIntent().getStringExtra("title_name")
                                + utils.getDate(), true);
                finish();

            }
        });

        findViewById(R.id.backBtnTimerActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTextview.stopTimer();
                finish();
            }
        });
        findViewById(R.id.startBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                countDownTextview.startTimer();
                findViewById(R.id.startBtn).setVisibility(View.GONE);
                findViewById(R.id.resumeBtn).setVisibility(View.GONE);
                findViewById(R.id.pauseBtn).setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.refreshBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                utils.storeLong(TimerActivity.this, "prev_time", 0);

                countDownTextview.startTimer();
                countDownTextview.pause();
                findViewById(R.id.startBtn).setVisibility(View.VISIBLE);
                findViewById(R.id.resumeBtn).setVisibility(View.GONE);
                findViewById(R.id.pauseBtn).setVisibility(View.GONE);
            }
        });
        findViewById(R.id.pauseBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTextview.pause();

                findViewById(R.id.resumeBtn).setVisibility(View.VISIBLE);
                findViewById(R.id.pauseBtn).setVisibility(View.GONE);
            }
        });
        findViewById(R.id.resumeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTextview.resume();
                findViewById(R.id.resumeBtn).setVisibility(View.GONE);
                findViewById(R.id.pauseBtn).setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    protected void onDestroy() {
        countDownTextview.pause();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        countDownTextview.pause();
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        countDownTextview.pause();
        super.onBackPressed();
    }
}