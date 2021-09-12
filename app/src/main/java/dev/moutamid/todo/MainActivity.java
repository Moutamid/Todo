package dev.moutamid.todo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.fxn.stash.Stash;

import java.util.ArrayList;
import java.util.Collections;

import dev.moutamid.todo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final Context context = MainActivity.this;

    private ArrayList<Task> tasksArrayList = new ArrayList<>();
    private Utils utils = new Utils();

    private int dailyTimerMintsValue = 0;
    private int dailyTimerHoursValue = 0;

    private RecyclerView conversationRecyclerView;
    private RecyclerViewAdapterMessages adapter;

//    private EditText titleEditText;
//    private EditText daysEditText;
//    private TextView timeTextView;

    private EditText titleNameEditText, numberOfDaysEditText;
    private RelativeLayout dailyTimerLayout;
    private TextView dailyTimerTextView, saveBtnTextView;


    private int hoursInt = 0, minutesInt = 30;

    private RelativeLayout newTaskLayout;

//       Stash.put("TAG_DATA_ARRAYLIST",userArrayList);
//       ArrayList<User> userArrayListNew =
//                       Stash.getArrayList("TAG_DATA_ARRAYLIST", User.class);

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
//        setContentView(R.layout.activity_main);
        setContentView(view);


//        BottomSheetLayout layout = (BottomSheetLayout) findViewById(R.id.bottom_sheet_layout);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                layout.expand();
//            }
//        }, 3000);
//        layout.setOnProgressListener(new BottomSheetLayout.OnProgressListener() {
//            @Override
//            public void onProgress(float v) {
//                Toast.makeText(context, String.valueOf(v), Toast.LENGTH_SHORT).show();
//            }
//        });

        tasksArrayList = Stash.getArrayList("tasksArraylist", Task.class);

//        titleEditText = findViewById(R.id.titleTextviewMainactivity);
//        daysEditText = findViewById(R.id.daysTExtviewMainactivity);
//        timeTextView = findViewById(R.id.timeTExtviewMainactivity);

        newTaskLayout = findViewById(R.id.newTaskLayout);

        titleNameEditText = findViewById(R.id.title_name_et_new_task);
        numberOfDaysEditText = findViewById(R.id.number_of_days_et_new_task);
        dailyTimerLayout = findViewById(R.id.daily_timer_layout_new_task);
        dailyTimerTextView = findViewById(R.id.daily_timer_text_view_new_task);
        saveBtnTextView = findViewById(R.id.save_btn_text_view_new_task);

        initAddBtn();

