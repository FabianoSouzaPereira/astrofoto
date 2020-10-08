package com.fabianosdev.astrofoto.ui.home;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fabianosdev.astrofoto.Adapters.RecyclerAdapter;
import com.fabianosdev.astrofoto.ClienteConfig;
import com.fabianosdev.astrofoto.R;
import com.fabianosdev.astrofoto.SwipeToDeleteCallback;
import com.fabianosdev.astrofoto.database.Database;
import com.fabianosdev.astrofoto.model.Photo;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private Context context;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDataSetListener;
    private static String DataPictures;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    static int codigo = -1;
    RelativeLayout relativeLayout;
    BottomSheetDialog mBottomDialogNotificationAction;
    private boolean mItemPressed = false;
    private boolean itemReturned = false;
    private ArrayList<Photo> photos;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = root.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        final ArrayList<Photo> photos = new ArrayList<>();
        Database db = new Database(getActivity());
        List<Photo> pic = db.listAllPictures();
        for (Photo img : pic) {
            photos.add(img);
        }
        layoutManager = new LinearLayoutManager(root.getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecyclerAdapter(photos);

        /* Open Bottom sheet with item selected form recycleview */
        mAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codigo = photos.get(mRecyclerView.getChildAdapterPosition(v)).getCode();
                if (mItemPressed) {
                    // Multi-item swipes not handled
                    return;
                }
                Photo photo = db.selectPicture(codigo);
                showDialogNotificationAction(photo, codigo);
            }
        });

        /* Open Editable Activity, sending Extra info to Activity about its your new state*/
        mAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                codigo = photos.get(mRecyclerView.getChildAdapterPosition(v)).getCode();
                Intent i = new Intent(context.getApplicationContext(), ClienteConfig.class);
                String editar = "Editar";
                i.putExtra("Title", editar);
                i.putExtra("Codigo", codigo);
                startActivity(i);
               // finish();
                return false;
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        //start - Getting data from DatePikerDialog-----------
        mDisplayDate = root.findViewById(R.id.tvDate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        root.getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDataSetListener,
                        year, month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "" + year;
                mDisplayDate.setText(date);
            }
        };
        //end - Getting data from DatePikerDialog--------------

        return root;
    }

    /* Delete item from Recycle view and undo if until 3 seconds */
    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final Photo item = mAdapter.getData().get(position);
                mAdapter.removeItem(position);
                Snackbar snackbar = Snackbar
                        .make(mRecyclerView, "O item foi removido da lista.", Snackbar.LENGTH_LONG);
                snackbar.setAction("Cancelar", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemReturned = true;
                        mAdapter.restoreItem(item, position);
                        mRecyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
                codigo = item.getCode();
                handler.postAtTime(runnable, System.currentTimeMillis() + interval);
                handler.postDelayed(runnable, interval);

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(mRecyclerView);

    }

    private final int interval = 3000; // 3 Second
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable(){
        public void run() {
            if(!itemReturned) {
                Database db = new Database(getActivity());
                db.deletePicture(codigo);
              //  listDevices();
                itemReturned = false;
            }
        }
    };



    private void showDialogNotificationAction(Photo photo, int codigo) {
        //TODO fazer algo com a foto, como mandar pelo whatsapp ou outra coisa qualquer.
      /*  try {
            View sheetView = this.getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
            mBottomDialogNotificationAction = new BottomSheetDialog(this, codigo,0,0);
            mBottomDialogNotificationAction.setContentView(sheetView);
            mBottomDialogNotificationAction.show();

            // Remove default white color background
            FrameLayout bottomSheet = mBottomDialogNotificationAction.findViewById(R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                bottomSheet.setBackground(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }



    private byte[] imageStream(Bitmap bmp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,100,stream);
        final byte[] bytes = stream.toByteArray();
        return bytes;
    }

    private void addPicture(Photo picture){
        Database db = new Database(getActivity());
        db.addDBPicture(picture);
    }



    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }
}