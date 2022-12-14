// Generated by view binder compiler. Do not edit!
package com.miguelrochefort.fitnesscamera.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.miguelrochefort.fitnesscamera.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityBoardBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button back;

  @NonNull
  public final Button btnSave;

  @NonNull
  public final EditText etContent;

  @NonNull
  public final EditText etTitle;

  @NonNull
  public final ConstraintLayout title;

  private ActivityBoardBinding(@NonNull LinearLayout rootView, @NonNull Button back,
      @NonNull Button btnSave, @NonNull EditText etContent, @NonNull EditText etTitle,
      @NonNull ConstraintLayout title) {
    this.rootView = rootView;
    this.back = back;
    this.btnSave = btnSave;
    this.etContent = etContent;
    this.etTitle = etTitle;
    this.title = title;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityBoardBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityBoardBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_board, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityBoardBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.back;
      Button back = ViewBindings.findChildViewById(rootView, id);
      if (back == null) {
        break missingId;
      }

      id = R.id.btn_save;
      Button btnSave = ViewBindings.findChildViewById(rootView, id);
      if (btnSave == null) {
        break missingId;
      }

      id = R.id.et_content;
      EditText etContent = ViewBindings.findChildViewById(rootView, id);
      if (etContent == null) {
        break missingId;
      }

      id = R.id.et_title;
      EditText etTitle = ViewBindings.findChildViewById(rootView, id);
      if (etTitle == null) {
        break missingId;
      }

      id = R.id.title;
      ConstraintLayout title = ViewBindings.findChildViewById(rootView, id);
      if (title == null) {
        break missingId;
      }

      return new ActivityBoardBinding((LinearLayout) rootView, back, btnSave, etContent, etTitle,
          title);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
