package com.fabianospdev.android.astrophoto.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.fabianospdev.android.astrophoto.R;
import com.fabianospdev.android.astrophoto.model.Photo;

import java.util.List;

public class RecyclerAdapter  extends RecyclerView.Adapter<RecyclerAdapter.ViewholderPhotos>
    implements  View.OnClickListener, View.OnTouchListener, View.OnLongClickListener{

  private View.OnClickListener clicklistener;
  private View.OnTouchListener touchListener;
  private View.OnLongClickListener longClickListener;
  private List<Photo> photos;
  private Context context;

  public RecyclerAdapter(List<Photo> photos){
    this.photos = photos;
  }

  @NonNull
  @Override
  public RecyclerAdapter.ViewholderPhotos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.itemsdetails,parent,false);
    view.setOnClickListener(this);
    view.setOnTouchListener(this);
    view.setOnLongClickListener(this);
    return new ViewholderPhotos(view);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerAdapter.ViewholderPhotos holder, int position) {
    Photo photo = photos.get(position);
    holder.code.setId(photo.getCode());
    holder.name.setText(photo.getName());
    holder.camera.setText(photo.getCamera());
    holder.lens.setText(photo.getLens());
    holder.schedule.setText(photo.getSchedule());
    holder.expose.setText(photo.getExposure());
    holder.iso_sensitivity.setText(photo.getIso_sensitivity());
    holder.diaphragm_opening.setText(photo.getDiaphragm_opening());
    holder.focal_distance.setText(photo.getFocal_distance());
    holder.dpi_resolution.setText(photo.getDpi_resolution());
    holder.flash_mode.setText(photo.getFlash_mode());
    holder.white_balance.setText(photo.getWhite_balance());
    holder.rotation.setText(photo.getRotation());
    holder.tags.setText(photo.getTags());
    holder.width.setText(photo.getWidth());
    holder.height.setText(photo.getHeight());
    holder.size.setText(photo.getSize());
    holder.path.setText(photo.getPath());
    holder.geolocation.setText(photo.getGeolocation());

  }

  @Override
  public int getItemCount() {
    return photos.size();
  }

  public void setOnClickListener(View.OnClickListener clicklistener){
    this.clicklistener = clicklistener;
  }
  public void setOnTouchListener(View.OnTouchListener touchListener){
    this.touchListener = touchListener;
  }
  public void setOnLongClickListener(View.OnLongClickListener longClickListener){
    this.longClickListener = longClickListener;
  }

  @Override
  public void onClick(View v) {
    if(clicklistener != null){
      clicklistener.onClick(v);
    }
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    if(touchListener != null){
      touchListener.onTouch(v, event);
    }
    return false;
  }

  @Override
  public boolean onLongClick(View v) {
    if(longClickListener != null){
      longClickListener.onLongClick(v);
    }
    return false;
  }

  public void removeItem(int position) {
    photos.remove(position);
    notifyItemRemoved(position);
  }

  public void restoreItem(Photo item, int position) {
    photos.add(position, item);
    notifyItemInserted(position);
  }

  /**  Classe interna Viewholder */
  public static class ViewholderPhotos extends RecyclerView.ViewHolder {
    TextView code;
    TextView name;
    TextView camera;
    TextView lens;
    TextView schedule;
    TextView expose;
    TextView iso_sensitivity;
    TextView diaphragm_opening;
    TextView focal_distance;
    TextView dpi_resolution;
    TextView flash_mode;
    TextView white_balance;
    TextView rotation;
    TextView tags;
    TextView width;
    TextView height;
    TextView size;
    TextView path;
    TextView geolocation;
    //byte[] image;

    public ViewholderPhotos(@NonNull View view) {
      super(view);
      code = view.findViewById(R.id.tvCode);
      name = view.findViewById(R.id.tvName);
      camera = view.findViewById(R.id.tvCamera);
      lens = view.findViewById(R.id.tvLens);
      schedule = view.findViewById(R.id.tvSchedule);
      expose = view.findViewById(R.id.tvExposure);
      iso_sensitivity = view.findViewById(R.id.tvIso);
      diaphragm_opening = view.findViewById(R.id.tvDiaphragm);
      focal_distance = view.findViewById(R.id.tvFocalDistance);
      dpi_resolution = view.findViewById(R.id.tvDpiResolution);
      flash_mode = view.findViewById(R.id.tvFlashMode);
      white_balance = view.findViewById(R.id.tvWhiteBalance);
      rotation = view.findViewById(R.id.tvRotation);
      tags = view.findViewById(R.id.tvTags);
      width = view.findViewById(R.id.tvWidth);
      height = view.findViewById(R.id.tvHeight);
      size = view.findViewById(R.id.tvSize);
      path = view.findViewById(R.id.tvPath);
      geolocation = view.findViewById(R.id.tvGeoLocation);
    }
  }
  /* Fim viewholder */
}