//        tasksArrayList.add(new Task(false, "Task 1", 0, 0, 30, 0));
//        tasksArrayList.add(new Task(false, "Task 2", 0, 0, 35, 0));
//        tasksArrayList.add(new Task(false, "Task 3", 0, 0, 40, 0));

        Collections.reverse(tasksArrayList);

        initRecyclerView();

        /*// ADDING THE DIVIDER LINE BETWEEN RECYCLER VIEW ITEMS
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        conversationRecyclerView.addItemDecoration(dividerItemDecoration);*/

        // ADDING THE DRAG AND DROP FEATURE TO ITEMS
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(conversationRecyclerView);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(tasksArrayList, fromPosition, toPosition);

            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

            Stash.put("tasksArraylist", tasksArrayList);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };


    @Override
    public void onBackPressed() {
        if (newTaskLayout.getVisibility() == View.VISIBLE) {
            newTaskLayout.setVisibility(View.GONE);
            findViewById(R.id.tasksListLayout).requestFocus();
            findViewById(R.id.tasksListLayout).setClickable(true);

//            findViewById(R.id.tasksListLayout).setEnabled(false);


        } else super.onBackPressed();
    }

    private void initAddBtn() {
        findViewById(R.id.addBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                findViewById(R.id.tasksListLayout).setVisibility(View.GONE);
//                findViewById(R.id.addTaskLayout).setVisibility(View.VISIBLE);

                newTaskLayout.setVisibility(View.VISIBLE);
                newTaskLayout.requestFocus();
                newTaskLayout.setClickable(true);
//                findViewById(R.id.tasksListLayout).setClickable(false);
//                findViewById(R.id.tasksListLayout).setEnabled(false);

            }
        });

        findViewById(R.id.backgroundLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                newTaskLayout.setVisibility(View.GONE);
                findViewById(R.id.tasksListLayout).requestFocus();
                findViewById(R.id.tasksListLayout).setClickable(true);
//                findViewById(R.id.tasksListLayout).setClickable(true);
//                findViewById(R.id.tasksListLayout).setEnabled(true);

            }
        });

        timeTextViewClickListener();
        dailyTimerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.getMenuInflater().inflate(
                        R.menu.popup_menu_options,
                        popupMenu.getMenu()
                );
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id._15_mins_menu_id) {
                            dailyTimerMintsValue = 15;
                            dailyTimerTextView.setText("15 min");
                        }
                        if (menuItem.getItemId() == R.id._30_mins_menu_id) {
                            dailyTimerMintsValue = 30;
                            dailyTimerTextView.setText("30 min");
                        }
                        if (menuItem.getItemId() == R.id._45_mins_menu_id) {
                            dailyTimerMintsValue = 45;
                            dailyTimerTextView.setText("45 min");
                        }
                        if (menuItem.getItemId() == R.id._60_mins_menu_id) {
                            dailyTimerHoursValue = 1;
                            dailyTimerTextView.setText("60 min");
                        }
                        if (menuItem.getItemId() == R.id._75_mins_menu_id) {
                            dailyTimerHoursValue = 1;
                            dailyTimerMintsValue = 15;
                            dailyTimerTextView.setText("75 min");
                        }
                        if (menuItem.getItemId() == R.id._90_mins_menu_id) {
                            dailyTimerHoursValue = 1;
                            dailyTimerMintsValue = 30;
                            dailyTimerTextView.setText("90 min");
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

//        findViewById(R.id.addTaskBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (areEdittextsEmpty1()) {
//                    return;
//                }
//                Task task = new Task();
//
//                task.setTitle(titleNameEditText.getText().toString());
//                task.setDays(Integer.parseInt(daysEditText.getText().toString()));
//                task.setHours(hoursInt);
//                task.setMinutes(minutesInt);
//                task.setSeconds(0);
//
//                tasksArrayList.add(task);
//
//                Stash.put("tasksArraylist", tasksArrayList);
//
//                Collections.reverse(tasksArrayList);
//
//                findViewById(R.id.tasksListLayout).setVisibility(View.VISIBLE);
//                findViewById(R.id.addTaskLayout).setVisibility(View.GONE);
//
//                adapter.notifyDataSetChanged();
//            }
//        });

        saveBtnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (areEdittextsEmpty1()) {
                    return;
                }
                Task task = new Task();

                task.setTitle(titleNameEditText.getText().toString());
                task.setTotalDays(Integer.parseInt(numberOfDaysEditText.getText().toString()));
                task.setCompletedDays(0);
                task.setHours(dailyTimerHoursValue);
                task.setMinutes(dailyTimerMintsValue);

                Collections.reverse(tasksArrayList);

                tasksArrayList.add(task);

                Stash.put("tasksArraylist", tasksArrayList);

                Collections.reverse(tasksArrayList);

                newTaskLayout.setVisibility(View.GONE);

                titleNameEditText.setText("");
                numberOfDaysEditText.setText("");
                dailyTimerHoursValue = 0;
                dailyTimerMintsValue = 0;
                dailyTimerTextView.setText("Select daily timer (Optional)");

                hideKeyboard();

                adapter.notifyDataSetChanged();
            }
        });

    }

    private void hideKeyboard() {
        View v = getCurrentFocus();

        if (v != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void timeTextViewClickListener() {
//        timeTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Dialog dialog = new Dialog(context);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.layout_edit_task);
//                dialog.setCancelable(true);
//                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//                layoutParams.copyFrom(dialog.getWindow().getAttributes());
//                layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
//                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//                NumberPicker hoursPicker = dialog.findViewById(R.id.hourspicker);
//                hoursPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//                    @Override
//                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                        hoursInt = newVal;
//                    }
//                });
//                NumberPicker minutesPicker = dialog.findViewById(R.id.minutespicker);
//                minutesPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//                    @Override
//                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                        minutesInt = newVal;
//                    }
//                });
//
//                dialog.findViewById(R.id.saveBtn).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        // CODE HERE
//
//                        String hoursStr = hoursInt < 9 ? String.valueOf("0" + hoursInt) : String.valueOf(hoursInt);
//                        String minutesStr = minutesInt < 9 ? String.valueOf("0" + minutesInt) : String.valueOf(minutesInt);
//
//                        timeTextView.setText(hoursStr + ":" + minutesStr);
//
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//                dialog.getWindow().setAttributes(layoutParams);
//
//            }
//        });
    }

    private boolean areEdittextsEmpty1() {
        if (titleNameEditText.getText().toString().equals("")) {
            Toast.makeText(context, "Please enter a title!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (numberOfDaysEditText.getText().toString().equals("")) {
            Toast.makeText(context, "Please enter a time!", Toast.LENGTH_SHORT).show();
            return true;
        }
//        if (timeTextView.getText().toString().equals("Per session")) {
//            Toast.makeText(context, "Please enter a time!", Toast.LENGTH_SHORT).show();
//            return true;
//        }
        return false;
    }

    private void initRecyclerView() {

        conversationRecyclerView = findViewById(R.id.recyclerview);
        adapter = new RecyclerViewAdapterMessages();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        conversationRecyclerView.setLayoutManager(linearLayoutManager);
        conversationRecyclerView.setHasFixedSize(true);
        conversationRecyclerView.setNestedScrollingEnabled(false);

        conversationRecyclerView.setAdapter(adapter);

    }

    private int getPositionInteger() {
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("dev.moutamid.todo", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("current_position", 9999);
    }

    @Override
    protected void onResume() {
        super.onResume();

        int position = getPositionInteger();

        if (position == 9999)
            return;

        if (utils.getStoredBoolean(
                MainActivity.this,
                tasksArrayList.get(position).getTitle() + utils.getDate()
        )) {

            if (tasksArrayList.get(position).getCompletedDays()
                    < tasksArrayList.get(position).getTotalDays())
                tasksArrayList.get(position).setCompletedDays(
                        tasksArrayList.get(position).getCompletedDays() + 1
                );
            Stash.put("tasksArraylist", tasksArrayList);

            adapter.notifyDataSetChanged();

        }
    }

    private class RecyclerViewAdapterMessages extends RecyclerView.Adapter
            <RecyclerViewAdapterMessages.ViewHolderRightMessage> {

        private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

        public RecyclerViewAdapterMessages() {
            // uncomment the line below if you want to open only one row at a time
            viewBinderHelper.setOpenOnlyOne(true);
        }

        @NonNull
        @Override
        public ViewHolderRightMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tasks_item, parent, false);
            return new ViewHolderRightMessage(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolderRightMessage holder, int position) {

//            holder.isEnabled.setChecked(tasksArrayList.get(position).isEnabled());
            holder.title.setText(tasksArrayList.get(position).getTitle());

            if (tasksArrayList.get(position).getMinutes() != 0) {
                if (utils.getStoredBoolean(
                        MainActivity.this,
                        tasksArrayList.get(position).getTitle() + utils.getDate()
                )) {
                    holder.statusImage.setImageResource(R.drawable.ic_baseline_check_24);
                } else {
                    holder.statusImage.setImageResource(R.drawable.ic_time_clock_black);
                }
            } else {
                if (utils.getStoredBoolean(
                        MainActivity.this,
                        tasksArrayList.get(position).getTitle() + utils.getDate()
                )) {
                    holder.statusImage.setImageResource(R.drawable.ic_baseline_check_24);
                } else {
                    holder.statusImage.setImageResource(R.drawable.ic_circle_black);
                }
            }

//            holder.days.setText(String.valueOf(tasksArrayList.get(position).getDays()));
//            holder.hours.setText(String.valueOf(tasksArrayList.get(position).getHours()));
//            holder.minutes.setText(String.valueOf(tasksArrayList.get(position).getMinutes()));
//            holder.seconds.setText(String.valueOf(tasksArrayList.get(position).getSeconds()));

            holder.progressBar.setMax(tasksArrayList.get(position).getTotalDays());
            holder.progressBar.setProgress(tasksArrayList.get(position).getCompletedDays());
            setprogressbarColor(holder.progressBar,
                    tasksArrayList.get(position).getTotalDays(),
                    tasksArrayList.get(position).getCompletedDays());

            holder.delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    tasksArrayList.remove(holder.getAdapterPosition());
                    adapter.notifyItemRemoved(holder.getAdapterPosition());
                    Stash.put("tasksArraylist", tasksArrayList);
                    Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();

                }
            });

            holder.editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(context, "Edit button clicked", Toast.LENGTH_SHORT).show();
//                    newTaskLayout.setVisibility(View.VISIBLE);
//                    newTaskLayout.requestFocus();
//                    newTaskLayout.setClickable(true);/

                }
            });

            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tasksArrayList.get(position).getMinutes() == 0) {
                        utils.storeBoolean(
                                MainActivity.this,
                                tasksArrayList.get(position).getTitle()
                                        + utils.getDate(), true);

                        if (tasksArrayList.get(position).getCompletedDays() < tasksArrayList.get(position).getTotalDays())
                            tasksArrayList.get(position).setCompletedDays(
                                    tasksArrayList.get(position).getCompletedDays() + 1
                            );
                        Stash.put("tasksArraylist", tasksArrayList);
                        holder.statusImage.setImageResource(R.drawable.ic_baseline_check_24);
                    } else {// IF THERE IS A TIMER ON THE TASK

                        utils.storeInteger(MainActivity.this, "current_position", position);

                        Intent intent = new Intent(MainActivity.this, TimerActivity.class);
                        intent.putExtra("title_name", tasksArrayList.get(position).getTitle());
//                        intent.putExtra("days", tasksArrayList.get(position).getDays());
                        intent.putExtra("hours", tasksArrayList.get(position).getHours());
                        intent.putExtra("minutes", tasksArrayList.get(position).getMinutes());
//                        intent.putExtra("seconds", tasksArrayList.get(position).getSeconds());
                        startActivity(intent);

                    }

                    // IF TASK IS NOT COMPLETED
//                    if (!utils.getStoredBoolean(
//                            MainActivity.this,
//                            tasksArrayList.get(position).getTitle()
//                                    + utils.getDate()
//                    )) {
//                        utils.storeBoolean(
//                                MainActivity.this,
//                                tasksArrayList.get(position).getTitle()
//                                        + utils.getDate(), true);
//                        /
//                    }


                }
            });

//            holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//
//                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
//                    builder.setMessage("You sure you want to delete this task?")
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    tasksArrayList.remove(position);
//                                    Stash.put("tasksArraylist", tasksArrayList);
//                                    adapter.notifyDataSetChanged();
//                                    Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
//                                    dialogInterface.dismiss();
//                                }
//                            })
//                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.dismiss();
//                                }
//                            })
//                            .show();
//
//                    return false;
//                }
//            });

//            holder.isEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//                    tasksArrayList.get(position).setEnabled(b);
//                    Stash.put("tasksArraylist", tasksArrayList);
//                    Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
//
//                }
//            });
        }

        private void setprogressbarColor(ProgressBar bar, int totalDays, int completedDays) {

            Drawable progressDrawable = bar.getProgressDrawable().mutate();

            if (completedDays <= totalDays / 2 && completedDays > totalDays / 3) {

                progressDrawable.setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN);
                bar.setProgressDrawable(progressDrawable);
//                parentLayout.setBackgroundResource(R.color.yellow);
            }
            if (completedDays <= totalDays / 3 && completedDays > totalDays / 3.5) {

                progressDrawable.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
                bar.setProgressDrawable(progressDrawable);
//                parentLayout.setBackgroundResource(R.color.forestGreen);
            }
            if (completedDays <= totalDays / 3.5) {
                progressDrawable.setColorFilter(Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);
                bar.setProgressDrawable(progressDrawable);
//                parentLayout.setBackgroundResource(R.color.skyBlue);
            }
        }

        @Override
        public int getItemCount() {
            if (tasksArrayList == null)
                return 0;
            return tasksArrayList.size();
        }

        public class ViewHolderRightMessage extends RecyclerView.ViewHolder {

            TextView title, days, hours, minutes, seconds;
            //            CheckBox isEnabled;
            LinearLayout parentLayout;
            ImageView statusImage;
            ProgressBar progressBar;
            SwipeRevealLayout parentSwipeLayout;
            ImageView delBtn, editBtn;

            public ViewHolderRightMessage(@NonNull View v) {
                super(v);
                title = v.findViewById(R.id.titleTextview);
//                days = v.findViewById(R.id.daysTextview);
//                hours = v.findViewById(R.id.hoursTextview);
//                minutes = v.findViewById(R.id.minutesTextview);
//                seconds = v.findViewById(R.id.secondsTextview);
//                isEnabled = v.findViewById(R.id.isEnabledCheckbox);
                parentLayout = v.findViewById(R.id.taskLayout);
                statusImage = v.findViewById(R.id.task_item_status);
                progressBar = v.findViewById(R.id.progressbar_item_task);
                parentSwipeLayout = v.findViewById(R.id.parent_swipe_reveal_layout);
                editBtn = v.findViewById(R.id.edit_button_layout_task_item);
                delBtn = v.findViewById(R.id.delete_button_layout_task_item);

            }
        }

    }

    private static class Task {

        private String title;
        private int totalDays;
        private int completedDays;
        private int hours, minutes;

        public Task(String title, int totalDays, int completedDays, int hours, int minutes) {
            this.title = title;
            this.totalDays = totalDays;
            this.completedDays = completedDays;
            this.hours = hours;
            this.minutes = minutes;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTotalDays() {
            return totalDays;
        }

        public void setTotalDays(int totalDays) {
            this.totalDays = totalDays;
        }

        public int getCompletedDays() {
            return completedDays;
        }

        public void setCompletedDays(int completedDays) {
            this.completedDays = completedDays;
        }

        public int getHours() {
            return hours;
        }

        public void setHours(int hours) {
            this.hours = hours;
        }

        public int getMinutes() {
            return minutes;
        }

        public void setMinutes(int minutes) {
            this.minutes = minutes;
        }

        Task() {
        }
    }

}


/**
 * package dev.moutamid.todo;
 * <p>
 * import android.app.Dialog;
 * import android.content.Context;
 * import android.content.DialogInterface;
 * import android.content.Intent;
 * import android.os.Bundle;
 * import android.view.LayoutInflater;
 * import android.view.MenuItem;
 * import android.view.View;
 * import android.view.ViewGroup;
 * import android.view.Window;
 * import android.view.WindowManager;
 * import android.widget.CheckBox;
 * import android.widget.CompoundButton;
 * import android.widget.EditText;
 * import android.widget.ImageView;
 * import android.widget.LinearLayout;
 * import android.widget.RelativeLayout;
 * import android.widget.TextView;
 * import android.widget.Toast;
 * <p>
 * import androidx.annotation.NonNull;
 * import androidx.appcompat.app.AppCompatActivity;
 * import androidx.appcompat.app.AppCompatDelegate;
 * import androidx.appcompat.widget.PopupMenu;
 * import androidx.recyclerview.widget.LinearLayoutManager;
 * import androidx.recyclerview.widget.RecyclerView;
 * <p>
 * import com.fxn.stash.Stash;
 * import com.google.android.material.dialog.MaterialAlertDialogBuilder;
 * import com.shawnlin.numberpicker.NumberPicker;
 * <p>
 * import java.util.ArrayList;
 * import java.util.Collections;
 * <p>
 * public class MainActivity extends AppCompatActivity {
 * private static final String TAG = "MainActivity";
 * private final Context context = MainActivity.this;
 * <p>
 * private ArrayList<Task> tasksArrayList = new ArrayList<>();
 * <p>
 * private int dailyTimerMintsValue = 0;
 * private int dailyTimerHoursValue = 0;
 * <p>
 * private RecyclerView conversationRecyclerView;
 * private RecyclerViewAdapterMessages adapter;
 * <p>
 * private EditText titleEditText;
 * private EditText daysEditText;
 * private TextView timeTextView;
 * <p>
 * private EditText titleNameEditText, numberOfDaysEditText;
 * private RelativeLayout dailyTimerLayout;
 * private TextView dailyTimerTextView, saveBtnTextView;
 * <p>
 * <p>
 * private int hoursInt = 0, minutesInt = 30;
 * <p>
 * private RelativeLayout newTaskLayout;
 * <p>
 * //       Stash.put("TAG_DATA_ARRAYLIST",userArrayList);
 * //       ArrayList<User> userArrayListNew =
 * //                       Stash.getArrayList("TAG_DATA_ARRAYLIST", User.class);
 *
 * @Override protected void onCreate(Bundle savedInstanceState) {
 * super.onCreate(savedInstanceState);
 * AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
 * setContentView(R.layout.activity_main);
 * <p>
 * //        BottomSheetLayout layout = (BottomSheetLayout) findViewById(R.id.bottom_sheet_layout);
 * //        new Handler().postDelayed(new Runnable() {
 * //            @Override
 * //            public void run() {
 * //                layout.expand();
 * //            }
 * //        }, 3000);
 * //        layout.setOnProgressListener(new BottomSheetLayout.OnProgressListener() {
 * //            @Override
 * //            public void onProgress(float v) {
 * //                Toast.makeText(context, String.valueOf(v), Toast.LENGTH_SHORT).show();
 * //            }
 * //        });
 * <p>
 * tasksArrayList = Stash.getArrayList("tasksArraylist", Task.class);
 * <p>
 * titleEditText = findViewById(R.id.titleTextviewMainactivity);
 * daysEditText = findViewById(R.id.daysTExtviewMainactivity);
 * timeTextView = findViewById(R.id.timeTExtviewMainactivity);
 * <p>
 * newTaskLayout = findViewById(R.id.newTaskLayout);
 * <p>
 * titleNameEditText = findViewById(R.id.title_name_et_new_task);
 * numberOfDaysEditText = findViewById(R.id.number_of_days_et_new_task);
 * dailyTimerLayout = findViewById(R.id.daily_timer_layout_new_task);
 * dailyTimerTextView = findViewById(R.id.daily_timer_text_view_new_task);
 * saveBtnTextView = findViewById(R.id.save_btn_text_view_new_task);
 * <p>
 * initAddBtn();
 * <p>
 * //        tasksArrayList.add(new Task(false, "Task 1", 0, 0, 30, 0));
 * //        tasksArrayList.add(new Task(false, "Task 2", 0, 0, 35, 0));
 * //        tasksArrayList.add(new Task(false, "Task 3", 0, 0, 40, 0));
 * <p>
 * Collections.reverse(tasksArrayList);
 * <p>
 * initRecyclerView();
 * }
 * @Override public void onBackPressed() {
 * if (newTaskLayout.getVisibility() == View.VISIBLE) {
 * newTaskLayout.setVisibility(View.GONE);
 * findViewById(R.id.tasksListLayout).requestFocus();
 * findViewById(R.id.tasksListLayout).setClickable(true);
 * <p>
 * //            findViewById(R.id.tasksListLayout).setEnabled(false);
 * <p>
 * <p>
 * } else super.onBackPressed();
 * }
 * <p>
 * private void initAddBtn() {
 * findViewById(R.id.addBtn).setOnClickListener(new View.OnClickListener() {
 * @Override public void onClick(View view) {
 * //                findViewById(R.id.tasksListLayout).setVisibility(View.GONE);
 * //                findViewById(R.id.addTaskLayout).setVisibility(View.VISIBLE);
 * <p>
 * newTaskLayout.setVisibility(View.VISIBLE);
 * newTaskLayout.requestFocus();
 * newTaskLayout.setClickable(true);
 * //                findViewById(R.id.tasksListLayout).setClickable(false);
 * //                findViewById(R.id.tasksListLayout).setEnabled(false);
 * <p>
 * }
 * });
 * <p>
 * findViewById(R.id.backgroundLayout).setOnClickListener(new View.OnClickListener() {
 * @Override public void onClick(View view) {
 * <p>
 * newTaskLayout.setVisibility(View.GONE);
 * findViewById(R.id.tasksListLayout).requestFocus();
 * findViewById(R.id.tasksListLayout).setClickable(true);
 * //                findViewById(R.id.tasksListLayout).setClickable(true);
 * //                findViewById(R.id.tasksListLayout).setEnabled(true);
 * <p>
 * }
 * });
 * <p>
 * timeTextViewClickListener();
 * dailyTimerLayout.setOnClickListener(new View.OnClickListener() {
 * @Override public void onClick(View view) {
 * <p>
 * PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
 * popupMenu.getMenuInflater().inflate(
 * R.menu.popup_menu_options,
 * popupMenu.getMenu()
 * );
 * popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
 * @Override public boolean onMenuItemClick(MenuItem menuItem) {
 * if (menuItem.getItemId() == R.id._15_mins_menu_id) {
 * dailyTimerMintsValue = 15;
 * dailyTimerTextView.setText("15 min");
 * }
 * if (menuItem.getItemId() == R.id._30_mins_menu_id) {
 * dailyTimerMintsValue = 30;
 * dailyTimerTextView.setText("30 min");
 * }
 * if (menuItem.getItemId() == R.id._45_mins_menu_id) {
 * dailyTimerMintsValue = 45;
 * dailyTimerTextView.setText("45 min");
 * }
 * if (menuItem.getItemId() == R.id._60_mins_menu_id) {
 * dailyTimerHoursValue = 1;
 * dailyTimerTextView.setText("60 min");
 * }
 * if (menuItem.getItemId() == R.id._75_mins_menu_id) {
 * dailyTimerHoursValue = 1;
 * dailyTimerMintsValue = 15;
 * dailyTimerTextView.setText("75 min");
 * }
 * if (menuItem.getItemId() == R.id._90_mins_menu_id) {
 * dailyTimerHoursValue = 1;
 * dailyTimerMintsValue = 30;
 * dailyTimerTextView.setText("90 min");
 * }
 * return true;
 * }
 * });
 * popupMenu.show();
 * }
 * });
 * <p>
 * //        findViewById(R.id.addTaskBtn).setOnClickListener(new View.OnClickListener() {
 * //            @Override
 * //            public void onClick(View view) {
 * //                if (areEdittextsEmpty1()) {
 * //                    return;
 * //                }
 * //                Task task = new Task();
 * //
 * //                task.setTitle(titleNameEditText.getText().toString());
 * //                task.setDays(Integer.parseInt(daysEditText.getText().toString()));
 * //                task.setHours(hoursInt);
 * //                task.setMinutes(minutesInt);
 * //                task.setSeconds(0);
 * //
 * //                tasksArrayList.add(task);
 * //
 * //                Stash.put("tasksArraylist", tasksArrayList);
 * //
 * //                Collections.reverse(tasksArrayList);
 * //
 * //                findViewById(R.id.tasksListLayout).setVisibility(View.VISIBLE);
 * //                findViewById(R.id.addTaskLayout).setVisibility(View.GONE);
 * //
 * //                adapter.notifyDataSetChanged();
 * //            }
 * //        });
 * <p>
 * saveBtnTextView.setOnClickListener(new View.OnClickListener() {
 * @Override public void onClick(View view) {
 * if (areEdittextsEmpty1()) {
 * return;
 * }
 * Task task = new Task();
 * <p>
 * task.setTitle(titleNameEditText.getText().toString());
 * task.setDays(Integer.parseInt(numberOfDaysEditText.getText().toString()));
 * task.setHours(dailyTimerHoursValue);
 * task.setMinutes(dailyTimerMintsValue);
 * task.setSeconds(0);
 * <p>
 * tasksArrayList.add(task);
 * <p>
 * Stash.put("tasksArraylist", tasksArrayList);
 * <p>
 * Collections.reverse(tasksArrayList);
 * <p>
 * newTaskLayout.setVisibility(View.GONE);
 * <p>
 * adapter.notifyDataSetChanged();
 * }
 * });
 * <p>
 * }
 * <p>
 * private void timeTextViewClickListener() {
 * timeTextView.setOnClickListener(new View.OnClickListener() {
 * @Override public void onClick(View view) {
 * <p>
 * Dialog dialog = new Dialog(context);
 * dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
 * dialog.setContentView(R.layout.layout_edit_task);
 * dialog.setCancelable(true);
 * WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
 * layoutParams.copyFrom(dialog.getWindow().getAttributes());
 * layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
 * layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
 * <p>
 * NumberPicker hoursPicker = dialog.findViewById(R.id.hourspicker);
 * hoursPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
 * @Override public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
 * hoursInt = newVal;
 * }
 * });
 * NumberPicker minutesPicker = dialog.findViewById(R.id.minutespicker);
 * minutesPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
 * @Override public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
 * minutesInt = newVal;
 * }
 * });
 * <p>
 * dialog.findViewById(R.id.saveBtn).setOnClickListener(new View.OnClickListener() {
 * @Override public void onClick(View view) {
 * // CODE HERE
 * <p>
 * String hoursStr = hoursInt < 9 ? String.valueOf("0" + hoursInt) : String.valueOf(hoursInt);
 * String minutesStr = minutesInt < 9 ? String.valueOf("0" + minutesInt) : String.valueOf(minutesInt);
 * <p>
 * timeTextView.setText(hoursStr + ":" + minutesStr);
 * <p>
 * dialog.dismiss();
 * }
 * });
 * dialog.show();
 * dialog.getWindow().setAttributes(layoutParams);
 * <p>
 * }
 * });
 * }
 * <p>
 * private boolean areEdittextsEmpty1() {
 * if (titleNameEditText.getText().toString().equals("")) {
 * Toast.makeText(context, "Please enter a title!", Toast.LENGTH_SHORT).show();
 * return true;
 * }
 * if (numberOfDaysEditText.getText().toString().equals("")) {
 * Toast.makeText(context, "Please enter a time!", Toast.LENGTH_SHORT).show();
 * return true;
 * }
 * //        if (timeTextView.getText().toString().equals("Per session")) {
 * //            Toast.makeText(context, "Please enter a time!", Toast.LENGTH_SHORT).show();
 * //            return true;
 * //        }
 * return false;
 * }
 * <p>
 * private void initRecyclerView() {
 * <p>
 * conversationRecyclerView = findViewById(R.id.recyclerview);
 * adapter = new RecyclerViewAdapterMessages();
 * <p>
 * LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
 * <p>
 * conversationRecyclerView.setLayoutManager(linearLayoutManager);
 * conversationRecyclerView.setHasFixedSize(true);
 * conversationRecyclerView.setNestedScrollingEnabled(false);
 * <p>
 * conversationRecyclerView.setAdapter(adapter);
 * <p>
 * }
 * <p>
 * private class RecyclerViewAdapterMessages extends RecyclerView.Adapter
 * <RecyclerViewAdapterMessages.ViewHolderRightMessage> {
 * @NonNull
 * @Override public ViewHolderRightMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
 * View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tasks_item, parent, false);
 * return new ViewHolderRightMessage(view);
 * }
 * @Override public void onBindViewHolder(@NonNull final ViewHolderRightMessage holder, int position) {
 * <p>
 * holder.isEnabled.setChecked(tasksArrayList.get(position).isEnabled());
 * holder.title.setText(tasksArrayList.get(position).getTitle());
 * <p>
 * if ()
 * <p>
 * //            holder.days.setText(String.valueOf(tasksArrayList.get(position).getDays()));
 * //            holder.hours.setText(String.valueOf(tasksArrayList.get(position).getHours()));
 * //            holder.minutes.setText(String.valueOf(tasksArrayList.get(position).getMinutes()));
 * //            holder.seconds.setText(String.valueOf(tasksArrayList.get(position).getSeconds()));
 * <p>
 * holder.parentLayout.setOnClickListener(new View.OnClickListener() {
 * @Override public void onClick(View view) {
 * Intent intent = new Intent(MainActivity.this, TimerActivity.class);
 * intent.putExtra("title_name", tasksArrayList.get(position).getTitle());
 * intent.putExtra("days", tasksArrayList.get(position).getDays());
 * intent.putExtra("hours", tasksArrayList.get(position).getHours());
 * intent.putExtra("minutes", tasksArrayList.get(position).getMinutes());
 * intent.putExtra("seconds", tasksArrayList.get(position).getSeconds());
 * startActivity(intent);
 * }
 * });
 * <p>
 * holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
 * @Override public boolean onLongClick(View view) {
 * <p>
 * MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
 * builder.setMessage("You sure you want to delete this task?")
 * .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
 * @Override public void onClick(DialogInterface dialogInterface, int i) {
 * tasksArrayList.remove(position);
 * Stash.put("tasksArraylist", tasksArrayList);
 * adapter.notifyDataSetChanged();
 * Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
 * dialogInterface.dismiss();
 * }
 * })
 * .setNegativeButton("No", new DialogInterface.OnClickListener() {
 * @Override public void onClick(DialogInterface dialogInterface, int i) {
 * dialogInterface.dismiss();
 * }
 * })
 * .show();
 * <p>
 * return false;
 * }
 * });
 * <p>
 * holder.isEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
 * @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
 * <p>
 * tasksArrayList.get(position).setEnabled(b);
 * Stash.put("tasksArraylist", tasksArrayList);
 * Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
 * <p>
 * }
 * });
 * }
 * @Override public int getItemCount() {
 * if (tasksArrayList == null)
 * return 0;
 * return tasksArrayList.size();
 * }
 * <p>
 * public class ViewHolderRightMessage extends RecyclerView.ViewHolder {
 * <p>
 * TextView title, days, hours, minutes, seconds;
 * CheckBox isEnabled;
 * LinearLayout parentLayout;
 * ImageView statusImage;
 * <p>
 * public ViewHolderRightMessage(@NonNull View v) {
 * super(v);
 * title = v.findViewById(R.id.titleTextview);
 * //                days = v.findViewById(R.id.daysTextview);
 * //                hours = v.findViewById(R.id.hoursTextview);
 * //                minutes = v.findViewById(R.id.minutesTextview);
 * //                seconds = v.findViewById(R.id.secondsTextview);
 * isEnabled = v.findViewById(R.id.isEnabledCheckbox);
 * parentLayout = v.findViewById(R.id.taskLayout);
 * statusImage = v.findViewById(R.id.task_item_status);
 * <p>
 * }
 * }
 * <p>
 * }
 * <p>
 * private static class Task {
 * <p>
 * private boolean enabled;
 * private String title;
 * private int days, hours, minutes, seconds = 0;
 * <p>
 * Task() {
 * }
 * <p>
 * public Task(boolean enabled, String title, int days, int hours, int minutes, int seconds) {
 * this.enabled = enabled;
 * this.title = title;
 * this.days = days;
 * this.hours = hours;
 * this.minutes = minutes;
 * this.seconds = seconds;
 * }
 * <p>
 * public int getDays() {
 * return days;
 * }
 * <p>
 * public void setDays(int days) {
 * this.days = days;
 * }
 * <p>
 * public int getHours() {
 * return hours;
 * }
 * <p>
 * public void setHours(int hours) {
 * this.hours = hours;
 * }
 * <p>
 * public int getMinutes() {
 * return minutes;
 * }
 * <p>
 * public void setMinutes(int minutes) {
 * this.minutes = minutes;
 * }
 * <p>
 * public int getSeconds() {
 * return seconds;
 * }
 * <p>
 * public void setSeconds(int seconds) {
 * this.seconds = seconds;
 * }
 * <p>
 * public boolean isEnabled() {
 * return enabled;
 * }
 * <p>
 * public void setEnabled(boolean enabled) {
 * this.enabled = enabled;
 * }
 * <p>
 * public String getTitle() {
 * return title;
 * }
 * <p>
 * public void setTitle(String title) {
 * this.title = title;
 * }
 * }
 * <p>
 * }
 */